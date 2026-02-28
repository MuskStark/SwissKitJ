package fan.summer.kitpage.excel.worker;

import fan.summer.kitpage.excel.listener.NoModelDataListener;
import org.apache.fesod.sheet.EasyExcel;
import org.apache.fesod.sheet.ExcelReader;
import org.apache.fesod.sheet.FesodSheet;
import org.apache.fesod.sheet.read.metadata.ReadSheet;
import org.apache.fesod.sheet.context.AnalysisContext;
import org.apache.fesod.sheet.event.AnalysisEventListener;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.swing.*;
import java.io.File;
import java.io.FileOutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.*;

/**
 * Worker for splitting Excel files into separate files.
 * Runs in background thread to avoid blocking UI.
 *
 * @author phoebej
 * @version 1.00
 * @Date 2026/2/27
 */
public class ExcelSplitWorker extends SwingWorker<Void, Integer> {
    private static final Logger logger = LoggerFactory.getLogger(ExcelSplitWorker.class);

    private final Path outputPath;
    private final Path orgFilePath;
    private final JProgressBar progressBar;
    private final JButton button;
    // Configuration map for split options
    private Map<String, Object> config = new HashMap<>();

    /**
     * Constructor for ExcelSplitWorker.
     *
     * @param outputPath       the directory to save split files
     * @param orgFilePath      the original Excel file path
     * @param progressBar      the progress bar to update
     * @param button           the button to disable during processing
     */
    public ExcelSplitWorker(Path outputPath, Path orgFilePath, JProgressBar progressBar, JButton button) {
        this.outputPath = outputPath;
        this.orgFilePath = orgFilePath;
        this.progressBar = progressBar;
        this.button = button;
    }

    /**
     * Sets the split model to split by selected sheet names.
     *
     * @param sheetNames set of sheet names to split into separate files
     * @return this worker for method chaining
     */
    public ExcelSplitWorker setSplitSheetModel(Set<String> sheetNames) {
        config.clear();
        config.put("model", "SSM");
        config.put("sheetNames", sheetNames);
        return this;
    }

    public ExcelSplitWorker setExcelFileAnalysisResultMap(Map<String, Map<Integer, String>> excelFileAnalysisResultMap) {
        if(configValidation()){
            config.put("excelFileAnalysisResultMap", excelFileAnalysisResultMap);
        }
        return this;
    }


    @Override
    protected Void doInBackground() throws Exception {
        // Reset progress bar and disable button in EDT thread
        SwingUtilities.invokeLater(() -> {
            progressBar.setValue(0);
            progressBar.setStringPainted(true);
            progressBar.setString("Splitting... 0%");
            button.setEnabled(false);
        });

        if (config.isEmpty()) {
            SwingUtilities.invokeLater(() -> {
                progressBar.setString("Please select split mode first");
            });
            throw new RuntimeException("Need Set Split Model First!");
        } else {
            switch ((String) config.get("model")) {
                case "SSM":
                    doSplitBySheet((Set<String>) config.get("sheetNames"));
                    return null;
                default:
                    return null;
            }
        }

    }

    @Override
    protected void done() {
        // EDT thread: Task completion
        try {
            get(); // Check for exceptions
            progressBar.setValue(100);
            progressBar.setString("Splitting completed!");
            button.setEnabled(true);
        } catch (Exception e) {
            progressBar.setString("Splitting failed: " + e.getMessage());
            // Button will be enabled in doInBackground when error occurs
        }
    }

    /**
     * Splits Excel file by selected sheet names.
     * Each selected sheet will be saved to a separate file.
     *
     * @param sheetNames set of sheet names to split
     */
    private void doSplitBySheet(Set<String> sheetNames) {
        NoModelDataListener noModelDataListener = new NoModelDataListener();
        Map<String, Map<Integer, String>> excelFileAnalysisResultMap = (Map<String, Map<Integer, String>>) config.get("excelFileAnalysisResultMap");
        int totalSheets = sheetNames.size();
        int currentSheet = 0;

        try (ExcelReader excelReader = FesodSheet.read(orgFilePath.toFile()).build()) {
            for (String sheetName : sheetNames) {
                ReadSheet sheet2 = FesodSheet.readSheet(sheetName).registerReadListener(noModelDataListener).build();
                excelReader.read(sheet2);
                List<Map<Integer, Object>> cachedDataList = noModelDataListener.getCachedDataList();
                Map<Integer, String> headerMap = excelFileAnalysisResultMap.get(sheetName);
                List<List<String>> headers= buildHeaders(headerMap);
                List<List<Object>> rows = buildRows(headerMap,cachedDataList);
                Path resolve = outputPath.resolve(sheetName +".xlsx");
                FesodSheet.write(resolve.toFile())
                        .sheet(sheetName)
                        .head(headers)
                        .doWrite(rows);
                noModelDataListener.clear();
                currentSheet++;
                publish(currentSheet * 100 / totalSheets);
            }
        }
    }

    /**
     * Processes progress updates published by doInBackground.
     * Updates the progress bar in the EDT thread.
     *
     * @param chunks list of progress values
     */
    @Override
    protected void process(List<Integer> chunks) {
        if (!chunks.isEmpty()) {
            int latestProgress = chunks.get(chunks.size() - 1);
            progressBar.setValue(latestProgress);
            progressBar.setString("Splitting... " + latestProgress + "%");
        }
    }

    // Convert Map<Integer, String> header to List<List<String>> format required by EasyExcel
    private static List<List<String>> buildHeaders(Map<Integer, String> headMap) {
        List<List<String>> headers = new ArrayList<>();
        // Sort by column index to ensure column order
        new TreeMap<>(headMap).forEach((index, name) -> {
            headers.add(Collections.singletonList(name));
        });
        return headers;
    }

    private static List<List<Object>> buildRows(Map<Integer, String> headMap,
                                                List<Map<Integer, Object>> dataList) {
        List<Integer> sortedKeys = new ArrayList<>(new TreeMap<>(headMap).keySet());
        List<List<Object>> rows = new ArrayList<>();
        for (Map<Integer, Object> rowMap : dataList) {
            List<Object> row = new ArrayList<>();
            for (Integer key : sortedKeys) {
                row.add(rowMap.getOrDefault(key, ""));
            }
            rows.add(row);
        }
        return rows;
    }

    private boolean configValidation(){
        try{
            config.get("model");
            return true;
        }catch (Exception e){
            return false;
        }
    }







}
