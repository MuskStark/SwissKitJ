package fan.summer.ui;

import javax.swing.*;

/**
 * 工具页面接口
 */
public interface ToolPage {
    
    /**
     * 获取页面面板
     */
    JPanel getPanel();
    
    /**
     * 获取页面标题
     */
    String getTitle();
}
