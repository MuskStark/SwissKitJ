package fan.summer.kitpage.excel.worker;

import fan.summer.database.DatabaseInit;
import fan.summer.database.entity.excel.ComplexSplitConfigEntity;
import fan.summer.database.mapper.excel.ComplexSplitConfigMapper;
import org.apache.ibatis.session.SqlSession;

import javax.swing.*;
import java.util.List;

/**
 * 类的详细说明
 *
 * @author summer
 * @version 1.00
 * @Date 2026/3/4
 */
public class ClearComplexSplitConfigWorker extends SwingWorker<Void, Integer> {
    private final JPanel fatherPanel;
    private final String taskId;
    private final JButton button;
    private final JProgressBar progressBar;

    private boolean isError;

    public ClearComplexSplitConfigWorker(JPanel fatherPanel, String taskId, JButton button, JProgressBar progressBar) {
        this.fatherPanel = fatherPanel;
        this.taskId = taskId;
        this.button = button;
        this.progressBar = progressBar;
    }

    @Override
    protected Void doInBackground() throws Exception {
        isError = false;
        SwingUtilities.invokeLater(() -> {
            progressBar.setValue(0);
            progressBar.setStringPainted(true);
        });
        button.setEnabled(false);
        try(SqlSession session = DatabaseInit.getSqlSession()){
            ComplexSplitConfigMapper mapper = session.getMapper(ComplexSplitConfigMapper.class);
            mapper.deleteAllByTaskId(taskId);
            session.commit();
            int process = 100;
            publish(process);
        }catch (Exception e){
            isError = true;
            JOptionPane.showConfirmDialog(
                    fatherPanel,
                    "Info: " + e.getMessage(),
                    "Delete Config Error",
                    JOptionPane.DEFAULT_OPTION
            );
        }

        return null;
    }

    @Override
    protected void process(List<Integer> chunks) {
        int latestProgress = chunks.get(chunks.size() - 1);
        progressBar.setValue(latestProgress);
        progressBar.setString("Deleting... " + latestProgress + "%");
    }

    @Override
    protected void done() {
        try {// Get return value from doInBackground
            button.setEnabled(true);
            if (isError) {
                progressBar.setValue(0);
                progressBar.setString("");
            } else {
                progressBar.setString("Delete Config Complete");
            }

            // Post-process result, such as displaying in a table...
        } catch (Exception e) {
            button.setEnabled(true);
        }
    }


}
