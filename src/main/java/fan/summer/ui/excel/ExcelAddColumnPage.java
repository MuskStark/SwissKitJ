package fan.summer.ui.excel;

import fan.summer.ui.ToolPage;
import fan.summer.ui.common.UIUtils;

import javax.swing.*;
import java.awt.*;

/**
 * Excel增加英文列工具页面
 */
public class ExcelAddColumnPage implements ToolPage {
    
    private JPanel panel;
    private JTextField fileField;
    private JTextField folderField;
    private JCheckBox openFolderCheck;
    private JProgressBar progressBar;
    private JLabel statusLabel;
    
    public ExcelAddColumnPage() {
        initComponents();
    }
    
    private void initComponents() {
        panel = new JPanel(new BorderLayout(0, 20));
        panel.setBackground(Color.WHITE);
        
        // 标题
        JLabel title = UIUtils.createPageTitle("Excel增加英文列工具");
        
        // 文件选择
        fileField = new JTextField();
        JButton fileBtn = new JButton("选择文件");
        JPanel filePanel = UIUtils.createFilePickerPanel("选择Excel文件", fileField, fileBtn);
        
        // 输出文件夹
        folderField = new JTextField();
        JButton folderBtn = new JButton("选择输出文件夹");
        JPanel folderPanel = UIUtils.createFolderPickerPanel("选择输出文件夹", folderField, folderBtn);
        
        // 选项
        openFolderCheck = new JCheckBox("处理完成后打开输出文件夹", true);
        
        // 按钮
        JButton processBtn = new JButton("开始处理");
        
        // 进度
        progressBar = UIUtils.createProgressBar();
        statusLabel = new JLabel("");
        
        // 组装面板
        JPanel centerPanel = new JPanel();
        centerPanel.setLayout(new BoxLayout(centerPanel, BoxLayout.Y_AXIS));
        centerPanel.setBackground(Color.WHITE);
        
        centerPanel.add(UIUtils.createSectionPanel("1. 选择Excel文件", filePanel));
        centerPanel.add(UIUtils.createVerticalStrut(15));
        centerPanel.add(UIUtils.createSectionPanel("2. 选择输出文件夹", folderPanel));
        centerPanel.add(UIUtils.createVerticalStrut(15));
        centerPanel.add(openFolderCheck);
        centerPanel.add(UIUtils.createVerticalStrut(20));
        centerPanel.add(UIUtils.createCenterButtonPanel(processBtn));
        centerPanel.add(UIUtils.createVerticalStrut(15));
        centerPanel.add(progressBar);
        centerPanel.add(statusLabel);
        
        panel.add(title, BorderLayout.NORTH);
        panel.add(centerPanel, BorderLayout.CENTER);
    }
    
    @Override
    public JPanel getPanel() {
        return panel;
    }
    
    @Override
    public String getTitle() {
        return "Excel增加英文列工具";
    }
}
