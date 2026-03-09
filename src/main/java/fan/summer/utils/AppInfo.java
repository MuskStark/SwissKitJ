package fan.summer.utils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.InputStream;
import java.net.URL;
import java.util.Properties;
import java.util.jar.Attributes;
import java.util.jar.Manifest;

/**
 * Application version constants.
 * Version should match pom.xml <version> tag.
 */
public abstract class AppInfo {
    private static final Logger logger = LoggerFactory.getLogger(AppInfo.class);

    public static final String VERSION = resolveVersion();
    public static final String NAME    = loadProperty("app.name", "SwissKit");

    /**
     * 版本号读取策略：
     *  1. 优先从 JAR 的 MANIFEST.MF 读 Implementation-Version（打包后生效）
     *  2. 读不到则从 app.properties 读（IntelliJ 直接 Run 时的兜底，值为 "dev"）
     */
    private static String resolveVersion() {

        try {

            String className = AppInfo.class.getSimpleName() + ".class";
            String classPath = AppInfo.class.getResource(className).toString();
            if (classPath.startsWith("jar:")) {
                String manifestPath = classPath.substring(0, classPath.lastIndexOf("!") + 1)
                        + "/META-INF/MANIFEST.MF";
                Manifest manifest = new Manifest(new URL(manifestPath).openStream());
                Attributes attrs = manifest.getMainAttributes();
                String version = attrs.getValue("Implementation-Version");
                if (version != null && !version.isBlank()) {
                    logger.debug("Version from MANIFEST: {}", version);
                    return version;
                }
            }
        } catch (Exception e) {
            logger.debug("MANIFEST not available, falling back to app.properties");
        }


        return loadProperty("app.version", "dev");
    }

    private static String loadProperty(String key, String defaultValue) {
        try (InputStream is = AppInfo.class.getResourceAsStream("/app.properties")) {
            if (is == null) return defaultValue;
            Properties props = new Properties();
            props.load(is);
            return props.getProperty(key, defaultValue);
        } catch (Exception e) {
            logger.warn("Failed to read {}", key, e);
            return defaultValue;
        }
    }

    public static String getVersion() { return VERSION; }
    public static String getName()    { return NAME; }
    public static String getFullName() { return NAME + "-" + VERSION; }
}
