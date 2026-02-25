package fan.summer.ui.email;

import fan.summer.ui.ToolPage;
import fan.summer.ui.common.UIUtils;

import javax.swing.*;
import java.awt.*;

/**
 * 邮件批量发送工具页面
 */
public class EmailPage implements ToolPage {
    
    private JPanel panel;
    private JTextField smtpField;
    private JTextField portField;
    private JTextField senderField;
    private JTextField attachmentField;
    private JTextField templateField;
    private JTextArea logArea;
    private JProgressBar progressBar;
    private JLabel statusLabel;
    
    public EmailPage() {
        initComponents();
    }
    
    private void initComponents() {
        panel = new JPanel(new BorderLayout(0, 20));
        panel.setBackground(Color.WHITE);
        
        // 标题
        JLabel title = UIUtils.createPageTitle("邮件批量发送");
        
        // 配置文件
        JPanel configPanel = new JPanel(new GridLayout(3, 2, 10, 10));
        configPanel.setBorder(BorderFactory.createTitledBorder("邮件配置"));
        configPanel.setBackground(Color.WHITE);
        
        smtpField = new JTextField();
        portField = new JTextField("465");
        senderField = new JTextField();
        
        configPanel.add(new JLabel("SMTP服务器:"));
        configPanel.add(smtpField);
        configPanel.add(new JLabel("端口:"));
        configPanel.add(portField);
        configPanel.add(new JLabel("发件人:"));
        configPanel.add(senderField);
        
        // 附件选择
        attachmentField = new JTextField();
        JButton attachmentBtn = new JButton("添加附件");
        JPanel attachmentPanel = UIUtils.createFilePickerPanel("添加附件", attachmentField, attachmentBtn);
        
        // 模板选择
        templateField = new JTextField();
        JButton templateBtn = new JButton("选择邮件模板");
        JPanel templatePanel = UIUtils.createFilePickerPanel("选择邮件模板", templateField, templateBtn);
        
        // 按钮
        JButton sendBtn = new JButton("开始发送");
        
        // 进度
        progressBar = UIUtils.createProgressBar();
        statusLabel = new JLabel("");
        
        // 日志显示
        logArea = new JTextArea(10, 50);
        logArea.setFont(new Font("Monospaced", Font.PLAIN, 11));
        logArea.setEditable(false);
        JScrollPane logScroll = UIUtils.createScrollableTextArea(logArea, 12, 50);
        
        // 组装面板
        JPanel centerPanel = new JPanel();
        centerPanel.setLayout(new BoxLayout(centerPanel, BoxLayout.Y_AXIS));
        centerPanel.setBackground(Color.WHITE);
        
        centerPanel.add(configPanel);
        centerPanel.add(UIUtils.createVerticalStrut(15));
        centerPanel.add(UIUtils.createSectionPanel("1. 添加附件", attachmentPanel));
        centerPanel.add(UIUtils.createVerticalStrut(15));
        centerPanel.add(UIUtils.createSectionPanel("2. 选择邮件模板", templatePanel));
        centerPanel.add(UIUtils.createVerticalStrut(20));
        centerPanel.add(UIUtils.createCenterButtonPanel(sendBtn));
        centerPanel.add(UIUtils.createVerticalStrut(15));
        centerPanel.add(progressBar);
        centerPanel.add(statusLabel);
        centerPanel.add(UIUtils.createVerticalStrut(10));
        
        // 日志区域
        JPanel logPanel = new JPanel(new BorderLayout());
        logPanel.add(new JLabel("发送日志:"), BorderLayout.NORTH);
        logPanel.add(logScroll, BorderLayout.CENTER);
        logPanel.setBackground(Color.WHITE);
        centerPanel.add(logPanel);
        
        panel.add(title, BorderLayout.NORTH);
        panel.add(centerPanel, BorderLayout.CENTER);
    }
    
    @Override
    public JPanel getPanel() {
        return panel;
    }
    
    @Override
    public String getTitle() {
        return "邮件批量发送";
    }
}
