package util;

import com.alibaba.fastjson2.JSON;

import java.net.URI;
import java.net.URLEncoder;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;
import java.time.Duration;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Utility class for HTTP requests and cookie parsing.
 *
 * @author phoebej
 * @version 1.00
 * @Date 2025/10/23
 */
public abstract class WebUtil {

    // -------------------------  Cookie Utils  -------------------------

    public static String getValueFromCookie(String cookie, String key) {
        return parseCookie(cookie).get(key);
    }

    public static Map<String, String> parseCookie(String cookie) {
        Map<String, String> result = new HashMap<>();
        if (cookie == null || cookie.trim().isEmpty()) {
            return result;
        }

        String[] cookies = cookie.split("[;,]");
        for (String ck : cookies) {
            if (ck.trim().isEmpty()) {
                continue;
            }
            String[] kv = ck.trim().split("=", 2);
            if (kv.length == 2) {
                result.put(kv[0].trim(), kv[1].trim());
            }
        }
        return result;
    }

    // -------------------------  HTTP Client  -------------------------

    private static final HttpClient HTTP_CLIENT = HttpClient.newBuilder()
            .connectTimeout(Duration.ofSeconds(10))
            .build();

    /**
     * Send a GET request.
     *
     * @param baseUrl the base URL
     * @param uri     the request path (relative or absolute)
     * @param headers the request headers
     * @param params  the query parameters (supports multiple values)
     * @param tClass  the target class for response body deserialization
     */
    public static <T> T sendGetRequest(String baseUrl,
                                       String uri,
                                       Map<String, String> headers,
                                       Map<String, List<String>> params,
                                       Class<T> tClass) {
        URI fullUri = buildUri(baseUrl, uri, params);

        HttpRequest.Builder requestBuilder = HttpRequest.newBuilder()
                .uri(fullUri)
                .GET();

        if (headers != null) {
            headers.forEach(requestBuilder::header);
        }

        return execute(requestBuilder.build(), tClass);
    }

    /**
     * Send a POST request.
     *
     * @param baseUrl     the base URL
     * @param uri         the request path
     * @param headers     the request headers (supports multiple values)
     * @param requestBody the request body object, sends empty body if null
     * @param tClass      the target class for response body deserialization
     */
    public static <T> T sendPostRequest(String baseUrl,
                                        String uri,
                                        Map<String, List<String>> headers,
                                        Object requestBody,
                                        Class<T> tClass) {
        HttpRequest.BodyPublisher bodyPublisher = HttpRequest.BodyPublishers.noBody();

        if (requestBody != null) {
            String json = JSON.toJSONString(requestBody);  // fastjson2 serialization
            bodyPublisher = HttpRequest.BodyPublishers.ofString(json);
        }

        HttpRequest.Builder requestBuilder = HttpRequest.newBuilder()
                .uri(URI.create(baseUrl + uri))
                .header("Content-Type", "application/json")
                .POST(bodyPublisher);

        if (headers != null) {
            headers.forEach((key, values) ->
                    values.forEach(value -> requestBuilder.header(key, value)));
        }

        return execute(requestBuilder.build(), tClass);
    }

    // -------------------------  Private Methods  -------------------------

    private static <T> T execute(HttpRequest request, Class<T> tClass) {
        try {
            HttpResponse<String> response = HTTP_CLIENT.send(
                    request, HttpResponse.BodyHandlers.ofString());

            int statusCode = response.statusCode();
            if (statusCode >= 400 && statusCode < 500) {
                throw new RuntimeException("Client error, status code: " + statusCode);
            }
            if (statusCode >= 500) {
                throw new RuntimeException("Server error, status code: " + statusCode);
            }

            String body = response.body();
            if (tClass == String.class) {
                return tClass.cast(body);
            }
            return JSON.parseObject(body, tClass);  // fastjson2 deserialization

        } catch (RuntimeException e) {
            throw e;
        } catch (Exception e) {
            throw new RuntimeException("HTTP request execution failed", e);
        }
    }

    private static URI buildUri(String baseUrl, String uri, Map<String, List<String>> params) {
        String base = uri.startsWith("http") ? uri : baseUrl + uri;

        if (params == null || params.isEmpty()) {
            return URI.create(base);
        }

        StringBuilder sb = new StringBuilder(base).append("?");
        params.forEach((key, values) -> {
            for (String value : values) {
                if (sb.charAt(sb.length() - 1) != '?') sb.append("&");
                sb.append(encode(key)).append("=").append(encode(value));
            }
        });

        return URI.create(sb.toString());
    }

    private static String encode(String value) {
        return URLEncoder.encode(value, StandardCharsets.UTF_8);
    }
}