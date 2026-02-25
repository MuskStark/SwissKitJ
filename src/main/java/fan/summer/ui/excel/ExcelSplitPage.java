package fan.summer.ui.excel;

import fan.summer.ui.ToolPage;
import fan.summer.ui.common.UIUtils;

import javax.swing.*;
import java.awt.*;

/**
 * Excel文件拆分工具页面
 */
public class ExcelSplitPage implements ToolPage {
    
    private JPanel panel;
    private JTextField fileField;
    private JTextField folderField;
    private JCheckBox keepFormatCheck;
    private JCheckBox openFolderCheck;
    private JRadioButton bySheetRadio;
    private JRadioButton byColumnRadio;
    private JProgressBar progressBar;
    private JLabel statusLabel;
    
    public ExcelSplitPage() {
        initComponents();
    }
    
    private void initComponents() {
        panel = new JPanel(new BorderLayout(0, 20));
        panel.setBackground(Color.WHITE);
        
        // 标题
        JLabel title = UIUtils.createPageTitle("Excel文件拆分工具");
        
        // 文件选择
        fileField = new JTextField();
        JButton fileBtn = new JButton("选择文件");
        JPanel filePanel = UIUtils.createFilePickerPanel("选择Excel文件", fileField, fileBtn);
        
        // 输出文件夹
        folderField = new JTextField();
        JButton folderBtn = new JButton("选择输出文件夹");
        JPanel folderPanel = UIUtils.createFolderPickerPanel("选择输出文件夹", folderField, folderBtn);
        
        // 拆分选项
        JPanel splitOptionPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        splitOptionPanel.add(new JLabel("拆分方式:"));
        ButtonGroup splitGroup = new ButtonGroup();
        bySheetRadio = new JRadioButton("按Sheet拆分", true);
        byColumnRadio = new JRadioButton("按列拆分");
        splitGroup.add(bySheetRadio);
        splitGroup.add(byColumnRadio);
        splitOptionPanel.add(bySheetRadio);
        splitOptionPanel.add(byColumnRadio);
        
        // 选项复选框
        keepFormatCheck = new JCheckBox("保持原文件格式", false);
        openFolderCheck = new JCheckBox("处理完成后打开输出文件夹", true);
        
        // 按钮
        JButton analyzeBtn = new JButton("解析文件");
        
        // 进度
        progressBar = UIUtils.createProgressBar();
        statusLabel = new JLabel("");
        
        // 组装面板
        JPanel centerPanel = new JPanel();
        centerPanel.setLayout(new BoxLayout(centerPanel, BoxLayout.Y_AXIS));
        centerPanel.setBackground(Color.WHITE);
        
        centerPanel.add(UIUtils.createSectionPanel("1. 选择需拆分文件", filePanel));
        centerPanel.add(UIUtils.createVerticalStrut(15));
        centerPanel.add(UIUtils.createSectionPanel("2. 选择输出文件夹", folderPanel));
        centerPanel.add(UIUtils.createVerticalStrut(15));
        centerPanel.add(splitOptionPanel);
        centerPanel.add(UIUtils.createVerticalStrut(10));
        centerPanel.add(keepFormatCheck);
        centerPanel.add(openFolderCheck);
        centerPanel.add(UIUtils.createVerticalStrut(20));
        centerPanel.add(UIUtils.createCenterButtonPanel(analyzeBtn));
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
        return "Excel文件拆分工具";
    }
}
