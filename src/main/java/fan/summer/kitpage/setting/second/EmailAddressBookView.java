/*
 * Created by JFormDesigner on Sat Mar 07 15:01:31 CST 2026
 */

package fan.summer.kitpage.setting.second;

import net.miginfocom.swing.MigLayout;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.event.ActionEvent;
import java.util.List;

/**
 * Dialog window for displaying and managing email address book entries.
 * Shows a table with email address, nickname, and tags columns.
 * Allows adding new addresses via the AddNewAddress button.
 *
 * @author phoebej
 */
public class EmailAddressBookView extends JDialog {
    private JPanel pannel;

    public EmailAddressBookView(JPanel panel) {
        super(SwingUtilities.getWindowAncestor(panel));
        this.pannel = panel;
        initComponents();
    }

    public EmailAddressBookView initTable(List<Object[]> rowData) {
        String[] columns = {"Address", "NickName", "Tags"};
        DefaultTableModel model = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return column > 99;
            }
        };
        for (Object[] row : rowData) {
            model.addRow(row);
        }
        addressInfoTable.setModel(model);
        return this;
    }

    private void addAddressBtAction(ActionEvent e) {
        new AddAddressView(pannel).setVisible(true);

    }

    private void closeBtAction(ActionEvent e) {
        this.setVisible(false);
    }

    private void modifyTagsBtAction(ActionEvent e) {
        new EmailTagsView(pannel).setVisible(true);
    }


    private void initComponents() {
        // JFormDesigner - Component initialization - DO NOT MODIFY  //GEN-BEGIN:initComponents  @formatter:off
        scrollPane1 = new JScrollPane();
        addressInfoTable = new JTable();
        panel1 = new JPanel();
        addAddressBt = new JButton();
        modifyTagsBt = new JButton();
        closeBt = new JButton();

        //======== this ========
        var contentPane = getContentPane();
        contentPane.setLayout(new MigLayout(
            "fill,hidemode 3",
            // columns
            "[fill]" +
            "[fill]",
            // rows
            "[]" +
            "[]" +
            "[]"));

        //======== scrollPane1 ========
        {
            scrollPane1.setViewportView(addressInfoTable);
        }
        contentPane.add(scrollPane1, "cell 0 0 2 1");

        //======== panel1 ========
        {
            panel1.setLayout(new MigLayout(
                "fill,hidemode 3",
                // columns
                "[fill]" +
                "[fill]",
                // rows
                "[]" +
                "[]" +
                "[]" +
                "[]" +
                "[]"));

            //---- addAddressBt ----
            addAddressBt.setText("AddNewAddress");
            addAddressBt.addActionListener(e -> addAddressBtAction(e));
            panel1.add(addAddressBt, "cell 0 0 2 1");

            //---- modifyTagsBt ----
            modifyTagsBt.setText("ModifyTags");
            modifyTagsBt.addActionListener(e -> modifyTagsBtAction(e));
            panel1.add(modifyTagsBt, "cell 0 1 2 1");

            //---- closeBt ----
            closeBt.setText("Close");
            closeBt.addActionListener(e -> closeBtAction(e));
            panel1.add(closeBt, "cell 0 2 2 1");
        }
        contentPane.add(panel1, "cell 0 1 2 2");
        pack();
        setLocationRelativeTo(getOwner());
        // JFormDesigner - End of component initialization  //GEN-END:initComponents  @formatter:on
    }

    // JFormDesigner - Variables declaration - DO NOT MODIFY  //GEN-BEGIN:variables  @formatter:off
    private JScrollPane scrollPane1;
    private JTable addressInfoTable;
    private JPanel panel1;
    private JButton addAddressBt;
    private JButton modifyTagsBt;
    private JButton closeBt;
    // JFormDesigner - End of variables declaration  //GEN-END:variables  @formatter:on
}
