package fan.summer.kitpage.excel.worker;

import javax.swing.*;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * Worker for splitting Excel files into separate files.
 * Runs in background thread to avoid blocking UI.
 *
 * @author phoebej
 * @version 1.00
 * @Date 2026/2/27
 */
public class ExcelSplitWorker extends SwingWorker<Void, Integer> {
    private Path outputPath;
    private final JProgressBar progressBar;
    private final JButton button;

    // Configuration map for split options
    private Map<String, Object> config = new HashMap<>();

    /**
     * Constructor for ExcelSplitWorker.
     *
     * @param outputPath  the directory to save split files
     * @param progressBar the progress bar to update
     * @param button      the button to disable during processing
     */
    public ExcelSplitWorker(Path outputPath, JProgressBar progressBar, JButton button) {
        this.outputPath = outputPath;
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


    @Override
    protected Void doInBackground() throws Exception {
        button.setEnabled(false);
        if (config.isEmpty()) {
            button.setEnabled(true);
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

    /**
     * Splits Excel file by selected sheet names.
     * Each selected sheet will be saved to a separate file.
     *
     * @param sheetNames set of sheet names to split
     */
    private void doSplitBySheet(Set<String> sheetNames) {
        //TODO: implement split function
    }

}
