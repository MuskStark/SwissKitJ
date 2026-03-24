package worker;

import dto.UserSearchResp;
import service.HappyLearningService;
import util.WebUtil;

import javax.swing.*;
import java.util.List;
import java.util.concurrent.CountDownLatch;

/**
 * 类的详细说明
 *
 * @author phoebej
 * @version 1.00
 * @Date 2026/3/23
 */
public class HappyLearningWorker extends SwingWorker<Void, int[]> {
    private String passKey;
    private JProgressBar majorSubjectProgressBar;
    private JProgressBar electiveSubjectProgressBar;
    private String type;
    private HappyLearningService service;
    private JButton startBt;
    private JButton stopBt;

    public HappyLearningWorker(String passKey, JProgressBar majorSubjectProgressBar, JProgressBar electiveSubjectProgressBar, String type, JButton startBt, JButton stopBt) {
        this.passKey = passKey;
        this.majorSubjectProgressBar = majorSubjectProgressBar;
        this.electiveSubjectProgressBar = electiveSubjectProgressBar;
        this.type = type;
        this.startBt = startBt;
        this.stopBt = stopBt;
        this.service = new HappyLearningService();
    }

    @Override
    protected Void doInBackground() throws Exception {
        // Initialize progress bar maximums on EDT
        SwingUtilities.invokeAndWait(this::initProcess);

        // Run autoLearning in a separate thread
        CountDownLatch latch = new CountDownLatch(1);
        Thread learningThread = new Thread(() -> {
            try {
                service.autoLearning(type, WebUtil.getValueFromCookie(passKey, "token"), passKey);
            } finally {
                latch.countDown();
            }
        });
        learningThread.start();

        // Poll progress periodically while learning is running
        while (latch.getCount() > 0) {
            if (isCancelled() || majorSubjectProgressBar.getParent() == null) {
                // Final progress update before exit
                updateProgress();
                learningThread.interrupt();
                break;
            }
            updateProgress();
            Thread.sleep(300000);
        }

        return null;
    }

    @Override
    protected void process(List<int[]> chunks) {
        // Update progress bars on EDT
        int[] latest = chunks.get(chunks.size() - 1);
        if (latest[0] >= 0) {
            majorSubjectProgressBar.setValue(latest[0]);
        }
        if (latest[1] >= 0) {
            electiveSubjectProgressBar.setValue(latest[1]);
        }
    }

    private void initProcess() {
        UserSearchResp resp = service.getPersonInfo(passKey, WebUtil.getValueFromCookie(passKey, "token"));
        Float majorSubjectGoal = resp.getData().getPeriodDataRU().getGroupLearningGoal();
        Float majorSubjectTotal = resp.getData().getPeriodDataRU().getGroupLearningTotal();
        Float electiveSubjectGoal = resp.getData().getPeriodDataRU().getSelfLearningGoal();
        Float electiveSubjectTotal = resp.getData().getPeriodDataRU().getSelfLearningTotal();

        majorSubjectProgressBar.setMaximum(majorSubjectTotal.intValue());
        majorSubjectProgressBar.setValue(majorSubjectGoal.intValue());
        electiveSubjectProgressBar.setMaximum(electiveSubjectTotal.intValue());
        electiveSubjectProgressBar.setValue(electiveSubjectGoal.intValue());
    }

    private void updateProgress() {
        UserSearchResp resp = service.getPersonInfo(passKey, WebUtil.getValueFromCookie(passKey, "token"));
        int majorCurrent = resp.getData().getPeriodDataRU().getGroupLearningTotal().intValue();
        int majorMax = majorSubjectProgressBar.getMaximum();
        int electiveCurrent = resp.getData().getPeriodDataRU().getSelfLearningTotal().intValue();
        int electiveMax = electiveSubjectProgressBar.getMaximum();

        publish(new int[]{majorMax > 0 ? majorCurrent : -1, electiveMax > 0 ? electiveCurrent : -1});
    }

    @Override
    protected void done() {
        SwingUtilities.invokeLater(() -> {
            if (startBt != null) {
                startBt.setEnabled(true);
            }
            if (stopBt != null) {
                stopBt.setEnabled(false);
            }
        });
    }
}