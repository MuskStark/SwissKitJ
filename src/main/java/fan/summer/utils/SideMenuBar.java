package fan.summer.utils;

import fan.summer.kitpage.KitPage;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.List;

/**
 * 侧边菜单栏组件
 * 根据 KitPage 动态生成菜单
 */
public class SideMenuBar extends JPanel {

    private static final int MENU_WIDTH = 220;
    private static final Color SELECTED_BG = new Color(0x2D, 0x2D, 0x2D);
    private static final Color SELECTED_TEXT = new Color(0xBB, 0x86, 0xFC);
    private static final Color HOVER_BG = new Color(0xE8, 0xE8, 0xE8);

    private final List<KitPage> pages;
    private final List<JLabel> menuItems;
    private final JPanel contentPanel;
    private int selectedIndex;

    private final JLabel titleLabel;
    private final JPanel menuContainer;

    /**
     * 创建侧边菜单栏
     *
     * @param pages        页面列表
     * @param contentPanel 内容面板（用于切换页面）
     */
    public SideMenuBar(List<KitPage> pages, JPanel contentPanel) {
        this.pages = pages;
        this.contentPanel = contentPanel;
        this.menuItems = new ArrayList<>();
        this.selectedIndex = -1;

        setLayout(new BorderLayout());
        setPreferredSize(new Dimension(MENU_WIDTH, 0));
        setBackground(UIUtils.LIGHT_GRAY);

        // 标题
        titleLabel = new JLabel("Swiss Kit", SwingConstants.LEFT);
        titleLabel.setFont(new Font("SansSerif", Font.BOLD, 18));
        titleLabel.setForeground(UIUtils.TEXT_COLOR);
        titleLabel.setBorder(new EmptyBorder(20, 20, 20, 20));

        JPanel titlePanel = new JPanel(new BorderLayout());
        titlePanel.add(titleLabel, BorderLayout.NORTH);
        titlePanel.setBackground(UIUtils.LIGHT_GRAY);

        // 菜单容器
        menuContainer = new JPanel();
        menuContainer.setLayout(new BoxLayout(menuContainer, BoxLayout.Y_AXIS));
        menuContainer.setBackground(UIUtils.LIGHT_GRAY);

        // 构建菜单项
        rebuildMenu();

        // 底部版权
        JLabel footerLabel = new JLabel("© 2025 Summer", SwingConstants.CENTER);
        footerLabel.setFont(new Font("SansSerif", Font.PLAIN, 11));
        footerLabel.setForeground(new Color(0x90, 0x90, 0x90));
        footerLabel.setBorder(new EmptyBorder(10, 0, 10, 0));

        JPanel footerPanel = new JPanel(new BorderLayout());
        footerPanel.add(footerLabel, BorderLayout.SOUTH);
        footerPanel.setBackground(UIUtils.LIGHT_GRAY);

        // 组装
        add(titlePanel, BorderLayout.NORTH);
        add(menuContainer, BorderLayout.CENTER);
        add(footerPanel, BorderLayout.SOUTH);
    }

    /**
     * 重建菜单（用于动态更新菜单）
     */
    public void rebuildMenu() {
        menuContainer.removeAll();
        menuItems.clear();

        for (int i = 0; i < pages.size(); i++) {
            KitPage page = pages.get(i);
            JLabel menuItem = createMenuItem(page, i);
            menuItems.add(menuItem);
            menuContainer.add(menuItem);
        }

        menuContainer.revalidate();
        menuContainer.repaint();
    }

    /**
     * 创建菜单项
     */
    private JLabel createMenuItem(KitPage page, int index) {
        String menuName = page.getMenuName();
        Icon menuIcon = page.getMenuIcon();
        String tooltip = page.getMenuTooltip();

        JLabel label = new JLabel(menuName, menuIcon, SwingConstants.LEFT);
        label.setFont(new Font("SansSerif", Font.PLAIN, 13));
        label.setForeground(UIUtils.TEXT_COLOR);
        label.setBackground(UIUtils.LIGHT_GRAY);
        label.setOpaque(true);
        label.setBorder(new EmptyBorder(12, 20, 12, 20));
        label.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        label.setFocusable(true);

        if (tooltip != null) {
            label.setToolTipText(tooltip);
        }

        // 点击事件
        label.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                selectPage(index);
            }

            @Override
            public void mouseEntered(MouseEvent e) {
                if (index != selectedIndex) {
                    label.setBackground(HOVER_BG);
                }
            }

            @Override
            public void mouseExited(MouseEvent e) {
                if (index != selectedIndex) {
                    label.setBackground(UIUtils.LIGHT_GRAY);
                }
            }
        });

        return label;
    }

    /**
     * 选择页面
     */
    public void selectPage(int index) {
        if (index < 0 || index >= pages.size()) {
            return;
        }

        // 更新选中状态
        int previousIndex = selectedIndex;
        selectedIndex = index;

        // 更新菜单项样式
        if (previousIndex >= 0 && previousIndex < menuItems.size()) {
            JLabel prevLabel = menuItems.get(previousIndex);
            prevLabel.setBackground(UIUtils.LIGHT_GRAY);
            prevLabel.setForeground(UIUtils.TEXT_COLOR);
        }

        JLabel selectedLabel = menuItems.get(index);
        selectedLabel.setBackground(SELECTED_BG);
        selectedLabel.setForeground(SELECTED_TEXT);

        // 切换内容面板
        if (contentPanel != null) {
            contentPanel.removeAll();
            contentPanel.add(pages.get(index).getPanel(), BorderLayout.CENTER);
            contentPanel.revalidate();
            contentPanel.repaint();
        }
    }

    /**
     * 获取当前选中的页面索引
     */
    public int getSelectedIndex() {
        return selectedIndex;
    }

    /**
     * 设置标题
     */
    public void setTitle(String title) {
        titleLabel.setText(title);
    }

    /**
     * 添加页面
     */
    public void addPage(KitPage page) {
        pages.add(page);
        JLabel menuItem = createMenuItem(page, pages.size() - 1);
        menuItems.add(menuItem);
        menuContainer.add(menuItem);
        menuContainer.revalidate();
        menuContainer.repaint();
    }

    /**
     * 移除页面
     */
    public void removePage(int index) {
        if (index >= 0 && index < pages.size()) {
            pages.remove(index);
            menuItems.get(index).setVisible(false);
            menuContainer.remove(index);
            menuItems.remove(index);
            menuContainer.revalidate();
            menuContainer.repaint();
        }
    }

    /**
     * 设置页面列表（会重建菜单）
     */
    public void setPages(List<KitPage> newPages) {
        pages.clear();
        pages.addAll(newPages);
        rebuildMenu();
    }
}
