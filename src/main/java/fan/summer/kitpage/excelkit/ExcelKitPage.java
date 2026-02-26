package fan.summer.kitpage.excelkit;

import fan.summer.kitpage.KitPage;
import fan.summer.utils.UIUtils;

import javax.swing.*;
import java.awt.*;
import java.io.File;

/**
 * 类的详细说明
 *
 * @author summer
 * @version 1.00
 * @Date 2026/2/26
 */
public class ExcelKitPage implements KitPage {
    private JPanel panel;

    public ExcelKitPage() {
        initPage();
    }

    private void initPage(){
        panel = new JPanel(new BorderLayout(0, 20));
        panel.setBackground(Color.WHITE);

        JLabel title = UIUtils.createPageTitle("ExcelSplit");
        
        JButton selectFileBtn = new JButton("选择文件");
        JTextField filePathField = new JTextField(20);
        filePathField.setEditable(false);
        
        JPanel selectFileBtnPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        
        selectFileBtn.addActionListener(e -> {
            JFileChooser chooser = new JFileChooser();
            chooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
            chooser.setDialogType(JFileChooser.OPEN_DIALOG);
            chooser.setDialogTitle("选择文件");
            int result = chooser.showOpenDialog(panel);

            if (result == JFileChooser.APPROVE_OPTION) {
                File selectedFile = chooser.getSelectedFile();
                filePathField.setText(selectedFile.getAbsolutePath());
                System.out.println("选择的文件：" + selectedFile.getAbsolutePath());
            } else {
                System.out.println("用户取消了选择");
            }
        });
        
        selectFileBtnPanel.add(selectFileBtn);
        selectFileBtnPanel.add(filePathField);

        panel.add(title, BorderLayout.NORTH);
        panel.add(selectFileBtnPanel, BorderLayout.CENTER);

    }
    @Override
    public JPanel getPanel() {
        return panel;
    }

    @Override
    public String getTitle() {
        return "Excel";
    }

}
