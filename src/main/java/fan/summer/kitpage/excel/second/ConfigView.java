/*
 * Created by JFormDesigner on Wed Mar 04 22:42:59 CST 2026
 */

package fan.summer.kitpage.excel.second;

import net.miginfocom.swing.MigLayout;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.util.List;


/**
 * @author phoebej
 */
public class ConfigView extends JDialog {
    public ConfigView(JPanel panel) {
        super(SwingUtilities.getWindowAncestor(panel));
        initComponents();
    }

    public ConfigView setTableModel(List<Object[]> rowDatas) {
        String[] columns = {"FileName", "SheetName", "HeaderIndex", "SplitBYColumnIndex"};
        DefaultTableModel defaultTableModel = new DefaultTableModel(columns, 0);
        for (Object[] row : rowDatas) {
            defaultTableModel.addRow(row);
        }
        this.configInfo.setModel(defaultTableModel);
        return this;
    }

    private void okBtAction(ActionEvent e) {
        // TODO add your code here
        this.setVisible(false);
    }

    private void initComponents() {
        // JFormDesigner - Component initialization - DO NOT MODIFY  //GEN-BEGIN:initComponents  @formatter:off
        dialogPane = new JPanel();
        contentPanel = new JPanel();
        scrollPane1 = new JScrollPane();
        configInfo = new JTable();
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
                    "fill,insets dialog,hidemode 3,align center center",
                    // columns
                    "[fill]" +
                    "[fill]",
                    // rows
                    "[]" +
                    "[]" +
                    "[]"));

                //======== scrollPane1 ========
                {
                    scrollPane1.setViewportView(configInfo);
                }
                contentPanel.add(scrollPane1, "cell 0 0,alignx left,growx 0");
            }
            dialogPane.add(contentPanel, BorderLayout.CENTER);

            //======== buttonBar ========
            {
                buttonBar.setLayout(new MigLayout(
                    "insets dialog,alignx right",
                    // columns
                    "[button,fill]" +
                    "[button,fill]",
                    // rows
                    null));

                //---- okButton ----
                okButton.setText("OK");
                okButton.addActionListener(e -> okBtAction(e));
                buttonBar.add(okButton, "cell 1 0");
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
    private JScrollPane scrollPane1;
    private JTable configInfo;
    private JPanel buttonBar;
    private JButton okButton;
    // JFormDesigner - End of variables declaration  //GEN-END:variables  @formatter:on
}
