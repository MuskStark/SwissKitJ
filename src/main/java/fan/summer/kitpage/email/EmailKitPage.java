package fan.summer.kitpage.email;

import fan.summer.annoattion.SwissKitPage;
import fan.summer.api.KitPage;
import net.miginfocom.swing.MigLayout;

import javax.swing.*;

/**
 * Email Tool Page
 * Provides email sending functionality
 *
 * @author summer
 * @version 1.00
 * @date 2026/2/26
 */
@SwissKitPage(menuName = "Email", menuTooltip = "Email", order = 2, visible = false)
public class EmailKitPage implements KitPage {

    public EmailKitPage() {
        initComponents();
    }

    @Override
    public JPanel getPanel() {
        return emailPanel;
    }

    private void initComponents() {
        // JFormDesigner - Component initialization - DO NOT MODIFY  //GEN-BEGIN:initComponents  @formatter:off
        emailPanel = new JPanel();
        emailTitle = new JLabel();
        subject = new JTextField();
        to = new JLabel();
        toText = new JTextField();
        body = new JTextArea();
        sentButton = new JButton();

        //======== emailPanel ========
        {
            emailPanel.setLayout(new MigLayout(
                "insets 0,hidemode 3,gap 10 5",
                // columns
                "[fill]" +
                "[grow,fill]" +
                "[grow,fill]",
                // rows
                "[fill]" +
                "[fill]" +
                "[grow,fill]" +
                "[]" +
                "[fill]"));

            //---- emailTitle ----
            emailTitle.setText("Subject:");
            emailPanel.add(emailTitle, "cell 0 0,align left center,grow 0 0");
            emailPanel.add(subject, "cell 1 0 2 1,aligny center,grow 100 0");

            //---- to ----
            to.setText("To:");
            emailPanel.add(to, "cell 0 1,align left center,grow 0 0");
            emailPanel.add(toText, "cell 1 1 2 1,aligny center,grow 100 0");

            //---- body ----
            body.setLineWrap(false);
            body.setText("");
            emailPanel.add(body, "cell 0 2 3 1,grow");

            //---- sentButton ----
            sentButton.setText("Sent");
            emailPanel.add(sentButton, "cell 0 3 3 1");
        }
        // JFormDesigner - End of component initialization  //GEN-END:initComponents  @formatter:on
    }

    // JFormDesigner - Variables declaration - DO NOT MODIFY  //GEN-BEGIN:variables  @formatter:off
    private JPanel emailPanel;
    private JLabel emailTitle;
    private JTextField subject;
    private JLabel to;
    private JTextField toText;
    private JTextArea body;
    private JButton sentButton;
    // JFormDesigner - End of variables declaration  //GEN-END:variables  @formatter:on
}
