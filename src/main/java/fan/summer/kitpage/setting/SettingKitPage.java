/*
 * Created by JFormDesigner on Thu Mar 05 23:18:59 CST 2026
 */

package fan.summer.kitpage.setting;

import fan.summer.annoattion.SwissKitPage;
import fan.summer.api.KitPage;
import net.miginfocom.swing.MigLayout;

import javax.swing.*;
import java.awt.*;

/**
 * @author phoebej
 */
@SwissKitPage(menuName = "Setting", menuTooltip = "Setting", order = 99999)
public class SettingKitPage implements KitPage {
    public SettingKitPage() {
        initComponents();
    }


    public JPanel getPanel() {
        return settingPanle;
    }

    private void initComponents() {
        // JFormDesigner - Component initialization - DO NOT MODIFY  //GEN-BEGIN:initComponents  @formatter:off
        settingPanle = new JPanel();
        settingTable = new JTabbedPane();
        panel1 = new JPanel();
        label1 = new JLabel();
        comboBox1 = new JComboBox();
        label2 = new JLabel();
        textField1 = new JTextField();
        label3 = new JLabel();
        textField2 = new JTextField();
        checkBox1 = new JCheckBox();
        checkBox2 = new JCheckBox();
        button2 = new JButton();
        button1 = new JButton();

        //======== settingPanle ========
        {
            settingPanle.setMinimumSize(new Dimension(310, 379));
            settingPanle.setLayout(new MigLayout(
                "hidemode 3",
                // columns
                "[fill]" +
                "[517,fill]",
                // rows
                "[]" +
                "[]" +
                "[428]"));

            //======== settingTable ========
            {

                //======== panel1 ========
                {
                    panel1.setLayout(new MigLayout(
                        "hidemode 3",
                        // columns
                        "[78,fill]" +
                        "[fill]" +
                        "[100,fill]",
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
                        "[345]"));

                    //---- label1 ----
                    label1.setText("Protocol");
                    panel1.add(label1, "cell 0 0");
                    panel1.add(comboBox1, "cell 1 0");

                    //---- label2 ----
                    label2.setText("ServerUrl");
                    panel1.add(label2, "cell 0 1");
                    panel1.add(textField1, "cell 1 1 2 1");

                    //---- label3 ----
                    label3.setText("ServerPort");
                    panel1.add(label3, "cell 0 2");
                    panel1.add(textField2, "cell 1 2 2 1");

                    //---- checkBox1 ----
                    checkBox1.setText("TSL");
                    panel1.add(checkBox1, "cell 0 3");

                    //---- checkBox2 ----
                    checkBox2.setText("SSL");
                    panel1.add(checkBox2, "cell 1 3");

                    //---- button2 ----
                    button2.setText("SentTestEmail");
                    panel1.add(button2, "cell 0 5 3 1");

                    //---- button1 ----
                    button1.setText("Save");
                    panel1.add(button1, "cell 0 6 3 1");
                }
                settingTable.addTab("Email", panel1);
            }
            settingPanle.add(settingTable, "cell 0 0 2 3,aligny top,growy 0");
        }
        // JFormDesigner - End of component initialization  //GEN-END:initComponents  @formatter:on
    }

    // JFormDesigner - Variables declaration - DO NOT MODIFY  //GEN-BEGIN:variables  @formatter:off
    private JPanel settingPanle;
    private JTabbedPane settingTable;
    private JPanel panel1;
    private JLabel label1;
    private JComboBox comboBox1;
    private JLabel label2;
    private JTextField textField1;
    private JLabel label3;
    private JTextField textField2;
    private JCheckBox checkBox1;
    private JCheckBox checkBox2;
    private JButton button2;
    private JButton button1;
    // JFormDesigner - End of variables declaration  //GEN-END:variables  @formatter:on
}
