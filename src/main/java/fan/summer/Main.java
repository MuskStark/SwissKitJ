package fan.summer;

import com.formdev.flatlaf.FlatIntelliJLaf;

import fan.summer.ui.ToolPage;
import fan.summer.ui.common.UIUtils;
import fan.summer.ui.common.WelcomePage;
import fan.summer.ui.excel.ExcelAddColumnPage;
import fan.summer.ui.excel.ExcelSplitPage;
import fan.summer.ui.email.EmailPage;
import fan.summer.ui.sql.SqlQueryPage;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.List;

public class Main {
    
    private static final int NAV_WIDTH = 220;
    
    private JList<String> navList;
    private JPanel contentPanel;
    private DefaultListModel<String> listModel;
    private List<ToolPage> pages;
    
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new Main().createAndShowGUI());
    }
    
    private void createAndShowGUI() {
        FlatIntelliJLaf.setup();
        
        JFrame frame = new JFrame("Swiss Kit");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLocationRelativeTo(null);
        
        // 初始化页面
        initPages();
        
        // 主面板
        JPanel mainPanel = new JPanel(new BorderLayout());
        
        // 左侧导航
        JPanel navPanel = createNavigationPanel();
        
        // 右侧内容
        contentPanel = new JPanel(new BorderLayout());
        contentPanel.setBorder(new EmptyBorder(20, 20, 20, 20));
        contentPanel.add(pages.get(0).getPanel(), BorderLayout.CENTER);
        
        mainPanel.add(navPanel, BorderLayout.WEST);
        mainPanel.add(contentPanel, BorderLayout.CENTER);
        
        frame.add(mainPanel);
        
        frame.pack();
        frame.setMinimumSize(new Dimension(800, 500));
        frame.setVisible(true);
    }
    
    private void initPages() {
        pages = new ArrayList<>();
        pages.add(new WelcomePage());                        // 0: 欢迎页
        pages.add(new ExcelAddColumnPage());                 // 1: Excel增加英文列
        pages.add(new ExcelSplitPage());                     // 2: Excel拆分
        pages.add(new SqlQueryPage());                       // 3: SQL查询
        pages.add(new EmailPage());                          // 4: 邮件发送
    }
    
    private JPanel createNavigationPanel() {
        JPanel navPanel = new JPanel(new BorderLayout());
        navPanel.setPreferredSize(new Dimension(NAV_WIDTH, 0));
        navPanel.setBackground(UIUtils.LIGHT_GRAY);
        
        // 标题
        JLabel titleLabel = new JLabel("Swiss Kit");
        titleLabel.setFont(new Font("SansSerif", Font.BOLD, 18));
        titleLabel.setBorder(new EmptyBorder(20, 20, 20, 20));
        
        // 导航项
        String[] navItems = {
            "📊 Excel增加英文列工具",
            "📄 Excel文件拆分工具",
            "🔍 Sql待查询值生成",
            "📧 邮件批量发送"
        };
        
        listModel = new DefaultListModel<>();
        for (String item : navItems) {
            listModel.addElement(item);
        }
        
        navList = new JList<>(listModel);
        navList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        navList.setSelectedIndex(0);
        navList.setBackground(UIUtils.LIGHT_GRAY);
        navList.setBorder(new EmptyBorder(10, 0, 0, 0));
        navList.setCellRenderer(new DefaultListCellRenderer() {
            @Override
            public Component getListCellRendererComponent(JList<?> list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
                JLabel label = (JLabel) super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
                label.setOpaque(true);
                if (isSelected) {
                    label.setBackground(new Color(0x2D, 0x2D, 0x2D));
                    label.setForeground(new Color(0xBB, 0x86, 0xFC));
                } else {
                    label.setBackground(UIUtils.LIGHT_GRAY);
                    label.setForeground(UIUtils.TEXT_COLOR);
                }
                label.setBorder(new EmptyBorder(12, 20, 12, 20));
                return label;
            }
        });
        
        navList.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                int index = navList.getSelectedIndex();
                if (index >= 0) {
                    showPage(index + 1); // +1 因为欢迎页是第0个
                }
            }
        });
        
        // 底部版权
        JLabel footerLabel = new JLabel("© 2025 Summer", SwingConstants.CENTER);
        footerLabel.setFont(new Font("SansSerif", Font.PLAIN, 11));
        footerLabel.setForeground(new Color(0x90, 0x90, 0x90));
        footerLabel.setBorder(new EmptyBorder(10, 0, 10, 0));
        
        JPanel footerPanel = new JPanel(new BorderLayout());
        footerPanel.add(footerLabel, BorderLayout.SOUTH);
        footerPanel.setBackground(UIUtils.LIGHT_GRAY);
        
        JPanel topPanel = new JPanel(new BorderLayout());
        topPanel.add(titleLabel, BorderLayout.NORTH);
        topPanel.add(navList, BorderLayout.CENTER);
        topPanel.setBackground(UIUtils.LIGHT_GRAY);
        
        navPanel.add(topPanel, BorderLayout.CENTER);
        navPanel.add(footerPanel, BorderLayout.SOUTH);
        
        return navPanel;
    }
    
    private void showPage(int index) {
        if (index >= 0 && index < pages.size()) {
            contentPanel.removeAll();
            contentPanel.add(pages.get(index).getPanel(), BorderLayout.CENTER);
            contentPanel.revalidate();
            contentPanel.repaint();
        }
    }
}
