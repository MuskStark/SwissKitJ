package fan.summer.kitpage;

import fan.summer.annoattion.SwissKitPage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.net.URL;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

/**
 * Scanner for automatically discovering and registering KitPage implementations.
 * Scans specified packages for classes annotated with @SwissKitPage.
 *
 * @author phoebej
 * @version 1.00
 * @Date 2026/3/1
 */
public class KitPageScanner {
    private static final Logger logger = LoggerFactory.getLogger(KitPageScanner.class);

    /**
     * Scan packages for KitPage implementations.
     *
     * @param packageNames packages to scan
     * @return list of KitPage instances sorted by order
     */
    public static List<KitPage> scan(String... packageNames) {
        List<KitPage> pages = new ArrayList<>();

        for (String packageName : packageNames) {
            pages.addAll(scanPackage(packageName));
        }

        // Sort by order
        pages.sort(Comparator.comparingInt(p -> getOrder(p)));
        
        logger.info("Scanned {} KitPage(s) from packages: {}", pages.size(), String.join(", ", packageNames));
        return pages;
    }

    /**
     * Scan a single package and its sub-packages for KitPage implementations.
     *
     * @param packageName package to scan
     * @return list of KitPage instances
     */
    private static List<KitPage> scanPackage(String packageName) {
        List<KitPage> pages = new ArrayList<>();
        String packagePath = packageName.replace('.', '/');

        try {
            ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
            if (classLoader == null) {
                classLoader = KitPageScanner.class.getClassLoader();
            }

            URL packageUrl = classLoader.getResource(packagePath);
            if (packageUrl == null) {
                logger.warn("Package not found: {}", packageName);
                return pages;
            }

            File packageDir = new File(packageUrl.getPath());
            if (!packageDir.exists() || !packageDir.isDirectory()) {
                // Try scanning from JAR
                return pages;
            }

            scanDirectory(packageDir, packageName, classLoader, pages);
            
        } catch (Exception e) {
            logger.error("Error scanning package {}: {}", packageName, e.getMessage(), e);
        }

        return pages;
    }
    
    /**
     * Recursively scan directory for KitPage implementations.
     */
    private static void scanDirectory(File dir, String packageName, ClassLoader classLoader, List<KitPage> pages) {
        File[] files = dir.listFiles();
        if (files == null) return;

        for (File file : files) {
            if (file.isDirectory()) {
                // Recursively scan sub-packages
                String subPackageName = packageName + "." + file.getName();
                scanDirectory(file, subPackageName, classLoader, pages);
            } else if (file.getName().endsWith(".class") && !file.getName().equals("KitPage.class")) {
                String className = packageName + "." + file.getName().replace(".class", "");
                try {
                    Class<?> clazz = classLoader.loadClass(className);
                    if (isKitPageImplementation(clazz)) {
                        KitPage instance = (KitPage) clazz.getDeclaredConstructor().newInstance();
                        if (isVisible(instance)) {
                            pages.add(instance);
                            logger.info("Registered KitPage: {} (order: {})",
                                    getMenuName(instance), getOrder(instance));
                        } else {
                            logger.debug("Skipped invisible KitPage: {}", className);
                        }
                    }
                } catch (Exception e) {
                    logger.debug("Failed to load class {}: {}", className, e.getMessage());
                }
            }
        }
    }

    /**
     * Check if a class is a KitPage implementation with @SwissKitPage annotation.
     *
     * @param clazz class to check
     * @return true if it's a KitPage implementation
     */
    private static boolean isKitPageImplementation(Class<?> clazz) {
        return KitPage.class.isAssignableFrom(clazz)
                && clazz.isAnnotationPresent(SwissKitPage.class);
    }

    /**
     * Get menu name from annotation or use class simple name.
     *
     * @param page KitPage instance
     * @return menu name
     */
    private static String getMenuName(KitPage page) {
        SwissKitPage annotation = page.getClass().getAnnotation(SwissKitPage.class);
        if (annotation != null && !annotation.menuName().isEmpty()) {
            return annotation.menuName();
        }
        return page.getClass().getSimpleName();
    }

    /**
     * Get order from annotation.
     *
     * @param page KitPage instance
     * @return order value
     */
    private static int getOrder(KitPage page) {
        SwissKitPage annotation = page.getClass().getAnnotation(SwissKitPage.class);
        if (annotation != null) {
            return annotation.order();
        }
        return Integer.MAX_VALUE;
    }

    /**
     * Check if page is visible.
     *
     * @param page KitPage instance
     * @return true if visible
     */
    private static boolean isVisible(KitPage page) {
        SwissKitPage annotation = page.getClass().getAnnotation(SwissKitPage.class);
        if (annotation != null) {
            return annotation.visible();
        }
        return true;
    }
}
