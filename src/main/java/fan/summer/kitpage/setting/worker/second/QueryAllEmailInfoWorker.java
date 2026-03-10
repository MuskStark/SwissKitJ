package fan.summer.kitpage.setting.worker.second;

import fan.summer.database.DatabaseInit;
import fan.summer.database.entity.setting.email.EmailAddressBookEntity;
import fan.summer.database.mapper.setting.email.EmailAddressBookMapper;
import org.apache.ibatis.session.SqlSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

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
    private static final Logger log = LoggerFactory.getLogger(QueryAllEmailInfoWorker.class);

    private QueryAllEmailInfoCallBack callBack;

    public QueryAllEmailInfoWorker(QueryAllEmailInfoCallBack callBack) {
        this.callBack = callBack;
    }

    @Override
    protected List<EmailAddressBookEntity> doInBackground() throws Exception {
        log.debug("Querying all email address book entries");
        try (SqlSession session = DatabaseInit.getSqlSession()) {
            EmailAddressBookMapper mapper = session.getMapper(EmailAddressBookMapper.class);
            List<EmailAddressBookEntity> result = mapper.selectEmailAddressBook();
            log.debug("Found {} email address book entries", result.size());
            return result;
        } catch (Exception e) {
            log.error("Failed to query email address book entries", e);
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
            log.error("Error in done() callback", e);
            callBack.onFailure(e);
        }

    }
}
