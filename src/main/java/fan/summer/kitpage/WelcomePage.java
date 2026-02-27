package fan.summer.kitpage;


import javax.swing.*;
import java.awt.*;

/**
 * Toolbox welcome page
 */
public class WelcomePage implements KitPage {
    
    private JPanel panel;
    
    public WelcomePage() {
        initComponents();
    }
    
    private void initComponents() {
        panel = new JPanel(new GridBagLayout());
        panel.setBackground(Color.WHITE);
        
        JLabel welcomeLabel = new JLabel("欢迎使用 Swiss Kit 工具箱");
        welcomeLabel.setFont(new Font("SansSerif", Font.BOLD, 24));
        welcomeLabel.setForeground(new Color(0x60, 0x60, 0x60));
        
        JLabel descLabel = new JLabel("请从左侧选择需要的工具");
        descLabel.setFont(new Font("SansSerif", Font.PLAIN, 14));
        descLabel.setForeground(new Color(0x90, 0x90, 0x90));
        
        panel.add(welcomeLabel, new GridBagConstraints(0, 0, 1, 1, 0, 0, GridBagConstraints.CENTER, GridBagConstraints.NONE, new Insets(0, 0, 20, 0), 0, 0));
        panel.add(descLabel, new GridBagConstraints(0, 1, 1, 1, 0, 0, GridBagConstraints.CENTER, GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 0, 0));
    }
    
    @Override
    public JPanel getPanel() {
        return panel;
    }
    
    @Override
    public String getTitle() {
        return "Swiss Kit";
    }
    
    @Override
    public String getMenuName() {
        return "🏠 Welcome";
    }
    
    @Override
    public Icon getMenuIcon() {
        return null;
    }
    
    @Override
    public String getMenuTooltip() {
        return "Return to welcome page";
    }
}
