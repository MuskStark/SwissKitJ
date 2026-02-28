package fan.summer.kitpage.excel.worker;

import fan.summer.kitpage.excel.listener.HeaderListener;
import org.apache.fesod.sheet.ExcelReader;
import org.apache.fesod.sheet.FesodSheet;
import org.apache.fesod.sheet.read.metadata.ReadSheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;

import javax.swing.*;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class ExcelAnalysisWorker extends SwingWorker<Map<String, Map<Integer, String>>, Integer> {

    private final ExcelAnalysisCallback callback;
    private final Path filePath;
    private final JProgressBar progressBar;
    private final JButton startBtn;

    /**
     * Constructor - Initialize the Excel analysis worker
     *
     * @param filePath    the path to the Excel file to analyze
     * @param progressBar the progress bar to update during analysis
     * @param startBtn    the start button to enable/disable during analysis
     * @param callback    the callback interface to notify of success or failure
     */
    public ExcelAnalysisWorker(Path filePath, JProgressBar progressBar, JButton startBtn, ExcelAnalysisCallback callback) {
        this.filePath = filePath;
        this.progressBar = progressBar;
        this.startBtn = startBtn;
        this.callback = callback;
    }

    /**
     * Perform the Excel file analysis in a background thread
     * Retrieves all sheet names and their headers using Apache POI and Apache FESOD
     *
     * @return a map where keys are sheet names and values are lists of header strings
     * @throws Exception if the file cannot be read or parsed
     */
    @Override
    protected Map<String, Map<Integer, String>> doInBackground() throws Exception {
        // Initialize progress bar in EDT thread
        SwingUtilities.invokeLater(() -> {
            progressBar.setValue(0);
            progressBar.setStringPainted(true);
        });

        // 1. Get all sheet names
        List<String> sheetNames = new ArrayList<>();
        try (Workbook workbook = WorkbookFactory.create(filePath.toFile())) {
            for (int i = 0; i < workbook.getNumberOfSheets(); i++) {
                sheetNames.add(workbook.getSheetName(i));
            }
        }

        int total = sheetNames.size();
        Map<String, Map<Integer, String>> result = new LinkedHashMap<>();

        // 2. Read sheet headers one by one, update progress after each sheet is read
        try (ExcelReader excelReader = FesodSheet.read(filePath.toFile()).build()) {
            for (int i = 0; i < sheetNames.size(); i++) {
                String sheetName = sheetNames.get(i);

                HeaderListener headerListener = new HeaderListener();
                ReadSheet readSheet = FesodSheet.readSheet(sheetName)
                        .headRowNumber(1)
                        .registerReadListener(headerListener)
                        .build();
                excelReader.read(readSheet);

                result.put(sheetName, headerListener.getHeaders());

                // Calculate percentage and publish progress
                int progress = (int) ((i + 1) * 100.0 / total);
                publish(progress);
                setProgress(progress); // Can also use PropertyChangeListener to monitor
            }
        }

        return result;
    }

    /**
     * Process intermediate results from doInBackground in the EDT thread
     * Updates the progress bar with the latest progress value
     *
     * @param chunks a list of progress values published by doInBackground
     */
    @Override
    protected void process(List<Integer> chunks) {
        // EDT thread: Update progress bar
        int latestProgress = chunks.get(chunks.size() - 1);
        progressBar.setValue(latestProgress);
        progressBar.setString("Parsing... " + latestProgress + "%");
    }

    /**
     * Called when the background task is complete in the EDT thread
     * Updates the UI with success or failure message and invokes the appropriate callback
     */
    @Override
    protected void done() {
        // EDT thread: Task completion cleanup
        try {
            Map<String, Map<Integer, String>> result = get(); // Get return value from doInBackground
            progressBar.setString("Parsing completed! Total " + result.size() + " sheets");
            startBtn.setEnabled(true);
            // Post-process result, such as displaying in a table...
            callback.onSuccess(result);
        } catch (Exception e) {
            progressBar.setString("Parsing failed: " + e.getMessage());
            startBtn.setEnabled(false);
            callback.onFailure(e);
        }
    }
}