package fan.summer.kitpage.excel;

import fan.summer.annoattion.SwissKitPage;
import fan.summer.api.KitPage;
import fan.summer.kitpage.excel.worker.ExcelAnalysisCallback;
import fan.summer.kitpage.excel.worker.ExcelAnalysisWorker;
import fan.summer.kitpage.excel.worker.ExcelSplitWorker;
import fan.summer.ui.components.FixedWidthComboBox;
import fan.summer.ui.components.GradientProgressBar;
import net.miginfocom.swing.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.swing.*;
import java.nio.file.Path;
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
@SwissKitPage(menuName = "Excel", menuTooltip = "Excel", order = 1)
public class ExcelKitPage implements KitPage {
    private static final Logger logger = LoggerFactory.getLogger(ExcelKitPage.class);

    private Path excelFilePath;
    Map<String, Map<Integer, String>> excelFileAnalysisResultMap;

    // simple split
    private String selectedSheetNm;
    private String selectedColumnNm;


    /**
     * Constructor - Initialize the Excel tool page and set up all event listeners
     */
    public ExcelKitPage() {
        initComponents();
        initComponents();
        initComponents();
        initComponents();
        excelSplitBt.setEnabled(false);
        // ActionListener for file selection button - opens file chooser dialog and updates file path
        fileSelectBt.addActionListener(e -> {
            JFileChooser fileChooser = new JFileChooser();
            fileChooser.setFileSelectionMode(JFileChooser.FILES_ONLY);

            // Set Excel file filter
            fileChooser.setFileFilter(new javax.swing.filechooser.FileNameExtensionFilter(
                    "Excel Files (*.xlsx, *.xls)", "xlsx", "xls"
            ));
            fileChooser.setDialogTitle("Select Excel File");
            int result = fileChooser.showOpenDialog(excelKitPage);
            if (result == JFileChooser.APPROVE_OPTION) {
                excelFilePath = fileChooser.getSelectedFile().toPath();
                selectedFilePath.setText(fileChooser.getSelectedFile().getAbsolutePath());
                logger.info("Excel file selected: {}", excelFilePath);
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
        excelFileAnalysisBt.addActionListener(e -> {
            excelFileAnalysisBt.setEnabled(false);
            new ExcelAnalysisWorker(excelFilePath, progressBar1, excelSplitBt, new ExcelAnalysisCallback() {
                @Override
                public void onSuccess(Map<String, Map<Integer, String>> result) {
                    excelFileAnalysisResultMap = result;
                    excelFileAnalysisBt.setEnabled(true);
                    logger.info("Excel file analysis completed successfully. Found {} sheets", result.size());
                }

                @Override
                public void onFailure(Exception e) {
                    excelFileAnalysisBt.setEnabled(true);
                    logger.error("Excel file analysis failed: {}", e.getMessage(), e);
                }
            }).execute();
        });


        // ActionListener for split button - executes file splitting logic (to be implemented)
        excelSplitBt.addActionListener(e -> {
            ExcelSplitWorker excelSplitWorker = new ExcelSplitWorker(Path.of(outputPath.getText()), excelFilePath, progressBar1, excelSplitBt);
            if (splitBySheetCheckBox.isSelected()) {
                excelSplitWorker.setSplitSheetModel(excelFileAnalysisResultMap.keySet()).setExcelFileAnalysisResultMap(excelFileAnalysisResultMap).execute();
            } else if (splitByColumnCheckBox.isSelected()) {
                excelSplitWorker.setSplitColumnModel((String) choiceSheetBox.getSelectedItem(), (String) choiceColumnBox.getSelectedItem()).setExcelFileAnalysisResultMap(excelFileAnalysisResultMap).execute();
            } else {
                JOptionPane.showMessageDialog(excelKitPage,
                        "Please select a split mode first!",
                        "Warning",
                        JOptionPane.WARNING_MESSAGE);
            }

        });

        // ChangeListener for split by sheet checkbox - triggers mutual exclusion logic
        splitBySheetCheckBox.addActionListener(e -> checkBoxInterlocked());

        // ChangeListener for split by column checkbox - loads sheet names and triggers mutual exclusion logic
        splitByColumnCheckBox.addActionListener(e -> {
            choiceSheetBox.removeAllItems();
            if (splitByColumnCheckBox.isSelected()) {
                // check excelFileAnalysisResultMap
                if (excelFileAnalysisResultMap != null) {
                    Set<String> sheets = excelFileAnalysisResultMap.keySet();
                    for (String sheet : sheets) {
                        choiceSheetBox.addItem(sheet);
                    }
                } else {
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
                excelFileAnalysisResultMap.get(sheetName).values().forEach(sheet -> {
                    choiceColumnBox.addItem(sheet);
                });
            }
        });

        // ActionListener for column selection combo box - handles column selection (to be implemented)
        choiceColumnBox.addActionListener(e -> {
            if (choiceColumnBox.getSelectedIndex() != -1) {
                String columnName = (String) choiceColumnBox.getSelectedItem();
                selectedColumnNm = columnName;
                logger.debug("Column selected: {}", columnName);
            }
        });
    }

    /**
     * Handle the mutual exclusion logic between splitBySheetCheckBox and splitByColumnCheckBox
     * When one checkbox is selected, the other is disabled and unselected
     */
    private void checkBoxInterlocked() {
        if (splitBySheetCheckBox.isSelected()) {
            splitByColumnCheckBox.setSelected(false);
            splitByColumnCheckBox.setEnabled(false);
        } else {
            splitByColumnCheckBox.setEnabled(true);
        }
        if (splitByColumnCheckBox.isSelected()) {
            splitBySheetCheckBox.setEnabled(false);
            splitBySheetCheckBox.setSelected(false);
        } else {
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
     * Initialize and create custom UI components.
     * Creates the gradient progress bar and fixed-width combo boxes for sheet and column selection.
     */
    private void createUIComponents() {
        // TODO: place custom component creation code here
        progressBar1 = new GradientProgressBar();
        choiceSheetBox = new FixedWidthComboBox(200);
        choiceColumnBox = new FixedWidthComboBox(200);
    }

    private void initComponents() {
        // JFormDesigner - Component initialization - DO NOT MODIFY  //GEN-BEGIN:initComponents  @formatter:off
        createUIComponents();

        excelKitPage = new JPanel();
        fileSelectBt = new JButton();
        selectedFilePath = new JTextField();
        outputPathBt = new JButton();
        outputPath = new JTextField();
        splitWayPane = new JTabbedPane();
        var panel1 = new JPanel();
        splitBySheetCheckBox = new JCheckBox();
        splitByColumnCheckBox = new JCheckBox();
        var label1 = new JLabel();
        var label2 = new JLabel();
        var panel2 = new JPanel();
        excelSplitBt = new JButton();
        excelFileAnalysisBt = new JButton();

        //======== excelKitPage ========
        {
            excelKitPage.setLayout(new MigLayout(
                "insets 0,hidemode 3,gap 10 5",
                // columns
                "[fill]" +
                "[grow,fill]" +
                "[fill]",
                // rows
                "[fill]" +
                "[fill]" +
                "[fill]" +
                "[grow 1,fill]"));

            //---- fileSelectBt ----
            fileSelectBt.setText("SelectFile");
            excelKitPage.add(fileSelectBt, "cell 0 0,aligny center,grow 100 0");

            //---- selectedFilePath ----
            selectedFilePath.setEditable(false);
            excelKitPage.add(selectedFilePath, "cell 1 0,aligny center,grow 100 0");

            //---- outputPathBt ----
            outputPathBt.setText("SelectOutPutPath");
            excelKitPage.add(outputPathBt, "cell 0 1,aligny center,grow 100 0");

            //---- outputPath ----
            outputPath.setEditable(false);
            excelKitPage.add(outputPath, "cell 1 1,aligny center,grow 100 0");

            //======== splitWayPane ========
            {
                splitWayPane.setEnabled(true);

                //======== panel1 ========
                {
                    panel1.setLayout(new MigLayout(
                        "insets 0,hidemode 3,gap 10 5",
                        // columns
                        "[fill]" +
                        "[fill]" +
                        "[grow 1,fill]" +
                        "[fill]" +
                        "[fill]" +
                        "[fill]" +
                        "[fill]",
                        // rows
                        "[fill]" +
                        "[]" +
                        "[fill]" +
                        "[fill]"));

                    //---- splitBySheetCheckBox ----
                    splitBySheetCheckBox.setText("SplitBySheet");
                    panel1.add(splitBySheetCheckBox, "cell 1 0,align left center,grow 0 0");

                    //---- splitByColumnCheckBox ----
                    splitByColumnCheckBox.setText("SplitByColumn");
                    panel1.add(splitByColumnCheckBox, "cell 1 1 1 3,align left center,grow 0 0");

                    //---- label1 ----
                    label1.setText("ChoiceSheet");
                    panel1.add(label1, "cell 2 1 4 1");

                    //---- choiceSheetBox ----
                    choiceSheetBox.setLightWeightPopupEnabled(true);
                    panel1.add(choiceSheetBox, "cell 2 1 4 1,growx");

                    //---- label2 ----
                    label2.setText("ChoiceColumn");
                    panel1.add(label2, "cell 2 3 4 1,align left center,grow 0 0");
                    panel1.add(choiceColumnBox, "cell 2 3 4 1,growx");
                }
                splitWayPane.addTab("SimpleSplit", panel1);
            }
            excelKitPage.add(splitWayPane, "cell 0 2 3 1,aligny center,grow 100 0");

            //======== panel2 ========
            {
                panel2.setLayout(new MigLayout(
                    "insets 0,hidemode 3,gap 10 5",
                    // columns
                    "[grow 1,fill]" +
                    "[fill]" +
                    "[fill]",
                    // rows
                    "[fill]" +
                    "[]" +
                    "[fill]" +
                    "[]" +
                    "[]"));

                //---- excelSplitBt ----
                excelSplitBt.setText("SplitExcel");
                panel2.add(excelSplitBt, "cell 0 2 3 1,aligny center,grow 100 0");

                //---- excelFileAnalysisBt ----
                excelFileAnalysisBt.setActionCommand("Button");
                excelFileAnalysisBt.setText("ExcelFileAnalysis");
                panel2.add(excelFileAnalysisBt, "cell 0 1 3 1,aligny center,grow 100 0");
                panel2.add(progressBar1, "cell 0 3 3 2");
            }
            excelKitPage.add(panel2, "cell 0 3 3 1,aligny top,grow 100 0");
        }
        // JFormDesigner - End of component initialization  //GEN-END:initComponents  @formatter:on
    }

    // JFormDesigner - Variables declaration - DO NOT MODIFY  //GEN-BEGIN:variables  @formatter:off
    private JPanel excelKitPage;
    private JButton fileSelectBt;
    private JTextField selectedFilePath;
    private JButton outputPathBt;
    private JTextField outputPath;
    private JTabbedPane splitWayPane;
    private JCheckBox splitBySheetCheckBox;
    private JCheckBox splitByColumnCheckBox;
    private JComboBox choiceSheetBox;
    private JComboBox choiceColumnBox;
    private JButton excelSplitBt;
    private JButton excelFileAnalysisBt;
    private JProgressBar progressBar1;
    // JFormDesigner - End of variables declaration  //GEN-END:variables  @formatter:on
}
