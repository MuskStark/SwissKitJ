/*
 * Created by JFormDesigner on Fri Mar 06 09:21:49 CST 2026
 */

package fan.summer.kitpage.excel.second;

import java.awt.*;
import javax.swing.*;
import net.miginfocom.swing.*;

/**
 * @author summer
 */
public class ConfigEditorView extends JDialog {
    private JTable table;
    private int row;
    public ConfigEditorView(JPanel panel, JTable table, int row) {
        super(SwingUtilities.getWindowAncestor(panel));
        this.table = table;
        this.row = row;
        initComponents();
        fileNameText.setText(table.getValueAt(row, 0).toString());
        sheetNameText.setText(table.getValueAt(row, 1).toString());
        headerIndexText.setText(table.getValueAt(row, 2).toString());
        columnIndex.setText(table.getValueAt(row, 3).toString());
    }

    private void initComponents() {
        // JFormDesigner - Component initialization - DO NOT MODIFY  //GEN-BEGIN:initComponents  @formatter:off
        dialogPane = new JPanel();
        contentPanel = new JPanel();
        fileName = new JLabel();
        fileNameText = new JTextField();
        label2 = new JLabel();
        sheetNameText = new JTextField();
        label3 = new JLabel();
        headerIndexText = new JTextField();
        label4 = new JLabel();
        columnIndex = new JTextField();
        buttonBar = new JPanel();
        okButton = new JButton();

        //======== this ========
        var contentPane = getContentPane();
        contentPane.setLayout(new BorderLayout());

        //======== dialogPane ========
        {
            dialogPane.setLayout(new BorderLayout());

            //======== contentPanel ========
            {
                contentPanel.setLayout(new MigLayout(
                    "insets dialog,hidemode 3",
                    // columns
                    "[fill]" +
                    "[fill]" +
                    "[fill]" +
                    "[274,fill]",
                    // rows
                    "[]" +
                    "[]" +
                    "[]" +
                    "[]"));

                //---- fileName ----
                fileName.setText("FileName");
                contentPanel.add(fileName, "cell 0 0");

                //---- fileNameText ----
                fileNameText.setEditable(false);
                contentPanel.add(fileNameText, "cell 1 0 3 1");

                //---- label2 ----
                label2.setText("SheetName");
                contentPanel.add(label2, "cell 0 1");

                //---- sheetNameText ----
                sheetNameText.setEditable(false);
                contentPanel.add(sheetNameText, "cell 1 1 3 1");

                //---- label3 ----
                label3.setText("HeaderIndex");
                contentPanel.add(label3, "cell 0 2");
                contentPanel.add(headerIndexText, "cell 1 2 3 1");

                //---- label4 ----
                label4.setText("SplitByColumnIndex");
                contentPanel.add(label4, "cell 0 3");
                contentPanel.add(columnIndex, "cell 1 3 3 1");
            }
            dialogPane.add(contentPanel, BorderLayout.CENTER);

            //======== buttonBar ========
            {
                buttonBar.setLayout(new MigLayout(
                    "insets dialog,alignx right",
                    // columns
                    "[button,fill]",
                    // rows
                    null));

                //---- okButton ----
                okButton.setText("Update");
                buttonBar.add(okButton, "cell 0 0");
            }
            dialogPane.add(buttonBar, BorderLayout.SOUTH);
        }
        contentPane.add(dialogPane, BorderLayout.CENTER);
        pack();
        setLocationRelativeTo(getOwner());
        // JFormDesigner - End of component initialization  //GEN-END:initComponents  @formatter:on
    }

    // JFormDesigner - Variables declaration - DO NOT MODIFY  //GEN-BEGIN:variables  @formatter:off
    private JPanel dialogPane;
    private JPanel contentPanel;
    private JLabel fileName;
    private JTextField fileNameText;
    private JLabel label2;
    private JTextField sheetNameText;
    private JLabel label3;
    private JTextField headerIndexText;
    private JLabel label4;
    private JTextField columnIndex;
    private JPanel buttonBar;
    private JButton okButton;
    // JFormDesigner - End of variables declaration  //GEN-END:variables  @formatter:on
}
