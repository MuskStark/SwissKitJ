package fan.summer.utils;

import javax.swing.*;
import java.awt.*;

/**
 * UI 通用工具类
 */
public class UIUtils {
    
    // 常用颜色
    public static final Color PRIMARY_COLOR = new Color(0xBB, 0x86, 0xFC);
    public static final Color TEXT_COLOR = new Color(0x60, 0x60, 0x60);
    public static final Color LIGHT_GRAY = new Color(0xF3, 0xF3, 0xF3);
    public static final Color DIVIDER_COLOR = new Color(0xE0, 0xE0, 0xE0);
    
    /**
     * 创建带标题的区域面板
     */
    public static JPanel createSectionPanel(String title, JComponent content) {
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setBackground(Color.WHITE);
        
        JLabel label = new JLabel(title);
        label.setFont(new Font("SansSerif", Font.PLAIN, 13));
        label.setForeground(TEXT_COLOR);
        label.setAlignmentX(Component.LEFT_ALIGNMENT);
        
        content.setAlignmentX(Component.LEFT_ALIGNMENT);
        
        panel.add(label);
        panel.add(Box.createVerticalStrut(8));
        panel.add(content);
        
        return panel;
    }
    
    /**
     * 创建文件选择面板
     */
    public static JPanel createFilePickerPanel(String label, JTextField textField, JButton button) {
        JPanel panel = new JPanel(new BorderLayout(10, 0));
        panel.add(textField, BorderLayout.CENTER);
        panel.add(button, BorderLayout.EAST);
        return panel;
    }
    
    /**
     * 创建文件夹选择面板
     */
    public static JPanel createFolderPickerPanel(String label, JTextField textField, JButton button) {
        JPanel panel = new JPanel(new BorderLayout(10, 0));
        textField.setEditable(false);
        panel.add(textField, BorderLayout.CENTER);
        panel.add(button, BorderLayout.EAST);
        return panel;
    }
    
    /**
     * 创建页面标题
     */
    public static JLabel createPageTitle(String title) {
        JLabel label = new JLabel(title);
        label.setFont(new Font("SansSerif", Font.BOLD, 18));
        return label;
    }
    
    /**
     * 创建文本域（带滚动）
     */
    public static JScrollPane createScrollableTextArea(JTextArea textArea, int rows, int cols) {
        textArea.setFont(new Font("Monospaced", Font.PLAIN, 12));
        textArea.setEditable(false);
        JScrollPane scrollPane = new JScrollPane(textArea);
        scrollPane.setPreferredSize(new Dimension(0, textArea.getFont().getSize() * rows + 20));
        return scrollPane;
    }
    
    /**
     * 创建进度条
     */
    public static JProgressBar createProgressBar() {
        JProgressBar progressBar = new JProgressBar();
        progressBar.setStringPainted(true);
        progressBar.setVisible(false);
        return progressBar;
    }
    
    /**
     * 创建居中按钮面板
     */
    public static JPanel createCenterButtonPanel(JButton... buttons) {
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        for (JButton btn : buttons) {
            panel.add(btn);
        }
        return panel;
    }
    
    /**
     * 创建空白间隔
     */
    public static Component createVerticalStrut(int size) {
        return Box.createVerticalStrut(size);
    }
    
    /**
     * 创建水平空白间隔
     */
    public static Component createHorizontalStrut(int size) {
        return Box.createHorizontalStrut(size);
    }
}
