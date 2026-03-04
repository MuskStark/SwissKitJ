package fan.summer.kitpage.excel.worker;

import fan.summer.database.DatabaseInit;
import fan.summer.database.entity.excel.ComplexSplitConfigEntity;
import fan.summer.database.mapper.excel.ComplexSplitConfigMapper;
import fan.summer.kitpage.excel.second.ConfigView;
import org.apache.ibatis.session.SqlSession;

import javax.swing.*;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

/**
 * 类的详细说明
 *
 * @author phoebej
 * @version 1.00
 * @Date 2026/3/4
 */
public class ShowConfigViewWorker extends SwingWorker<List<Object[]>, Void> {
    private final JPanel jpanel;
    private final String taskId;

    public ShowConfigViewWorker(JPanel jpanel, String taskId) {
        this.jpanel = jpanel;
        this.taskId = taskId;
    }

    @Override
    protected List<Object[]> doInBackground() throws Exception {
        List<Object[]> rowDatas = new ArrayList<>();
        try (SqlSession session = DatabaseInit.getSqlSession()) {
            ComplexSplitConfigMapper mapper = session.getMapper(ComplexSplitConfigMapper.class);
            List<ComplexSplitConfigEntity> complexSplitConfigEntities = mapper.selectAllByTaskId(taskId);
            if (complexSplitConfigEntities.isEmpty()) {
                return null;
            } else {
                for (ComplexSplitConfigEntity entity : complexSplitConfigEntities) {
                    rowDatas.add(new Object[]{entity.getFieldName(), entity.getSheetName(), entity.getHeaderIndex(), entity.getColumnIndex()});
                    return rowDatas;
                }
            }

        } catch (Exception e) {
            return null;
        }
        return rowDatas;
    }

    @Override
    protected void done() {
        try {
            List<Object[]> objects = get();
            if (objects != null && !objects.isEmpty()) {
                new ConfigView(jpanel).setTableModel(objects).setVisible(true);
            } else {
                JOptionPane.showConfirmDialog(
                        jpanel,
                        "Info: Empty Config",
                        "ViewConfig Error",
                        JOptionPane.DEFAULT_OPTION
                );
            }
        } catch (InterruptedException | ExecutionException e) {
            JOptionPane.showConfirmDialog(
                    jpanel,
                    "Info: " + e.getMessage(),
                    "ViewConfig Error",
                    JOptionPane.DEFAULT_OPTION
            );
            throw new RuntimeException(e);
        }
    }
}
