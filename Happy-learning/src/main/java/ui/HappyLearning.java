/*
 * Created by JFormDesigner on Thu Mar 19 10:27:32 CST 2026
 */

package ui;


import fan.summer.annoattion.SwissKitPage;
import fan.summer.api.KitPage;
import net.miginfocom.swing.MigLayout;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import util.ConfigLoader;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;

/**
 * Happy Learning plugin page for SwissKitJ.
 * <p>
 * This plugin provides automated learning functionality with configurable settings.
 * Users can upload configuration files and set passkeys for authentication.
 * </p>
 *
 * @author summer
 */
@SwissKitPage()
public class HappyLearning implements KitPage {

    private static final Logger log = LoggerFactory.getLogger(HappyLearning.class);

    /**
     * Authentication key for the learning service
     */
    private String key;

    /**
     * Constructs a new HappyLearning page and initializes UI components.
     */
    public HappyLearning() {
        initComponents();
    }

    /**
     * Returns the main panel of this page.
     *
     * @return the learning panel
     */
    @Override
    public JPanel getPanel() {
        return learningPanel;
    }

    /**
     * Handles the upload button action.
     * Opens a file chooser dialog to select a config file, then copies it to the config directory.
     *
     * @param e the action event
     */
    private void uploadBtAction(ActionEvent e) {
        File configDir = Path.of(ConfigLoader.CONFIG_DIR).toFile();
        configDir.mkdirs();
        try {
            JFileChooser fileChooser = new JFileChooser();
            fileChooser.setFileSelectionMode(JFileChooser.FILES_ONLY);

            // Set config file filter
            fileChooser.setFileFilter(new javax.swing.filechooser.FileNameExtensionFilter(
                    "Config Files (*.json)", "json"
            ));
            fileChooser.setDialogTitle("Select Config File");
            int result = fileChooser.showOpenDialog(learningPanel);
            if (result == JFileChooser.APPROVE_OPTION) {
                configFilePath.setText(fileChooser.getSelectedFile().getAbsolutePath());
            }

            Path source = Path.of(configFilePath.getText());
            if (!Files.exists(source)) {
                throw new IOException("Config file not found: " + source);
            }
            Path target = configDir.toPath().resolve(source.toFile().getName());
            log.debug("Installing config from {} to {}", source, target);
            Files.copy(source, target, StandardCopyOption.REPLACE_EXISTING);
            log.info("Successfully installed config: {}", source.toFile().getName());
            JOptionPane.showMessageDialog(null,
                    "✅ Installed:" + Path.of(configFilePath.getText()).toFile().getName() + "（Need ReOpen SwissKitJ）",
                    "Plugin Install Success",
                    JOptionPane.INFORMATION_MESSAGE);
        } catch (IOException ex) {
            log.error("Failed to install config: {}", configFilePath.getText(), ex);
            JOptionPane.showMessageDialog(null,
                    "Error:" + ex.getMessage(),
                    "Plugin Install Error",
                    JOptionPane.ERROR_MESSAGE);
        }

    }

    /**
     * Handles the set passkey button action.
     * Validates and stores the passkey for authentication.
     *
     * @param e the action event
     */
    private void setPassKeyBtAction(ActionEvent e) {
        if (passKey.getText().isEmpty()) {
            JOptionPane.showMessageDialog(null,
                    "PASSKEY IS EMPTY",
                    "PASSKEY EMPTY Error",
                    JOptionPane.ERROR_MESSAGE);

        } else {
            this.key = passKey.getText();
        }

    }

    private void startBtAction(ActionEvent e) {
        // TODO add your code here
    }

    private void initComponents() {
        // JFormDesigner - Component initialization - DO NOT MODIFY  //GEN-BEGIN:initComponents  @formatter:off
        learningPanel = new JPanel();
        label4 = new JLabel();
        configFilePath = new JTextField();
        uploadBt = new JButton();
        label1 = new JLabel();
        passKey = new JTextField();
        setPassKeyBt = new JButton();
        label2 = new JLabel();
        majorSubjiectPB = new JProgressBar();
        label3 = new JLabel();
        electiveSubjectPB = new JProgressBar();
        panel1 = new JPanel();
        checkBox1 = new JCheckBox();
        checkBox2 = new JCheckBox();
        startBt = new JButton();

        //======== learningPanel ========
        {
            learningPanel.setLayout(new MigLayout(
                "hidemode 3",
                // columns
                "[fill]" +
                "[417,fill]" +
                "[fill]",
                // rows
                "[]" +
                "[]" +
                "[23]" +
                "[27]" +
                "[]" +
                "[]"));

            //---- label4 ----
            label4.setText("ConfigFile");
            learningPanel.add(label4, "cell 0 0");
            learningPanel.add(configFilePath, "cell 1 0");

            //---- uploadBt ----
            uploadBt.setText("UploadConfig");
            uploadBt.addActionListener(e -> uploadBtAction(e));
            learningPanel.add(uploadBt, "cell 2 0");

            //---- label1 ----
            label1.setText("PassKey");
            learningPanel.add(label1, "cell 0 1");
            learningPanel.add(passKey, "cell 1 1");

            //---- setPassKeyBt ----
            setPassKeyBt.setText("SetPassKey");
            setPassKeyBt.addActionListener(e -> setPassKeyBtAction(e));
            learningPanel.add(setPassKeyBt, "cell 2 1");

            //---- label2 ----
            label2.setText("MajorSubject");
            learningPanel.add(label2, "cell 0 2");
            learningPanel.add(majorSubjiectPB, "cell 1 2 2 1");

            //---- label3 ----
            label3.setText("ElectiveSubject");
            learningPanel.add(label3, "cell 0 3");
            learningPanel.add(electiveSubjectPB, "cell 1 3 2 1");

            //======== panel1 ========
            {
                panel1.setLayout(new MigLayout(
                    "hidemode 3",
                    // columns
                    "[fill]" +
                    "[fill]",
                    // rows
                    "[]" +
                    "[]" +
                    "[]"));

                //---- checkBox1 ----
                checkBox1.setText("OnlyMajorSubject");
                panel1.add(checkBox1, "cell 0 0");

                //---- checkBox2 ----
                checkBox2.setText("OnlyElectiveSubject");
                panel1.add(checkBox2, "cell 1 0");
            }
            learningPanel.add(panel1, "cell 0 4 3 1");

            //---- startBt ----
            startBt.setText("StartHappy");
            startBt.addActionListener(e -> startBtAction(e));
            learningPanel.add(startBt, "cell 1 5");
        }
        // JFormDesigner - End of component initialization  //GEN-END:initComponents  @formatter:on
    }

    // JFormDesigner - Variables declaration - DO NOT MODIFY  //GEN-BEGIN:variables  @formatter:off
    private JPanel learningPanel;
    private JLabel label4;
    private JTextField configFilePath;
    private JButton uploadBt;
    private JLabel label1;
    private JTextField passKey;
    private JButton setPassKeyBt;
    private JLabel label2;
    private JProgressBar majorSubjiectPB;
    private JLabel label3;
    private JProgressBar electiveSubjectPB;
    private JPanel panel1;
    private JCheckBox checkBox1;
    private JCheckBox checkBox2;
    private JButton startBt;
    // JFormDesigner - End of variables declaration  //GEN-END:variables  @formatter:on
}
