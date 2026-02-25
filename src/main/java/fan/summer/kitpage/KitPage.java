package fan.summer.kitpage;

import javax.swing.*;
import java.awt.*;

public interface KitPage {
    /**
     * 获取页面面板
     */
    JPanel getPanel();

    /**
     * 获取页面标题
     */
    String getTitle();

    /**
     * 获取菜单显示名称
     */
    default String getMenuName() {
        return getTitle();
    }

    /**
     * 获取菜单图标（可返回 null）
     */
    default Icon getMenuIcon() {
        return null;
    }

    /**
     * 获取菜单提示文本
     */
    default String getMenuTooltip() {
        return null;
    }
}
