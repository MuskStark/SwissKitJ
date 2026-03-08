package fan.summer.kitpage.setting.worker.second;

import fan.summer.database.DatabaseInit;
import fan.summer.database.entity.setting.email.EmailAddressBookEntity;
import fan.summer.database.mapper.setting.email.EmailAddressBookMapper;
import org.apache.ibatis.session.SqlSession;

import javax.swing.*;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.ExecutionException;

/**
 * SwingWorker for querying all email address book entries in background.
 * Executes database query on a background thread and notifies callback on completion.
 *
 * @author phoebej
 * @version 1.00
 * @Date 2026/3/8
 */
public class QueryAllEmailInfoWorker extends SwingWorker<List<EmailAddressBookEntity>, Void> {
    private QueryAllEmailInfoCallBack callBack;

    public QueryAllEmailInfoWorker(QueryAllEmailInfoCallBack callBack) {
        this.callBack = callBack;
    }

    @Override
    protected List<EmailAddressBookEntity> doInBackground() throws Exception {
        try (SqlSession session = DatabaseInit.getSqlSession()) {
            EmailAddressBookMapper mapper = session.getMapper(EmailAddressBookMapper.class);
            return mapper.selectEmailAddressBook();
        } catch (Exception e) {
            return Collections.emptyList();
        }
    }


    @Override
    protected void done() {
        List<EmailAddressBookEntity> emailAddressBookEntities = null;
        try {
            emailAddressBookEntities = get();
            callBack.onSuccess(emailAddressBookEntities);

        } catch (RuntimeException | InterruptedException | ExecutionException e) {
            callBack.onFailure(e);
        }

    }
}
