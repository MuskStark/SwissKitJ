/*
 * Created by JFormDesigner on Thu Mar 19 10:27:32 CST 2026
 */

package ui;

import net.miginfocom.swing.MigLayout;
import plugin.swisskitj.api.annoattion.SwissKitPage;
import plugin.swisskitj.api.api.KitPage;

import javax.swing.*;

/**
 * @author summer
 */
@SwissKitPage()
public class HappyLearning implements KitPage {
    public HappyLearning() {
        initComponents();
    }

    @Override
    public JPanel getPanel() {
        return learningPanel;
    }

    private void initComponents() {
        // JFormDesigner - Component initialization - DO NOT MODIFY  //GEN-BEGIN:initComponents  @formatter:off
        learningPanel = new JPanel();
        label4 = new JLabel();
        configFilePath = new JTextField();
        uploadBt = new JButton();
        label1 = new JLabel();
        passKey = new JTextField();
        button1 = new JButton();
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
            learningPanel.add(uploadBt, "cell 2 0");

            //---- label1 ----
            label1.setText("PassKey");
            learningPanel.add(label1, "cell 0 1");
            learningPanel.add(passKey, "cell 1 1");

            //---- button1 ----
            button1.setText("SetPassKey");
            learningPanel.add(button1, "cell 2 1");

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
    private JButton button1;
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
