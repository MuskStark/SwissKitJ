/*
 * Created by JFormDesigner on Mon Mar 09 22:10:10 CST 2026
 */

package fan.summer.kitpage.setting.second;

import fan.summer.database.DatabaseInit;
import fan.summer.database.entity.setting.email.EmailTagEntity;
import fan.summer.database.mapper.setting.email.EmailTagMapper;
import fan.summer.ui.components.GradientProgressBar;
import net.miginfocom.swing.MigLayout;
import org.apache.ibatis.session.SqlSession;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;

/**
 * @author phoebej
 */
public class EmailTagsView extends JDialog {
    public EmailTagsView(JPanel panel) {
        super(SwingUtilities.getWindowAncestor(panel));
        ;
        initComponents();
    }

    private void addTagBtAction(ActionEvent e) {
        new SwingWorker<Void, Void>() {
            private Exception error;

            @Override
            protected Void doInBackground() throws Exception {
                SwingUtilities.invokeLater(() -> {
                    progressBar1.setValue(0);
                    progressBar1.setStringPainted(true);
                    progressBar1.setString("Please wait...");
                    progressBar1.setVisible(true);
                });
                if (tagField.getText() != null && !tagField.getText().isEmpty()) {
                    try (SqlSession session = DatabaseInit.getSqlSession()) {
                        EmailTagMapper mapper = session.getMapper(EmailTagMapper.class);
                        EmailTagEntity emailTagEntity = new EmailTagEntity();
                        emailTagEntity.setTag(tagField.getText());
                        mapper.insert(emailTagEntity);
                        session.commit();
                    } catch (Exception ex) {
                        error = ex;
                        throw ex;
                    }
                } else {
                    throw new RuntimeException("Tag field is empty!");
                }

                return null;
            }

            @Override
            protected void done() {
                try {
                    get();
                    progressBar1.setValue(100);
                    progressBar1.setString("Done");
                } catch (Exception ex) {
                    progressBar1.setString("Error: " + ex.getMessage());
                    JOptionPane.showMessageDialog(EmailTagsView.this,
                            "Failed to add tag: " + ex.getMessage(),
                            "Error",
                            JOptionPane.ERROR_MESSAGE);
                }
            }
        }.execute();
    }

    private void closeBtAction(ActionEvent e) {
        this.setVisible(false);
    }

    private void createUIComponents() {
        progressBar1 = new GradientProgressBar();
    }

    private void initComponents() {
        // JFormDesigner - Component initialization - DO NOT MODIFY  //GEN-BEGIN:initComponents  @formatter:off
        createUIComponents();

        dialogPane = new JPanel();
        contentPanel = new JPanel();
        scrollPane1 = new JScrollPane();
        tagTable = new JTable();
        label1 = new JLabel();
        tagField = new JTextField();
        addTagBt = new JButton();
        closeBt = new JButton();

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
                    "[113,fill]" +
                    "[324,fill]" +
                    "[404,fill]",
                    // rows
                    "[]" +
                    "[]" +
                    "[]" +
                    "[]" +
                    "[]"));

                //======== scrollPane1 ========
                {
                    scrollPane1.setViewportView(tagTable);
                }
                contentPanel.add(scrollPane1, "cell 1 0 3 1");

                //---- label1 ----
                label1.setText("Tag");
                contentPanel.add(label1, "cell 1 1,alignx center,growx 0");
                contentPanel.add(tagField, "cell 2 1");

                //---- addTagBt ----
                addTagBt.setText("AddNewTAg");
                addTagBt.addActionListener(e -> addTagBtAction(e));
                contentPanel.add(addTagBt, "cell 3 1");
                contentPanel.add(progressBar1, "cell 0 2 4 1");

                //---- closeBt ----
                closeBt.setText("Close");
                closeBt.addActionListener(e -> closeBtAction(e));
                contentPanel.add(closeBt, "cell 1 3 3 1");
            }
            dialogPane.add(contentPanel, BorderLayout.CENTER);
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
    private JTable tagTable;
    private JLabel label1;
    private JTextField tagField;
    private JButton addTagBt;
    private JProgressBar progressBar1;
    private JButton closeBt;
    // JFormDesigner - End of variables declaration  //GEN-END:variables  @formatter:on
}
