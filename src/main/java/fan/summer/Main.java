package fan.summer;

import com.formdev.flatlaf.FlatIntelliJLaf;
import fan.summer.kitpage.KitPage;
import fan.summer.utils.SideMenuBar;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.io.File;
import java.net.URL;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class Main {

    private SideMenuBar sideMenuBar;
    private List<KitPage> pages;

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new Main().createAndShowGUI());
    }

    private void createAndShowGUI() {
        FlatIntelliJLaf.setup();

        UIManager.put("ProgressBar.arc", 10);
        UIManager.put("ProgressBar.foreground", new Color(64,158,255));
        UIManager.put("ProgressBar.background", new Color(240,240,240));
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
        System.out.println("提示: 未找到应用图标，请添加 icon.png 到 resources 目录");
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
            File packageDir = new File(packageURL.getFile());
            if (packageDir.exists() && packageDir.isDirectory()) {
                // Recursively scan package and its sub-packages
                List<Class<?>> pageClasses = scanPackage(packageDir, packageName, classLoader);
                
                // Sort by class name to ensure consistent order
                pageClasses.sort(Comparator.comparing(Class::getName));
                
                // Instantiate all page classes
                for (Class<?> clazz : pageClasses) {
                    try {
                        KitPage page = (KitPage) clazz.getDeclaredConstructor().newInstance();
                        pages.add(page);
                    } catch (Exception e) {
                        System.err.println("无法实例化页面: " + clazz.getName());
                    }
                }
            }
        }
        
        // If reflection scan fails, use fallback
        if (pages.isEmpty()) {
            System.out.println("警告: 未能自动扫描到页面，使用备用方案");
            fallbackInitPages();
        }
    }
    
    /**
     * Recursively scan package and its sub-packages
     */
    private List<Class<?>> scanPackage(File dir, String packageName, ClassLoader classLoader) {
        List<Class<?>> pageClasses = new ArrayList<>();
        
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
