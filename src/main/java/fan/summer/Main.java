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

        JFrame frame = new JFrame("Swiss Kit");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLocationRelativeTo(null);
        
        // 设置应用图标
        setAppIcon(frame);

        // 初始化页面
        initPages();

        // 内容面板
        JPanel contentPanel = new JPanel(new BorderLayout());
        contentPanel.setBorder(new EmptyBorder(20, 20, 20, 20));
        contentPanel.setBackground(Color.WHITE);

        // 侧边菜单栏
        sideMenuBar = new SideMenuBar(pages, contentPanel);

        // 主面板
        JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.add(sideMenuBar, BorderLayout.WEST);
        mainPanel.add(contentPanel, BorderLayout.CENTER);

        frame.add(mainPanel);

        // 默认选中第一个页面
        sideMenuBar.selectPage(0);

        frame.pack();
        frame.setMinimumSize(new Dimension(800, 500));
        frame.setVisible(true);
    }
    
    /**
     * 设置应用图标
     * 将图标文件放入 resources 目录，支持: icon.png, icon.jpg, app.png
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
        // 未找到图标文件时使用默认
        System.out.println("提示: 未找到应用图标，请添加 icon.png 到 resources 目录");
    }

    /**
     * 自动扫描并注册所有 KitPage 实现类
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
                // 递归扫描包及其子包
                List<Class<?>> pageClasses = scanPackage(packageDir, packageName, classLoader);
                
                // 按类名排序，确保顺序一致
                pageClasses.sort(Comparator.comparing(Class::getName));
                
                // 实例化所有页面类
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
        
        // 如果反射扫描失败，使用备用方案
        if (pages.isEmpty()) {
            System.out.println("警告: 未能自动扫描到页面，使用备用方案");
            fallbackInitPages();
        }
    }
    
    /**
     * 递归扫描包及其子包
     */
    private List<Class<?>> scanPackage(File dir, String packageName, ClassLoader classLoader) {
        List<Class<?>> pageClasses = new ArrayList<>();
        
        File[] files = dir.listFiles();
        if (files == null) return pageClasses;
        
        for (File file : files) {
            if (file.isDirectory()) {
                // 递归扫描子包
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
                    // 忽略无法加载的类
                }
            }
        }
        
        return pageClasses;
    }
    
    /**
     * 备用方案：手动注册页面（仅在反射失败时使用）
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
