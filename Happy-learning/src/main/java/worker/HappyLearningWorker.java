package worker;

import dto.UserSearchResp;
import service.HappyLearningService;
import util.WebUtil;

import javax.swing.*;

/**
 * 类的详细说明
 *
 * @author phoebej
 * @version 1.00
 * @Date 2026/3/23
 */
public class HappyLearningWorker extends SwingWorker<Void, Void> {
    private String passKey;
    private JProgressBar majorSubjectProgressBar;
    private JProgressBar electiveSubjectProgressBar;
    private String type;
    private HappyLearningService service;

    public HappyLearningWorker(String passKey, JProgressBar majorSubjectProgressBar, JProgressBar electiveSubjectProgressBar, String type) {
        this.passKey = passKey;
        this.majorSubjectProgressBar = majorSubjectProgressBar;
        this.electiveSubjectProgressBar = electiveSubjectProgressBar;
        this.type = type;
        this.service = new HappyLearningService();
    }

    @Override
    protected Void doInBackground() throws Exception {
        service.autoLearning(type, WebUtil.getValueFromCookie(passKey, "token"), passKey);
        return null;
    }

    private void initProcess() {
        UserSearchResp resp = service.getPersonInfo(passKey, WebUtil.getValueFromCookie(passKey, "token"));
        resp.getData().getPeriodDataRU().getGroupLearningGoal();
        resp.getData().getPeriodDataRU().getSelfLearningGoal();
    }
}
