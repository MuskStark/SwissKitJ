package worker;

import dto.UserSearchResp;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import service.HappyLearningService;
import util.WebUtil;

import javax.swing.*;
import java.util.List;
import java.util.concurrent.CountDownLatch;

/**
 * Happy learning background worker
 *
 * @author phoebej
 * @version 1.00
 * @Date 2026/3/23
 */
public class HappyLearningWorker extends SwingWorker<Void, int[]> {
    private static final Logger log = LoggerFactory.getLogger(HappyLearningWorker.class);
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
        log.info("Starting HappyLearningWorker, type: {}", type);
        // Initialize progress bar maximums on EDT
        SwingUtilities.invokeAndWait(this::initProcess);
        log.info("Progress bars initialized");

        // Run autoLearning in a separate thread
        CountDownLatch latch = new CountDownLatch(1);
        Thread learningThread = new Thread(() -> {
            try {
                log.info("Auto learning thread started");
                service.autoLearning(type, WebUtil.getValueFromCookie(passKey, "token"), passKey);
                log.info("Auto learning thread finished");
            } finally {
                latch.countDown();
            }
        });
        learningThread.start();

        // Poll progress periodically while learning is running
        while (latch.getCount() > 0) {
            if (isCancelled() || majorSubjectProgressBar.getParent() == null) {
                log.info("Worker cancelled or panel removed, final progress update");
                updateProgress();
                learningThread.interrupt();
                break;
            }
            updateProgress();
            try {
                Thread.sleep(300000);
            } catch (InterruptedException e) {
                log.info("Progress polling interrupted");
                Thread.currentThread().interrupt();
                break;
            }
        }

        log.info("HappyLearningWorker done");
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

        log.info("Init progress - MajorSubject: {}/{}, ElectiveSubject: {}/{}",
                majorSubjectGoal, majorSubjectTotal, electiveSubjectGoal, electiveSubjectTotal);

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

        log.debug("Progress update - MajorSubject: {}/{}, ElectiveSubject: {}/{}",
                majorCurrent, majorMax, electiveCurrent, electiveMax);

        publish(new int[]{majorMax > 0 ? majorCurrent : -1, electiveMax > 0 ? electiveCurrent : -1});
    }

    @Override
    protected void done() {
        log.info("Worker done, resetting buttons");
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