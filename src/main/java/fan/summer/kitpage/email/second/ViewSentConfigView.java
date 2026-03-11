/*
 * Created by JFormDesigner on Wed Mar 11 11:06:02 CST 2026
 */

package fan.summer.kitpage.email.second;

import fan.summer.database.DatabaseInit;
import fan.summer.database.entity.email.EmailMassSentConfigEntity;
import fan.summer.database.mapper.email.EmailMassSentConfigMapper;
import net.miginfocom.swing.MigLayout;
import org.apache.ibatis.session.SqlSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.swing.*;
import java.awt.event.ActionEvent;

/**
 * View sent config dialog for displaying email mass sending configuration in read-only mode.
 *
 * @author summer
 * @version 1.00
 * @date 2026/3/11
 */
public class ViewSentConfigView extends JDialog {
    private static final Logger log = LoggerFactory.getLogger(ViewSentConfigView.class);

    private final String taskId;

    public ViewSentConfigView(JPanel panel, String taskId) {
        super(SwingUtilities.getWindowAncestor(panel));
        this.taskId = taskId;
        initComponents();
        loadConfig();
    }

    private void closedBtAction(ActionEvent e) {
        dispose();
    }

    /**
     * Loads and displays the config for the current task.
     */
    private void loadConfig() {
        try (SqlSession session = DatabaseInit.getSqlSession()) {
            EmailMassSentConfigMapper mapper = session.getMapper(EmailMassSentConfigMapper.class);
            EmailMassSentConfigEntity config = mapper.selectByTaskId(taskId);

            if (config != null) {
                taskIdValue.setText(config.getTaskId());
                toTagValue.setText(config.getToTag() != null ? config.getToTag() : "");
                ccTagValue.setText(config.getCcTag() != null ? config.getCcTag() : "");
                isSentAttValue.setText(config.isSentAtt() ? "Yes" : "No");
                attFolderValue.setText(config.getAttFolderPath() != null ? config.getAttFolderPath() : "");
            } else {
                JOptionPane.showMessageDialog(this, "No configuration found for this task", "Info", JOptionPane.INFORMATION_MESSAGE);
                taskIdValue.setText(taskId);
                toTagValue.setText("N/A");
                ccTagValue.setText("N/A");
                isSentAttValue.setText("N/A");
                attFolderValue.setText("N/A");
            }
        } catch (Exception ex) {
            log.error("Failed to load config for taskId: {}", taskId, ex);
            JOptionPane.showMessageDialog(this, "Failed to load config: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void initComponents() {
        // JFormDesigner - Component initialization - DO NOT MODIFY  //GEN-BEGIN:initComponents  @formatter:off
        JPanel contentPane = new JPanel();
        JLabel titleLabel = new JLabel();
        JLabel taskIdLabel = new JLabel();
        taskIdValue = new JLabel();
        JLabel toTagLabel = new JLabel();
        toTagValue = new JLabel();
        JLabel ccTagLabel = new JLabel();
        ccTagValue = new JLabel();
        JLabel isSentAttLabel = new JLabel();
        isSentAttValue = new JLabel();
        JLabel attFolderLabel = new JLabel();
        attFolderValue = new JLabel();
        JButton closedBt = new JButton();

        //======== contentPane ========
        {
            contentPane.setLayout(new MigLayout(
                "hidemode 3",
                // columns
                "[fill]" +
                "[grow,fill]",
                // rows
                "[]" +
                "[]" +
                "[]" +
                "[]" +
                "[]" +
                "[]"));

            //---- titleLabel ----
            titleLabel.setText("Mass Sent Configuration");
            titleLabel.setFont(titleLabel.getFont().deriveFont(titleLabel.getFont().getSize() + 2f));
            contentPane.add(titleLabel, "cell 0 0 2 1,align center center");

            //---- taskIdLabel ----
            taskIdLabel.setText("Task ID:");
            taskIdLabel.setFont(taskIdLabel.getFont().deriveFont(taskIdLabel.getFont().getStyle() | java.awt.Font.BOLD));
            contentPane.add(taskIdLabel, "cell 0 1,align right center");
            contentPane.add(taskIdValue, "cell 1 1,align left center");

            //---- toTagLabel ----
            toTagLabel.setText("To Tag:");
            toTagLabel.setFont(toTagLabel.getFont().deriveFont(toTagLabel.getFont().getStyle() | java.awt.Font.BOLD));
            contentPane.add(toTagLabel, "cell 0 2,align right center");
            contentPane.add(toTagValue, "cell 1 2,align left center");

            //---- ccTagLabel ----
            ccTagLabel.setText("Cc Tag:");
            ccTagLabel.setFont(ccTagLabel.getFont().deriveFont(ccTagLabel.getFont().getStyle() | java.awt.Font.BOLD));
            contentPane.add(ccTagLabel, "cell 0 3,align right center");
            contentPane.add(ccTagValue, "cell 1 3,align left center");

            //---- isSentAttLabel ----
            isSentAttLabel.setText("Send Attachment:");
            isSentAttLabel.setFont(isSentAttLabel.getFont().deriveFont(isSentAttLabel.getFont().getStyle() | java.awt.Font.BOLD));
            contentPane.add(isSentAttLabel, "cell 0 4,align right center");
            contentPane.add(isSentAttValue, "cell 1 4,align left center");

            //---- attFolderLabel ----
            attFolderLabel.setText("Attachment Folder:");
            attFolderLabel.setFont(attFolderLabel.getFont().deriveFont(attFolderLabel.getFont().getStyle() | java.awt.Font.BOLD));
            contentPane.add(attFolderLabel, "cell 0 5,align right center");
            contentPane.add(attFolderValue, "cell 1 5,align left center");
        }

        //======== this ========
        setContentPane(contentPane);
        setModal(true);
        setTitle("View Mass Sent Config");
        setSize(400, 280);
        setLocationRelativeTo(getOwner());

        //---- closedBt ----
        closedBt.setText("Closed");
        closedBt.addActionListener(e -> closedBtAction(e));
        contentPane.add(closedBt, "cell 0 6 2 1,align center center");

        // JFormDesigner - End of component initialization  @formatter:on
    }

    // JFormDesigner - Variables declaration - DO NOT MODIFY  @formatter:off
    private JLabel taskIdValue;
    private JLabel toTagValue;
    private JLabel ccTagValue;
    private JLabel isSentAttValue;
    private JLabel attFolderValue;
    // JFormDesigner - End of variables declaration  @formatter:on
}
