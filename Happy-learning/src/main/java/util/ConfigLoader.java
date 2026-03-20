package util;

import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.JSONObject;

import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Loads request header configuration from netschool-headers.json.
 *
 * @author phoebej
 * @version 1.00
 */
public abstract class ConfigLoader {

    private static final JSONObject CONFIG;

    static {
        try (InputStream is = ConfigLoader.class
                .getClassLoader()
                .getResourceAsStream("netschool-headers.json")) {
            if (is == null) throw new IllegalStateException("netschool-headers.json not found");
            CONFIG = JSON.parseObject(new String(is.readAllBytes(), StandardCharsets.UTF_8));
        } catch (Exception e) {
            throw new ExceptionInInitializerError(e);
        }
    }

    // -------------------------  URL Constants  -------------------------

    public static String url(String key) {
        return CONFIG.getJSONObject("urls").getString("baseUrl")
                + CONFIG.getJSONObject("urls").getString(key);
    }

    public static String rawUrl(String key) {
        return CONFIG.getJSONObject("urls").getString(key);
    }

    // -------------------------  Headers Builder  -------------------------

    /**
     * Build headers for the specified profile and inject dynamic fields.
     *
     * @param profile  the key in profiles section of the JSON config
     * @param dynamics runtime dynamic headers to append, e.g. token, cookie
     */
    public static Map<String, String> headers(String profile, Map<String, String> dynamics) {
        Map<String, String> result = new LinkedHashMap<>();

        JSONObject profiles = CONFIG.getJSONObject("profiles");
        JSONObject common = CONFIG.getJSONObject("common");
        JSONObject target = profiles.getJSONObject(profile);

        if (target == null) throw new IllegalArgumentException("Unknown profile: " + profile);

        // 1. Apply common headers first
        String extendsFrom = target.getString("extends");
        if ("common".equals(extendsFrom)) {
            common.forEach((k, v) -> result.put(k, v.toString()));
        } else if (extendsFrom != null) {
            // Recursively resolve parent profile
            result.putAll(headers(extendsFrom, Collections.emptyMap()));
        }

        // 2. Override with current profile fields (skip extends meta field)
        target.forEach((k, v) -> {
            if (!"extends".equals(k)) result.put(k, v.toString());
        });

        // 3. Finally inject dynamic fields (token / cookie, etc.)
        if (dynamics != null) result.putAll(dynamics);

        return result;
    }

    /**
     * Overload without dynamic fields.
     */
    public static Map<String, String> headers(String profile) {
        return headers(profile, null);
    }
    // -------------------------  Lesson Type  -------------------------

    /**
     * Get the server-side lesson type string by UI key.
     * <p>
     * Example keys: {@code "MajorSubject"}, {@code "ElectiveSubject"}
     *
     * @param key the UI-facing lesson type key defined in lessonType section
     * @return the corresponding server-side string value
     * @throws IllegalArgumentException if the key does not exist
     */
    public static String lessonType(String key) {
        JSONObject lessonTypes = CONFIG.getJSONObject("lessonType");
        if (lessonTypes == null) throw new IllegalStateException("lessonType section not found in config");
        String value = lessonTypes.getString(key);
        if (value == null) throw new IllegalArgumentException("Unknown lessonType key: " + key);
        return value;
    }
}