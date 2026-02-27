package fan.summer.kitpage.excel;

import fan.summer.kitpage.KitPage;
import fan.summer.kitpage.excel.worker.ExcelAnalysisCallback;
import fan.summer.kitpage.excel.worker.ExcelAnalysisWorker;
import fan.summer.ui.components.FixedWidthComboBox;
import fan.summer.ui.components.GradientProgressBar;

import javax.swing.*;
import java.nio.file.Path;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Excel Tool Page
 * Provides file analysis and splitting functionality
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
    private JTabbedPane splitWayPane;
    private JProgressBar progressBar1;
    private JButton excelFileAnalysisBt;
    private JButton excelSplitBt;
    private JCheckBox splitBySheetCheckBox;
    private JCheckBox splitByColumnCheckBox;
    private JComboBox choiceSheetBox;
    private JComboBox choiceColumnBox;

    private Path excelFilePath;
    Map<String, List<String>> excelFileAnalysisResultMap;

    // simple split
    private String selectedSheetNm;
    private String selectedColumnNm;




    /**
     * Constructor - Initialize the Excel tool page and set up all event listeners
     */
    public ExcelKitPage() {
        // ActionListener for file selection button - opens file chooser dialog and updates file path
        fileSelectBt.addActionListener(e -> {
            JFileChooser fileChooser = new JFileChooser();
            fileChooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
            fileChooser.setDialogTitle("Select Output Directory");
            int result = fileChooser.showOpenDialog(excelKitPage);
            if (result == JFileChooser.APPROVE_OPTION) {
                excelFilePath = fileChooser.getSelectedFile().toPath();
                selectedFilePath.setText(fileChooser.getSelectedFile().getAbsolutePath());
            }

        });

        // ActionListener for output path button - opens directory chooser dialog and updates output path
        outputPathBt.addActionListener(e -> {
            JFileChooser fileChooser = new JFileChooser();
            fileChooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
            fileChooser.setDialogTitle("Select Output Directory");
            int result = fileChooser.showOpenDialog(excelKitPage);
            if (result == JFileChooser.APPROVE_OPTION) {
                outputPath.setText(fileChooser.getSelectedFile().getAbsolutePath());
            }

        });

        // ActionListener for analysis button - starts Excel file analysis in background worker
        excelFileAnalysisBt.addActionListener(e -> new ExcelAnalysisWorker(excelFilePath, progressBar1, excelSplitBt, new ExcelAnalysisCallback() {
            @Override
            public void onSuccess(Map<String, List<String>> result) {
                excelFileAnalysisResultMap = result;
            }
            @Override
            public void onFailure(Exception e) {
                System.out.println(e.getMessage());
            }
        }).execute());

        // ActionListener for split button - executes file splitting logic (to be implemented)
        excelSplitBt.addActionListener(e -> {
            // TODO: Implement file splitting logic
        });

        // ChangeListener for split by sheet checkbox - triggers mutual exclusion logic
        splitBySheetCheckBox.addActionListener( e -> checkBoxInterlocked());

        // ChangeListener for split by column checkbox - loads sheet names and triggers mutual exclusion logic
        splitByColumnCheckBox.addActionListener( e -> {
            choiceSheetBox.removeAllItems();
            if(splitByColumnCheckBox.isSelected()){
                // check excelFileAnalysisResultMap
                if(excelFileAnalysisResultMap!=null){
                    Set<String> sheets = excelFileAnalysisResultMap.keySet();
                    for (String sheet: sheets) {
                        choiceSheetBox.addItem(sheet);
                    }
                }else {
                    choiceSheetBox.removeAllItems();
                }
            }
            checkBoxInterlocked();
        });

        // ActionListener for sheet selection combo box - loads column headers for the selected sheet
        choiceSheetBox.addActionListener(e -> {
            choiceColumnBox.removeAllItems();
            if (choiceSheetBox.getSelectedIndex() != -1) {
                String sheetName = (String) choiceSheetBox.getSelectedItem();
                selectedSheetNm = sheetName;
                excelFileAnalysisResultMap.get(sheetName).forEach(sheet -> {
                    choiceColumnBox.addItem(sheet);
                });
            }
        });

        // ActionListener for column selection combo box - handles column selection (to be implemented)
        choiceColumnBox.addActionListener(e -> {
            if(choiceColumnBox.getSelectedIndex() != -1){
                String columnName = (String) choiceColumnBox.getSelectedItem();
                selectedColumnNm = columnName;
                System.out.println(columnName);
            }
        });
    }

    /**
     * Handle the mutual exclusion logic between splitBySheetCheckBox and splitByColumnCheckBox
     * When one checkbox is selected, the other is disabled and unselected
     */
    private void checkBoxInterlocked(){
        if(splitBySheetCheckBox.isSelected()){
            splitByColumnCheckBox.setSelected(false);
            splitByColumnCheckBox.setEnabled(false);
        }else {
            splitByColumnCheckBox.setEnabled(true);
        }
        if(splitByColumnCheckBox.isSelected()){
            splitBySheetCheckBox.setEnabled(false);
            splitBySheetCheckBox.setSelected(false);
        }else {
            splitBySheetCheckBox.setEnabled(true);
        }
    }

    /**
     * Get the main panel of this Excel tool page
     *
     * @return the JPanel component containing all UI elements
     */
    @Override
    public JPanel getPanel() {
        return excelKitPage;
    }

    /**
     * Get the title of this page
     *
     * @return the title string "Excel"
     */
    @Override
    public String getTitle() {
        return "Excel";
    }

    private void createUIComponents() {
        // TODO: place custom component creation code here
        progressBar1 = new GradientProgressBar();
        choiceSheetBox = new FixedWidthComboBox(200);
        choiceColumnBox = new FixedWidthComboBox(200);
    }
}
