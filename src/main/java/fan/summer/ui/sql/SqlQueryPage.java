package fan.summer.ui.sql;

import fan.summer.ui.ToolPage;
import fan.summer.ui.common.UIUtils;

import javax.swing.*;
import java.awt.*;

/**
 * Sql待查询值生成工具页面
 */
public class SqlQueryPage implements ToolPage {
    
    private JPanel panel;
    private JTextField fileField;
    private JComboBox<String> sheetCombo;
    private JComboBox<String> columnCombo;
    private JTextArea resultArea;
    private JProgressBar progressBar;
    private JLabel statusLabel;
    
    public SqlQueryPage() {
        initComponents();
    }
    
    private void initComponents() {
        panel = new JPanel(new BorderLayout(0, 20));
        panel.setBackground(Color.WHITE);
        
        // 标题
        JLabel title = UIUtils.createPageTitle("Sql待查询值生成");
        
        // 文件选择
        fileField = new JTextField();
        JButton fileBtn = new JButton("选择文件");
        JPanel filePanel = UIUtils.createFilePickerPanel("选择数据源文件", fileField, fileBtn);
        
        // Sheet选择
        sheetCombo = new JComboBox<>(new String[]{"-- 请先解析文件 --"});
        sheetCombo.setPreferredSize(new Dimension(200, 30));
        
        // 列选择
        columnCombo = new JComboBox<>(new String[]{"-- 请先选择Sheet --"});
        columnCombo.setPreferredSize(new Dimension(300, 30));
        
        // 按钮
        JButton analyzeBtn = new JButton("文件分析");
        JButton generateBtn = new JButton("生成查询值");
        JButton copyBtn = new JButton("复制结果");
        
        // 结果显示
        resultArea = new JTextArea(8, 50);
        JScrollPane resultScroll = UIUtils.createScrollableTextArea(resultArea, 10, 50);
        
        // 进度
        progressBar = UIUtils.createProgressBar();
        statusLabel = new JLabel("");
        
        // 组装面板
        JPanel centerPanel = new JPanel();
        centerPanel.setLayout(new BoxLayout(centerPanel, BoxLayout.Y_AXIS));
        centerPanel.setBackground(Color.WHITE);
        
        centerPanel.add(UIUtils.createSectionPanel("1. 选择数据源文件(.xlsx)", filePanel));
        centerPanel.add(UIUtils.createVerticalStrut(15));
        
        // 选择区域
        JPanel selectPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        selectPanel.add(new JLabel("Sheet页:"));
        selectPanel.add(sheetCombo);
        selectPanel.add(UIUtils.createHorizontalStrut(20));
        selectPanel.add(new JLabel("数据列:"));
        selectPanel.add(columnCombo);
        selectPanel.add(analyzeBtn);
        centerPanel.add(selectPanel);
        
        centerPanel.add(UIUtils.createVerticalStrut(20));
        centerPanel.add(UIUtils.createCenterButtonPanel(generateBtn));
        centerPanel.add(UIUtils.createVerticalStrut(10));
        centerPanel.add(statusLabel);
        centerPanel.add(UIUtils.createVerticalStrut(10));
        
        // 结果区域
        JPanel resultPanel = new JPanel(new BorderLayout());
        resultPanel.add(new JLabel("生成结果:"), BorderLayout.NORTH);
        resultPanel.add(resultScroll, BorderLayout.CENTER);
        resultPanel.add(copyBtn, BorderLayout.SOUTH);
        resultPanel.setBackground(Color.WHITE);
        centerPanel.add(resultPanel);
        
        panel.add(title, BorderLayout.NORTH);
        panel.add(centerPanel, BorderLayout.CENTER);
    }
    
    @Override
    public JPanel getPanel() {
        return panel;
    }
    
    @Override
    public String getTitle() {
        return "Sql待查询值生成";
    }
}
