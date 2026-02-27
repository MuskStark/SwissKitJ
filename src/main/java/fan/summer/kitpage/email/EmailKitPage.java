package fan.summer.kitpage.email;

import fan.summer.kitpage.KitPage;

import javax.swing.*;

/**
 * Email Tool Page
 * Provides email sending functionality
 *
 * @author summer
 * @version 1.00
 * @Date 2026/2/26
 */
public class EmailKitPage implements KitPage {
    private JPanel emailPanel;
    private JTextField subject;
    private JLabel emailTitle;
    private JTextField toText;
    private JLabel to;
    private JTextArea body;
    private JButton sentButton;

    public EmailKitPage() {
        sentButton.addActionListener(e -> {
            // TODO: implement email sending functionality
        });
    }

    @Override
    public JPanel getPanel() {
        return emailPanel;
    }

    @Override
    public String getTitle() {
        return "Email";
    }
}
