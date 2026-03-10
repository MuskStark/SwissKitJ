/*
 * Created by JFormDesigner on Sun Mar 08 00:03:23 CST 2026
 */

package fan.summer.kitpage.setting.second;

import fan.summer.database.DatabaseInit;
import fan.summer.database.entity.setting.email.EmailAddressBookEntity;
import fan.summer.database.mapper.setting.email.EmailAddressBookMapper;
import fan.summer.utils.StringUtil;
import net.miginfocom.swing.MigLayout;
import org.apache.ibatis.session.SqlSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;

/**
 * Dialog window for adding a new email address entry.
 * Validates email format before saving to database.
 *
 * @author phoebej
 */
public class AddAddressView extends JDialog {
    private static final Logger log = LoggerFactory.getLogger(AddAddressView.class);

    public AddAddressView(JPanel panel) {
        super(SwingUtilities.getWindowAncestor(panel));
        initComponents();
    }

    /**
     * Handles the insert button action.
     * Validates the email address and saves it to the database.
     *
     * @param e the action event
     */
    private void insertBtAction(ActionEvent e) {
        // Validate email format
        if (StringUtil.checkEmail(addressField.getText())) {
            log.debug("Inserting email address: {}", addressField.getText());
            try (SqlSession session = DatabaseInit.getSqlSession()) {
                EmailAddressBookMapper mapper = session.getMapper(EmailAddressBookMapper.class);
                EmailAddressBookEntity emailAddressBookEntity = new EmailAddressBookEntity();
                emailAddressBookEntity.setEmailAddress(addressField.getText());
                emailAddressBookEntity.setNickname(nicknameField.getText());
                emailAddressBookEntity.setTags(tagsField.getText());
                mapper.insert(emailAddressBookEntity);
                session.commit();
                log.info("Successfully inserted email address: {}", addressField.getText());
                this.setVisible(false);
            } catch (Exception ex) {
                log.error("Failed to insert email address: {}", addressField.getText(), ex);
                this.setVisible(false);
                JOptionPane.showMessageDialog(this,
                        "Can Not Insert Email Address :" + ex.getMessage(),
                        "Error",
                        JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private void initComponents() {
        // JFormDesigner - Component initialization - DO NOT MODIFY  //GEN-BEGIN:initComponents  @formatter:off
        dialogPane = new JPanel();
        contentPanel = new JPanel();
        label1 = new JLabel();
        addressField = new JTextField();
        label2 = new JLabel();
        nicknameField = new JTextField();
        label3 = new JLabel();
        tagsField = new JTextField();
        button1 = new JButton();
        label4 = new JLabel();
        comboBox1 = new JComboBox();
        buttonBar = new JPanel();
        okButton = new JButton();
        cancelButton = new JButton();

        //======== this ========
        var contentPane = getContentPane();
        contentPane.setLayout(new BorderLayout());

        //======== dialogPane ========
        {
            dialogPane.setLayout(new BorderLayout());

            //======== contentPanel ========
            {
                contentPanel.setLayout(new MigLayout(
                    "insets dialog,hidemode 3,align center top",
                    // columns
                    "[80,fill]" +
                    "[217,fill]" +
                    "[fill]",
                    // rows
                    "[]" +
                    "[]" +
                    "[]" +
                    "[]"));

                //---- label1 ----
                label1.setText("Address");
                contentPanel.add(label1, "cell 0 0");
                contentPanel.add(addressField, "cell 1 0");

                //---- label2 ----
                label2.setText("NickName");
                contentPanel.add(label2, "cell 0 1");
                contentPanel.add(nicknameField, "cell 1 1");

                //---- label3 ----
                label3.setText("Tags");
                contentPanel.add(label3, "cell 0 2");

                //---- tagsField ----
                tagsField.setEditable(false);
                contentPanel.add(tagsField, "cell 1 2");

                //---- button1 ----
                button1.setText("Reset");
                contentPanel.add(button1, "cell 2 2");

                //---- label4 ----
                label4.setText("ChoiceTag");
                contentPanel.add(label4, "cell 0 3");
                contentPanel.add(comboBox1, "cell 1 3");
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
                okButton.setText("Insert");
                okButton.addActionListener(e -> insertBtAction(e));
                buttonBar.add(okButton, "cell 0 0");

                //---- cancelButton ----
                cancelButton.setText("Cancel");
                buttonBar.add(cancelButton, "cell 1 0");
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
    private JLabel label1;
    private JTextField addressField;
    private JLabel label2;
    private JTextField nicknameField;
    private JLabel label3;
    private JTextField tagsField;
    private JButton button1;
    private JLabel label4;
    private JComboBox comboBox1;
    private JPanel buttonBar;
    private JButton okButton;
    private JButton cancelButton;
    // JFormDesigner - End of variables declaration  //GEN-END:variables  @formatter:on
}
