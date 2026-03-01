package fan.summer.ui.home;

import com.formdev.flatlaf.FlatIntelliJLaf;
import fan.summer.kitpage.KitPage;
import fan.summer.ui.sidebar.SideMenuBar;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.io.File;
import java.net.URL;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

/**
 * Home page class that serves as the main application container.
 * Manages the application window, side menu bar, and automatic page discovery.
 *
 * @author phoebej
 * @version 1.00
 * @Date 2026/3/1
 */
public class HomePage {

    private static final Logger logger = LoggerFactory.getLogger(HomePage.class);

    private SideMenuBar sideMenuBar;
    private List<KitPage> pages;

    public void init() {
        FlatIntelliJLaf.setup();

        UIManager.put("ProgressBar.arc", 10);
        UIManager.put("ProgressBar.foreground", new Color(64, 158, 255));
        UIManager.put("ProgressBar.background", new Color(240, 240, 240));
        UIManager.put("ProgressBar.selectionForeground", Color.WHITE);
        UIManager.put("ProgressBar.selectionBackground", Color.GRAY);

        JFrame frame = new JFrame("Swiss Kit");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLocationRelativeTo(null);

        // Set application icon
        setAppIcon(frame);

        // Initialize pages
        initPages();

        // Content panel
        JPanel contentPanel = new JPanel(new BorderLayout());
        contentPanel.setBorder(new EmptyBorder(20, 20, 20, 20));
        contentPanel.setBackground(Color.WHITE);

        // Side menu bar
        sideMenuBar = new SideMenuBar(pages, contentPanel);

        // Main panel
        JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.add(sideMenuBar, BorderLayout.WEST);
        mainPanel.add(contentPanel, BorderLayout.CENTER);

        frame.add(mainPanel);

        // Select the first page by default
        sideMenuBar.selectPage(0);

//        frame.pack();
        frame.setSize(new Dimension(900, 600));
        frame.setMinimumSize(new Dimension(800, 500));
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }

    /**
     * Set application icon
     * Place icon file in resources directory, supports: icon.png, icon.jpg, app.png
     */
    private void setAppIcon(JFrame frame) {
        String[] iconPaths = {"/icon.png", "/icon.jpg", "/app.png"};

        for (String path : iconPaths) {
            URL url = getClass().getResource(path);
            if (url != null) {
                frame.setIconImage(new ImageIcon(url).getImage());
                return;
            }
        }
        // Use default when icon file not found
        logger.info("Application icon not found. Please add icon.png to resources directory");
    }

    /**
     * Automatically scan and register all KitPage implementation classes
     */
    private void initPages() {
        pages = new ArrayList<>();

        String packageName = "fan.summer.kitpage";
        String packagePath = packageName.replace('.', '/');

        ClassLoader classLoader = getClass().getClassLoader();
        URL packageURL = classLoader.getResource(packagePath);

        if (packageURL != null) {
            java.util.List<Class<?>> pageClasses;

            if (packageURL.getProtocol().equals("jar")) {
                // Scan from JAR file
                logger.info("Scanning pages from JAR file");
                pageClasses = scanPackageFromJar(packageURL, packagePath, classLoader);
            } else {
                // Scan from file system
                logger.info("Scanning pages from file system");
                File packageDir = new File(packageURL.getFile());
                if (packageDir.exists() && packageDir.isDirectory()) {
                    pageClasses = scanPackage(packageDir, packageName, classLoader);
                } else {
                    pageClasses = new ArrayList<>();
                }
            }

            // Sort by class name to ensure consistent order
            pageClasses.sort(Comparator.comparing(Class::getName));

            // Instantiate all page classes
            for (Class<?> clazz : pageClasses) {
                try {
                    KitPage page = (KitPage) clazz.getDeclaredConstructor().newInstance();
                    pages.add(page);
                    logger.info("Loaded page: {}", clazz.getSimpleName());
                } catch (Exception e) {
                    logger.error("Failed to instantiate page: {}", clazz.getName(), e);
                }
            }
        }

        // If reflection scan fails, use fallback
        if (pages.isEmpty()) {
            logger.warn("Failed to scan pages automatically, using fallback method");
            fallbackInitPages();
        }

        logger.info("Total pages loaded: {}", pages.size());
    }

    /**
     * Recursively scan package and its sub-packages (file system)
     */
    private java.util.List<Class<?>> scanPackage(File dir, String packageName, ClassLoader classLoader) {
        java.util.List<Class<?>> pageClasses = new ArrayList<>();

        File[] files = dir.listFiles();
        if (files == null) return pageClasses;

        for (File file : files) {
            if (file.isDirectory()) {
                // Recursively scan sub-packages
                String subPackageName = packageName + "." + file.getName();
                pageClasses.addAll(scanPackage(file, subPackageName, classLoader));
            } else if (file.getName().endsWith(".class") && !file.getName().equals("KitPage.class")) {
                String className = packageName + "." + file.getName().substring(0, file.getName().length() - 6);
                try {
                    Class<?> clazz = classLoader.loadClass(className);
                    if (KitPage.class.isAssignableFrom(clazz) && !clazz.isInterface()) {
                        pageClasses.add(clazz);
                    }
                } catch (ClassNotFoundException | NoClassDefFoundError e) {
                    // Ignore classes that cannot be loaded
                }
            }
        }

        return pageClasses;
    }

    /**
     * Scan package from JAR file
     */
    private java.util.List<Class<?>> scanPackageFromJar(URL jarUrl, String packagePath, ClassLoader classLoader) {
        List<Class<?>> pageClasses = new ArrayList<>();

        try {
            String jarPath = jarUrl.getPath().substring(5, jarUrl.getPath().indexOf("!"));
            java.util.jar.JarFile jarFile = new java.util.jar.JarFile(jarPath);
            java.util.Enumeration<java.util.jar.JarEntry> entries = jarFile.entries();

            String basePath = packagePath + "/";

            while (entries.hasMoreElements()) {
                java.util.jar.JarEntry entry = entries.nextElement();
                String entryName = entry.getName();

                if (entryName.startsWith(basePath) && entryName.endsWith(".class")) {
                    String className = entryName.replace('/', '.').substring(0, entryName.length() - 6);

                    if (!className.equals("fan.summer.kitpage.KitPage")) {
                        try {
                            Class<?> clazz = classLoader.loadClass(className);
                            if (KitPage.class.isAssignableFrom(clazz) && !clazz.isInterface()) {
                                pageClasses.add(clazz);
                            }
                        } catch (ClassNotFoundException | NoClassDefFoundError e) {
                            // Ignore classes that cannot be loaded
                        }
                    }
                }
            }

            jarFile.close();
        } catch (Exception e) {
            logger.error("Failed to scan JAR file: {}", e.getMessage(), e);
        }

        return pageClasses;
    }

    /**
     * Fallback: Manually register pages (only used when reflection fails)
     */
    private void fallbackInitPages() {
        pages = new ArrayList<>();
        try {
            pages.add((KitPage) Class.forName("fan.summer.kitpage.WelcomePage").getDeclaredConstructor().newInstance());
            pages.add((KitPage) Class.forName("fan.summer.kitpage.email.EmailPage").getDeclaredConstructor().newInstance());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
