/*
 * Created by JFormDesigner on Thu Mar 05 23:18:59 CST 2026
 */

package fan.summer.kitpage.setting;

import fan.summer.annoattion.SwissKitPage;
import fan.summer.api.KitPage;
import fan.summer.database.entity.setting.email.EmailAddressBookEntity;
import fan.summer.kitpage.setting.second.EmailAddressBookView;
import fan.summer.kitpage.setting.worker.second.QueryAllEmailInfoCallBack;
import fan.summer.kitpage.setting.worker.second.QueryAllEmailInfoWorker;
import fan.summer.plugin.PluginLoader;
import net.miginfocom.swing.MigLayout;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import java.util.List;

/**
 * Settings page for application configuration.
 * Includes email server settings, address book management, and plugin installation.
 *
 * @author phoebej
 */
@SwissKitPage(menuName = "Setting", menuTooltip = "Setting", order = 99999)
public class SettingKitPage implements KitPage {
    private static final Logger log = LoggerFactory.getLogger(SettingKitPage.class);

    /** Cached email address book data */
    private List<EmailAddressBookEntity> dataBaseInfo;

    public SettingKitPage() {
        initComponents();
    }

    /**
     * Returns the main panel for this settings page.
     *
     * @return the settings JPanel
     */
    public JPanel getPanel() {
        return settingPanle;
    }

    /**
     * Opens the email address book view dialog.
     * Queries all email addresses from database and displays them in a table.
     */
    private void openAddressBookBtActionListener(ActionEvent e) {
        log.debug("Opening address book view");
        new QueryAllEmailInfoWorker(new QueryAllEmailInfoCallBack() {
            @Override
            public void onSuccess(List<EmailAddressBookEntity> emailAddressBookEntities) {
                dataBaseInfo = emailAddressBookEntities;
                log.debug("Loaded {} email addresses", emailAddressBookEntities.size());
                if (dataBaseInfo != null && !dataBaseInfo.isEmpty()) {
                    List<Object[]> rowData = new ArrayList<>();
                    for (EmailAddressBookEntity info : dataBaseInfo) {
                        rowData.add(new Object[]{info.getEmailAddress(), info.getNickname(), info.getTags()});
                    }
                    new EmailAddressBookView(settingPanle).initTable(rowData).setVisible(true);
                } else {
                    new EmailAddressBookView(settingPanle).setVisible(true);
                }
            }

            @Override
            public void onFailure(Exception e) {
                log.error("Failed to load email addresses", e);
                JOptionPane.showMessageDialog(settingTable,
                        "Can Not Find Any Email Address!",
                        "Warning",
                        JOptionPane.WARNING_MESSAGE);
            }
        }).execute();


    }

    /**
     * Handles the plugin file selection action.
     * Opens a file chooser dialog for selecting a JAR plugin file.
     *
     * @param e the action event
     */
    private void choicePluginBtAction(ActionEvent e) {
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
        fileChooser.setDialogTitle("Select Plugin Jar");
        int result = fileChooser.showOpenDialog(settingTable);
        if (result == JFileChooser.APPROVE_OPTION) {
            File selectedFile = fileChooser.getSelectedFile();
            String fileName = selectedFile.getName().toLowerCase();

            if (!fileName.endsWith(".jar")) {
                log.warn("Invalid file type selected: {}", fileName);
                JOptionPane.showMessageDialog(settingTable,
                        "Please select a valid JAR file!",
                        "Invalid File Type",
                        JOptionPane.ERROR_MESSAGE);
                return;
            }

            log.debug("Selected plugin file: {}", selectedFile.getAbsolutePath());
            pluginPath.setText(selectedFile.getAbsolutePath());
        }
    }

    /**
     * Handles the plugin upload/installation action.
     * Copies the selected plugin JAR file to the plugins directory.
     *
     * @param e the action event
     */
    private void pluginUploadBtAction(ActionEvent e) {
        File pluginDir = Path.of(PluginLoader.PLUGIN_DIR).toFile();
        pluginDir.mkdirs();
        try {
            Path source = Path.of(pluginPath.getText());
            Path target = pluginDir.toPath().resolve(source.toFile().getName());
            log.debug("Installing plugin from {} to {}", source, target);
            Files.copy(source, target, StandardCopyOption.REPLACE_EXISTING);
            log.info("Successfully installed plugin: {}", source.toFile().getName());
            JOptionPane.showMessageDialog(settingTable,
                    "✅ Installed:" + Path.of(pluginPath.getText()).toFile().getName() + "（Need ReOpen SwissKitJ）",
                    "Plugin Install Success",
                    JOptionPane.INFORMATION_MESSAGE);
        } catch (IOException ex) {
            log.error("Failed to install plugin: {}", pluginPath.getText(), ex);
            JOptionPane.showMessageDialog(settingTable,
                    "Error:" + ex.getMessage(),
                    "Plugin Install Error",
                    JOptionPane.ERROR_MESSAGE);
        }

    }

    private void initComponents() {
        // JFormDesigner - Component initialization - DO NOT MODIFY  //GEN-BEGIN:initComponents  @formatter:off
        settingPanle = new JPanel();
        settingTable = new JTabbedPane();
        email = new JPanel();
        label1 = new JLabel();
        comboBox1 = new JComboBox();
        label2 = new JLabel();
        textField1 = new JTextField();
        label3 = new JLabel();
        textField2 = new JTextField();
        label4 = new JLabel();
        textField3 = new JTextField();
        label5 = new JLabel();
        textField4 = new JTextField();
        checkBox1 = new JCheckBox();
        checkBox2 = new JCheckBox();
        button2 = new JButton();
        button1 = new JButton();
        button3 = new JButton();
        plugin = new JPanel();
        choicePluginBt = new JButton();
        pluginPath = new JTextField();
        pluginUploadBt = new JButton();

        //======== settingPanle ========
        {
            settingPanle.setMinimumSize(new Dimension(310, 379));
            settingPanle.setLayout(new MigLayout(
                "hidemode 3",
                // columns
                "[fill]" +
                "[592,fill]",
                // rows
                "[]" +
                "[]" +
                "[428]"));

            //======== settingTable ========
            {

                //======== email ========
                {
                    email.setLayout(new MigLayout(
                        "hidemode 3",
                        // columns
                        "[78,fill]" +
                        "[fill]" +
                        "[431,fill]",
                        // rows
                        "[]" +
                        "[]" +
                        "[]" +
                        "[]" +
                        "[]" +
                        "[]" +
                        "[]" +
                        "[]" +
                        "[]" +
                        "[]" +
                        "[]" +
                        "[]" +
                        "[]" +
                        "[345]"));

                    //---- label1 ----
                    label1.setText("Protocol");
                    email.add(label1, "cell 0 0");
                    email.add(comboBox1, "cell 1 0");

                    //---- label2 ----
                    label2.setText("ServerUrl");
                    email.add(label2, "cell 0 1");
                    email.add(textField1, "cell 1 1 2 1");

                    //---- label3 ----
                    label3.setText("ServerPort");
                    email.add(label3, "cell 0 2");
                    email.add(textField2, "cell 1 2 2 1");

                    //---- label4 ----
                    label4.setText("UserName");
                    email.add(label4, "cell 0 3");
                    email.add(textField3, "cell 1 3 2 1");

                    //---- label5 ----
                    label5.setText("PassWord");
                    email.add(label5, "cell 0 4");
                    email.add(textField4, "cell 1 4 2 1");

                    //---- checkBox1 ----
                    checkBox1.setText("TSL");
                    email.add(checkBox1, "cell 0 5");

                    //---- checkBox2 ----
                    checkBox2.setText("SSL");
                    email.add(checkBox2, "cell 1 5");

                    //---- button2 ----
                    button2.setText("SentTestEmail");
                    email.add(button2, "cell 0 7 3 1");

                    //---- button1 ----
                    button1.setText("Save");
                    email.add(button1, "cell 0 8 3 1");

                    //---- button3 ----
                    button3.setText("OpenAddressBook");
                    button3.addActionListener(e -> openAddressBookBtActionListener(e));
                    email.add(button3, "cell 0 11 3 1");
                }
                settingTable.addTab("Email", email);

                //======== plugin ========
                {
                    plugin.setLayout(new MigLayout(
                        "hidemode 3",
                        // columns
                        "[fill]" +
                        "[465,fill]",
                        // rows
                        "[]" +
                        "[]" +
                        "[]"));

                    //---- choicePluginBt ----
                    choicePluginBt.setText("ChoicePlugin");
                    choicePluginBt.addActionListener(e -> choicePluginBtAction(e));
                    plugin.add(choicePluginBt, "cell 0 0");

                    //---- pluginPath ----
                    pluginPath.setEditable(false);
                    plugin.add(pluginPath, "cell 1 0");

                    //---- pluginUploadBt ----
                    pluginUploadBt.setText("Upload");
                    pluginUploadBt.addActionListener(e -> pluginUploadBtAction(e));
                    plugin.add(pluginUploadBt, "cell 0 2 2 1");
                }
                settingTable.addTab("Plugin", plugin);
            }
            settingPanle.add(settingTable, "cell 0 0 2 3,aligny top,growy 0");
        }
        // JFormDesigner - End of component initialization  //GEN-END:initComponents  @formatter:on
    }

    // JFormDesigner - Variables declaration - DO NOT MODIFY  //GEN-BEGIN:variables  @formatter:off
    private JPanel settingPanle;
    private JTabbedPane settingTable;
    private JPanel email;
    private JLabel label1;
    private JComboBox comboBox1;
    private JLabel label2;
    private JTextField textField1;
    private JLabel label3;
    private JTextField textField2;
    private JLabel label4;
    private JTextField textField3;
    private JLabel label5;
    private JTextField textField4;
    private JCheckBox checkBox1;
    private JCheckBox checkBox2;
    private JButton button2;
    private JButton button1;
    private JButton button3;
    private JPanel plugin;
    private JButton choicePluginBt;
    private JTextField pluginPath;
    private JButton pluginUploadBt;
    // JFormDesigner - End of variables declaration  //GEN-END:variables  @formatter:on
}
