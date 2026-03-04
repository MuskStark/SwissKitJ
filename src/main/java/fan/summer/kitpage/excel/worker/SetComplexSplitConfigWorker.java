package fan.summer.kitpage.excel.worker;

import fan.summer.database.DatabaseInit;
import fan.summer.database.entity.excel.ComplexSplitConfigEntity;
import fan.summer.database.mapper.excel.ComplexSplitConfigMapper;
import org.apache.ibatis.session.SqlSession;

import javax.swing.*;
import java.nio.file.Path;
import java.util.List;

/**
 * Worker for setting complex split configuration
 *
 * @author phoebej
 * @version 1.00
 * @date 2026/3/3
 */
public class SetComplexSplitConfigWorker extends SwingWorker<Void, Integer> {
    private final JPanel fatherPanel;
    private final JProgressBar progressBar;
    private final JButton startBtn;
    private final Path excelFilePath;
    private final String splitTaskId;
    private final JComboBox comboBox;
    private final JTextField headerRowIndex;
    private final JTextField columnRowIndex;
    private boolean isError;

    public SetComplexSplitConfigWorker(JPanel fatherPanel, JProgressBar progressBar, JButton startBtn, Path excelFilePath, String splitTaskId, JComboBox comboBox, JTextField headerRowIndex, JTextField columnRowIndex) {
        this.fatherPanel = fatherPanel;
        this.progressBar = progressBar;
        this.startBtn = startBtn;
        this.excelFilePath = excelFilePath;
        this.splitTaskId = splitTaskId;
        this.comboBox = comboBox;
        this.headerRowIndex = headerRowIndex;
        this.columnRowIndex = columnRowIndex;
    }


    @Override
    protected Void doInBackground() throws Exception {
        isError = false;
        startBtn.setEnabled(false);
        SwingUtilities.invokeLater(() -> {
            progressBar.setValue(0);
            progressBar.setStringPainted(true);
        });
        try (SqlSession sqlSession = DatabaseInit.getSqlSession()) {
            ComplexSplitConfigMapper mapper = sqlSession.getMapper(ComplexSplitConfigMapper.class);
            ComplexSplitConfigEntity entity = new ComplexSplitConfigEntity();
            entity.setTaskId(this.splitTaskId);
            entity.setFieldName(excelFilePath.getFileName().toString());
            entity.setSheetName(this.comboBox.getSelectedItem().toString());
            entity.setHeaderIndex(Integer.parseInt(this.headerRowIndex.getText()));
            entity.setColumnIndex(Integer.parseInt(this.columnRowIndex.getText()));
            mapper.insert(entity);
            sqlSession.commit();
            this.comboBox.removeItemAt(this.comboBox.getSelectedIndex());
            this.headerRowIndex.setText("");
            this.columnRowIndex.setText("");
            int progress = (int) (100 * 100.0 / 100);
            publish(progress);
        } catch (Exception ex) {
            isError = true;
            JOptionPane.showConfirmDialog(
                    fatherPanel,
                    "Info: " + ex.getMessage(),
                    "Set Config Error",
                    JOptionPane.DEFAULT_OPTION
            );
        }
        return null;
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
        try {// Get return value from doInBackground
            startBtn.setEnabled(true);
            if (isError) {
                progressBar.setValue(0);
                progressBar.setString("");
            } else {
                progressBar.setString("Set Config Complete");
            }
            
            // Post-process result, such as displaying in a table...
        } catch (Exception e) {
            startBtn.setEnabled(true);
        }
    }
}
