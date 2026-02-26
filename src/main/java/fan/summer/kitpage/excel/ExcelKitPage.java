package fan.summer.kitpage.excel;

import fan.summer.kitpage.KitPage;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * 类的详细说明
 *
 * @author phoebej
 * @version 1.00
 * @Date 2026/2/26
 */
public class ExcelKitPage implements KitPage {

    private JPanel excelKitPage;
    private JButton fileSelectBt;
    private JTextField selectedFilePath;
    private JButton outputPathBt;
    private JTextField outputPath;
    private JTabbedPane tabbedPane1;
    private JProgressBar progressBar1;
    private JButton excelFileAnalysisBt;
    private JButton excelSplitBt;


    public ExcelKitPage() {
        fileSelectBt.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JFileChooser fileChooser = new JFileChooser();
                fileChooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
                fileChooser.setDialogTitle("Select Output Directory");
                int result = fileChooser.showOpenDialog(excelKitPage);
                if (result == JFileChooser.APPROVE_OPTION) {
                    selectedFilePath.setText(fileChooser.getSelectedFile().getAbsolutePath());
                }

            }
        });
        outputPathBt.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JFileChooser fileChooser = new JFileChooser();
                fileChooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
                fileChooser.setDialogTitle("Select Output Directory");
                int result = fileChooser.showOpenDialog(excelKitPage);
                if (result == JFileChooser.APPROVE_OPTION) {
                    outputPath.setText(fileChooser.getSelectedFile().getAbsolutePath());
                }

            }
        });
        excelFileAnalysisBt.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // TODO: Read selected Excel and parse structure
            }
        });
        excelSplitBt.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // TODO: Implement file splitting logic
            }
        });
    }

    @Override
    public JPanel getPanel() {
        return excelKitPage;
    }

    @Override
    public String getTitle() {
        return "Excel";
    }
}
