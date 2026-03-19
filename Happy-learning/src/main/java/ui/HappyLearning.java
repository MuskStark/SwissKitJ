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
        label1 = new JLabel();
        textField1 = new JTextField();
        button1 = new JButton();
        label2 = new JLabel();
        progressBar1 = new JProgressBar();
        label3 = new JLabel();
        progressBar2 = new JProgressBar();

        //======== learningPanel ========
        {
            learningPanel.setLayout(new MigLayout(
                "hidemode 3",
                // columns
                "[fill]" +
                "[323,fill]" +
                "[fill]",
                // rows
                "[]" +
                "[]" +
                "[]"));

            //---- label1 ----
            label1.setText("PassKey");
            learningPanel.add(label1, "cell 0 0");
            learningPanel.add(textField1, "cell 1 0");

            //---- button1 ----
            button1.setText("SetPassKey");
            learningPanel.add(button1, "cell 2 0");

            //---- label2 ----
            label2.setText("MajorSubject");
            learningPanel.add(label2, "cell 0 1");
            learningPanel.add(progressBar1, "cell 1 1 2 1");

            //---- label3 ----
            label3.setText("ElectiveSubject");
            learningPanel.add(label3, "cell 0 2");
            learningPanel.add(progressBar2, "cell 1 2 2 1");
        }
        // JFormDesigner - End of component initialization  //GEN-END:initComponents  @formatter:on
    }

    // JFormDesigner - Variables declaration - DO NOT MODIFY  //GEN-BEGIN:variables  @formatter:off
    private JPanel learningPanel;
    private JLabel label1;
    private JTextField textField1;
    private JButton button1;
    private JLabel label2;
    private JProgressBar progressBar1;
    private JLabel label3;
    private JProgressBar progressBar2;
    // JFormDesigner - End of variables declaration  //GEN-END:variables  @formatter:on
}
