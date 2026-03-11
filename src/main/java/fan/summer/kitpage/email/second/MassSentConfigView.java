/*
 * Created by JFormDesigner on Wed Mar 11 11:06:02 CST 2026
 */

package fan.summer.kitpage.email.second;

import fan.summer.database.DatabaseInit;
import fan.summer.database.entity.email.EmailMassSentConfigEntity;
import fan.summer.database.entity.setting.email.EmailTagEntity;
import fan.summer.database.mapper.email.EmailMassSentConfigMapper;
import fan.summer.database.mapper.setting.email.EmailTagMapper;
import net.miginfocom.swing.MigLayout;
import org.apache.ibatis.session.SqlSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ItemEvent;
import java.io.File;
import java.util.List;
import java.util.Objects;

/**
 * Mass sent config view dialog for configuring email mass sending settings.
 *
 * @author summer
 * @version 1.00
 * @date 2026/3/11
 */
public class MassSentConfigView extends JDialog {
    private static final Logger log = LoggerFactory.getLogger(MassSentConfigView.class);

    private final String taskId;

    public MassSentConfigView(JPanel panel, String taskId) {
        super(SwingUtilities.getWindowAncestor(panel));
        this.taskId = taskId;
        initComponents();
        loadTags();
        loadConfig();
    }

    /**
     * Validates the form inputs.
     * Returns true if all required fields are filled.
     *
     * @return true if validation passes, false otherwise
     */
    private boolean validateForm() {
        if (toTagcomboBox.getSelectedItem() == null || toTagcomboBox.getSelectedItem().toString().trim().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please select a To tag", "Validation Error", JOptionPane.ERROR_MESSAGE);
            return false;
        }

        if (attachmentByTagCheckBox.isSelected()) {
            String attPath = attPathField.getText();
            if (attPath == null || attPath.trim().isEmpty()) {
                JOptionPane.showMessageDialog(this, "Please select an attachment folder", "Validation Error", JOptionPane.ERROR_MESSAGE);
                return false;
            }
        }

        return true;
    }

    private void saveBtAction(ActionEvent e) {
        if (!validateForm()) {
            return;
        }

        new SwingWorker<Void, Void>() {
            @Override
            protected Void doInBackground() throws Exception {
                try (SqlSession session = DatabaseInit.getSqlSession()) {
                    EmailMassSentConfigMapper mapper = session.getMapper(EmailMassSentConfigMapper.class);

                    EmailMassSentConfigEntity config = new EmailMassSentConfigEntity();
                    config.setTaskId(taskId);
                    config.setToTag(Objects.requireNonNull(toTagcomboBox.getSelectedItem()).toString());
                    config.setCcTag(ccTagComboBox.getSelectedItem() != null ? ccTagComboBox.getSelectedItem().toString() : null);
                    config.setSentAtt(attachmentByTagCheckBox.isSelected());
                    config.setAttFolderPath(attachmentByTagCheckBox.isSelected() ? attPathField.getText() : null);

                    mapper.upsert(config);
                    session.commit();
                    log.info("Mass sent config saved for taskId: {}", taskId);
                }

                return null;
            }

            @Override
            protected void done() {
                JOptionPane.showMessageDialog(MassSentConfigView.this, "Configuration saved successfully", "Success", JOptionPane.INFORMATION_MESSAGE);
                dispose();
            }
        }.execute();
    }

    private void closedBtAction(ActionEvent e) {
        dispose();
    }

    private void attachmentByTagCheckBoxItemStateChanged(ItemEvent e) {
        choiceAttFolderBt.setEnabled(attachmentByTagCheckBox.isSelected());
        if (!attachmentByTagCheckBox.isSelected()) {
            attPathField.setText("");
        }
    }

    private void choiceAttFolderBtAction(ActionEvent e) {
        JFileChooser folderChooser = new JFileChooser();
        folderChooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
        folderChooser.setDialogTitle("Select Attachment Folder");

        int result = folderChooser.showOpenDialog(this);
        if (result == JFileChooser.APPROVE_OPTION) {
            File selectedFolder = folderChooser.getSelectedFile();
            attPathField.setText(selectedFolder.getAbsolutePath());
            log.debug("Attachment folder selected: {}", selectedFolder.getAbsolutePath());
        }
    }

    /**
     * Loads tags from database into combo boxes.
     */
    private void loadTags() {
        try (SqlSession session = DatabaseInit.getSqlSession()) {
            EmailTagMapper tagMapper = session.getMapper(EmailTagMapper.class);
            List<EmailTagEntity> tags = tagMapper.selectAll();

            toTagcomboBox.removeAllItems();
            ccTagComboBox.removeAllItems();

            for (EmailTagEntity tag : tags) {
                toTagcomboBox.addItem(tag.getTag());
                ccTagComboBox.addItem(tag.getTag());
            }
        } catch (Exception ex) {
            log.error("Failed to load tags", ex);
            JOptionPane.showMessageDialog(this, "Failed to load tags: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    /**
     * Loads existing config for the current task.
     */
    private void loadConfig() {
        try (SqlSession session = DatabaseInit.getSqlSession()) {
            EmailMassSentConfigMapper mapper = session.getMapper(EmailMassSentConfigMapper.class);
            EmailMassSentConfigEntity config = mapper.selectByTaskId(taskId);

            if (config != null) {
                toTagcomboBox.setSelectedItem(config.getToTag());
                ccTagComboBox.setSelectedItem(config.getCcTag());
                attachmentByTagCheckBox.setSelected(config.isSentAtt());
                attPathField.setText(config.getAttFolderPath() != null ? config.getAttFolderPath() : "");
            }
        } catch (Exception ex) {
            log.error("Failed to load config for taskId: {}", taskId, ex);
        }
    }

    private void initComponents() {
        // JFormDesigner - Component initialization - DO NOT MODIFY  //GEN-BEGIN:initComponents  @formatter:off
        label1 = new JLabel();
        toTagcomboBox = new JComboBox();
        label2 = new JLabel();
        ccTagComboBox = new JComboBox();
        attachmentByTagCheckBox = new JCheckBox();
        choiceAttFolderBt = new JButton();
        attPathField = new JTextField();
        panel1 = new JPanel();
        saveBt = new JButton();
        closedBt = new JButton();

        //======== this ========
        var contentPane = getContentPane();
        contentPane.setLayout(new MigLayout(
            "hidemode 3",
            // columns
            "[fill]" +
            "[42,fill]" +
            "[90,fill]" +
            "[287,fill]",
            // rows
            "[]" +
            "[]" +
            "[]" +
            "[]" +
            "[]"));

        //---- label1 ----
        label1.setText("To");
        contentPane.add(label1, "cell 1 0");
        contentPane.add(toTagcomboBox, "cell 2 0 2 1");

        //---- label2 ----
        label2.setText("Cc");
        contentPane.add(label2, "cell 1 1");
        contentPane.add(ccTagComboBox, "cell 2 1 2 1");

        //---- attachmentByTagCheckBox ----
        attachmentByTagCheckBox.setText("AddAttachmentByTag");
        attachmentByTagCheckBox.addItemListener(e -> attachmentByTagCheckBoxItemStateChanged(e));
        contentPane.add(attachmentByTagCheckBox, "cell 1 2 2 1");

        //---- choiceAttFolderBt ----
        choiceAttFolderBt.setText("ChoiceAttachmentFolder");
        choiceAttFolderBt.setEnabled(false);
        choiceAttFolderBt.addActionListener(e -> choiceAttFolderBtAction(e));
        contentPane.add(choiceAttFolderBt, "cell 1 3 2 1");

        //---- attPathField ----
        attPathField.setEditable(false);
        contentPane.add(attPathField, "cell 3 3");

        //======== panel1 ========
        {
            panel1.setLayout(new MigLayout(
                "fillx,hidemode 3,align center center",
                // columns
                "[492,fill]",
                // rows
                "[]" +
                "[]" +
                "[]"));

            //---- saveBt ----
            saveBt.setText("Save");
            saveBt.addActionListener(e -> saveBtAction(e));
            panel1.add(saveBt, "cell 0 0");

            //---- closedBt ----
            closedBt.setText("Closed");
            closedBt.addActionListener(e -> closedBtAction(e));
            panel1.add(closedBt, "cell 0 1");
        }
        contentPane.add(panel1, "cell 0 4 4 1");
        pack();
        setLocationRelativeTo(getOwner());
        // JFormDesigner - End of component initialization  //GEN-END:initComponents  @formatter:on
    }

    // JFormDesigner - Variables declaration - DO NOT MODIFY  //GEN-BEGIN:variables  @formatter:off
    private JLabel label1;
    private JComboBox toTagcomboBox;
    private JLabel label2;
    private JComboBox ccTagComboBox;
    private JCheckBox attachmentByTagCheckBox;
    private JButton choiceAttFolderBt;
    private JTextField attPathField;
    private JPanel panel1;
    private JButton saveBt;
    private JButton closedBt;
    // JFormDesigner - End of variables declaration  //GEN-END:variables  @formatter:on
}
