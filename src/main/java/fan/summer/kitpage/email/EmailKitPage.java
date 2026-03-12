package fan.summer.kitpage.email;

import fan.summer.annoattion.SwissKitPage;
import fan.summer.api.KitPage;
import fan.summer.database.DatabaseInit;
import fan.summer.database.entity.email.EmailMassSentConfigEntity;
import fan.summer.database.mapper.email.EmailMassSentConfigMapper;
import fan.summer.kitpage.email.second.MassSentConfigView;
import fan.summer.kitpage.email.worker.EmailSentWorker;
import net.miginfocom.swing.MigLayout;
import org.apache.ibatis.session.SqlSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.util.UUID;

/**
 * Email Tool Page
 * Provides email sending functionality with support for single and mass email sending.
 *
 * @author summer
 * @version 1.00
 * @date 2026/2/26
 */
@SwissKitPage(menuName = "Email", menuTooltip = "Email", order = 2)
public class EmailKitPage implements KitPage {
    private static final Logger log = LoggerFactory.getLogger(EmailKitPage.class);
    private String taskId;

    public EmailKitPage() {
        initComponents();
    }

    /**
     * Returns the main panel for this email page.
     *
     * @return the email JPanel
     */
    @Override
    public JPanel getPanel() {
        return emailPanel;
    }

    /**
     * Handles the mass send checkbox state change.
     * Enables or disables mass send configuration buttons and text fields.
     * When enabled, To and Cc fields are disabled as recipients are loaded from configuration.
     *
     * @param e the action event
     */
    private void massSentCheckBoxActionListener(ActionEvent e) {
        if (massSentCheckBox.isSelected()) {
            taskId = "MassTask-" + UUID.randomUUID();
            log.debug("Mass send mode enabled, taskId:{}", taskId);
            setMassSentConfigBt.setEnabled(true);
            viewSentConfigBt.setEnabled(true);
            toText.setText("");
            toText.setEnabled(false);
            ccText.setText("");
            ccText.setEnabled(false);
        } else {
            log.debug("Mass send mode disabled");
            taskId = null;
            toText.setText("");
            toText.setEnabled(true);
            ccText.setText("");
            ccText.setEnabled(true);
            setMassSentConfigBt.setEnabled(false);
            viewSentConfigBt.setEnabled(false);
        }
    }

    private void setMassSentConfigBtAction(ActionEvent e) {
        if (taskId != null) {
            new MassSentConfigView(emailPanel, taskId).setVisible(true);
        }
    }

    private void viewSentConfigBtAction(ActionEvent e) {
        if (taskId == null) {
            return;
        }

        new SwingWorker<EmailMassSentConfigEntity, Void>() {
            @Override
            protected EmailMassSentConfigEntity doInBackground() throws Exception {
                try (SqlSession session = DatabaseInit.getSqlSession()) {
                    EmailMassSentConfigMapper mapper = session.getMapper(EmailMassSentConfigMapper.class);
                    return mapper.selectByTaskId(taskId);
                }
            }

            @Override
            protected void done() {
                try {
                    EmailMassSentConfigEntity config = get();
                    StringBuilder message = new StringBuilder();
                    message.append("<html><body>");
                    message.append("<p><b>Task ID:</b> ").append(taskId).append("</p>");
                    message.append("<p><b>To Tag:</b> ").append(config != null && config.getToTag() != null ? config.getToTag() : "N/A").append("</p>");
                    message.append("<p><b>Cc Tag:</b> ").append(config != null && config.getCcTag() != null ? config.getCcTag() : "N/A").append("</p>");
                    message.append("<p><b>Send Attachment:</b> ").append(config != null && config.isSentAtt() ? "Yes" : "No").append("</p>");
                    message.append("<p><b>Attachment Folder:</b> ").append(config != null && config.getAttFolderPath() != null ? config.getAttFolderPath() : "N/A").append("</p>");
                    message.append("</body></html>");

                    if (config == null) {
                        JOptionPane.showMessageDialog(emailPanel, "No configuration found for this task", "Info", JOptionPane.INFORMATION_MESSAGE);
                    } else {
                        JOptionPane.showMessageDialog(emailPanel, message.toString(), "Mass Sent Configuration", JOptionPane.INFORMATION_MESSAGE);
                    }
                } catch (Exception ex) {
                    log.error("Failed to load config for taskId: {}", taskId, ex);
                    JOptionPane.showMessageDialog(emailPanel, "Failed to load config: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        }.execute();
    }

    private void sentBtAction(ActionEvent e) {
        new EmailSentWorker(subject.getText(), body.getText(), taskId, massSentCheckBox.isSelected()).execute();
    }

    private void initComponents() {
        // JFormDesigner - Component initialization - DO NOT MODIFY  //GEN-BEGIN:initComponents  @formatter:off
        emailPanel = new JPanel();
        emailTitle = new JLabel();
        subject = new JTextField();
        to = new JLabel();
        toText = new JTextField();
        label1 = new JLabel();
        ccText = new JTextField();
        body = new JTextArea();
        massSentCheckBox = new JCheckBox();
        setMassSentConfigBt = new JButton();
        viewSentConfigBt = new JButton();
        sentButton = new JButton();
        progressBar1 = new JProgressBar();

        //======== emailPanel ========
        {
            emailPanel.setLayout(new MigLayout(
                "insets 0,hidemode 3,gap 10 5",
                // columns
                "[fill]" +
                "[grow,fill]" +
                "[grow,fill]" +
                "[fill]",
                // rows
                "[fill]" +
                "[fill]" +
                "[]" +
                "[grow,fill]" +
                "[]" +
                "[]" +
                "[]" +
                "[fill]"));

            //---- emailTitle ----
            emailTitle.setText("Subject:");
            emailPanel.add(emailTitle, "cell 0 0,align left center,grow 0 0");
            emailPanel.add(subject, "cell 1 0 3 1,aligny center,grow 100 0");

            //---- to ----
            to.setText("To:");
            emailPanel.add(to, "cell 0 1,align left center,grow 0 0");
            emailPanel.add(toText, "cell 1 1 3 1,aligny center,grow 100 0");

            //---- label1 ----
            label1.setText("Cc:");
            emailPanel.add(label1, "cell 0 2");
            emailPanel.add(ccText, "cell 1 2 3 1");

            //---- body ----
            body.setLineWrap(false);
            body.setText("");
            emailPanel.add(body, "cell 0 3 4 1,grow");

            //---- massSentCheckBox ----
            massSentCheckBox.setText("MassSent");
            massSentCheckBox.addActionListener(e -> massSentCheckBoxActionListener(e));
            emailPanel.add(massSentCheckBox, "cell 0 4");

            //---- setMassSentConfigBt ----
            setMassSentConfigBt.setText("MassSendConfig");
            setMassSentConfigBt.setEnabled(false);
            setMassSentConfigBt.addActionListener(e -> setMassSentConfigBtAction(e));
            emailPanel.add(setMassSentConfigBt, "cell 1 4");

            //---- viewSentConfigBt ----
            viewSentConfigBt.setText("ViewSentConfig");
            viewSentConfigBt.setEnabled(false);
            viewSentConfigBt.addActionListener(e -> viewSentConfigBtAction(e));
            emailPanel.add(viewSentConfigBt, "cell 2 4");

            //---- sentButton ----
            sentButton.setText("Sent");
            sentButton.addActionListener(e -> sentBtAction(e));
            emailPanel.add(sentButton, "cell 0 5 4 1");
            emailPanel.add(progressBar1, "cell 0 6 4 1");
        }
        // JFormDesigner - End of component initialization  //GEN-END:initComponents  @formatter:on
    }

    // JFormDesigner - Variables declaration - DO NOT MODIFY  //GEN-BEGIN:variables  @formatter:off
    private JPanel emailPanel;
    private JLabel emailTitle;
    private JTextField subject;
    private JLabel to;
    private JTextField toText;
    private JLabel label1;
    private JTextField ccText;
    private JTextArea body;
    private JCheckBox massSentCheckBox;
    private JButton setMassSentConfigBt;
    private JButton viewSentConfigBt;
    private JButton sentButton;
    private JProgressBar progressBar1;
    // JFormDesigner - End of variables declaration  //GEN-END:variables  @formatter:on
}
