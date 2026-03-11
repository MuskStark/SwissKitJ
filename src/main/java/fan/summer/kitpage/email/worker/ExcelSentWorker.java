package fan.summer.kitpage.email.worker;

import fan.summer.database.DatabaseInit;
import fan.summer.database.entity.setting.email.EmailAddressBookEntity;
import fan.summer.database.mapper.setting.email.EmailAddressBookMapper;
import fan.summer.utils.EmailUtil;
import org.apache.ibatis.session.SqlSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.swing.*;
import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Background worker for sending Excel files via email.
 * Supports mass sending mode based on email tags.
 *
 * @author phoebej
 * @version 1.00
 * @Date 2026/3/11
 */
public class ExcelSentWorker extends SwingWorker<Void, Void> {

    private static final Logger log = LoggerFactory.getLogger(ExcelSentWorker.class);

    private String subject;
    private String body;
    private List<String> toTags;
    private List<String> ccTags;
    private String attachmentPath;
    private boolean isMassModel;

    public ExcelSentWorker(String subject, String body, List<String> toTags, List<String> ccTags, String attachmentPath, boolean isMassModel) {
        this.subject = subject;
        this.body = body;
        this.toTags = toTags;
        this.ccTags = ccTags;
        this.attachmentPath = attachmentPath;
        this.isMassModel = isMassModel;
    }

    @Override
    protected Void doInBackground() throws Exception {
        if (isMassModel) {
            // Parse attachment files and extract tags from filenames
            Map<String, List<File>> taggedFiles = parseAttachmentFiles(attachmentPath);
            log.debug("Found {} tagged attachment files", taggedFiles.size());

            try (SqlSession session = DatabaseInit.getSqlSession()) {
                EmailAddressBookMapper mapper = session.getMapper(EmailAddressBookMapper.class);
                List<EmailAddressBookEntity> allEmailAddresses = mapper.selectEmailAddressBook();

                // Iterate through each tag and send emails with matching attachments
                for (Map.Entry<String, List<File>> entry : taggedFiles.entrySet()) {
                    String fileTag = entry.getKey();
                    List<File> files = entry.getValue();

                    // Build recipient lists based on tags
                    List<String> toList = new ArrayList<>();
                    List<String> ccList = new ArrayList<>();

                    for (EmailAddressBookEntity entity : allEmailAddresses) {
                        String entityTag = entity.getTags();
                        if (toTags != null && toTags.contains(entityTag)) {
                            toList.add(entity.getEmailAddress());
                        }
                        if (ccTags != null && ccTags.contains(entityTag)) {
                            ccList.add(entity.getEmailAddress());
                        }
                    }

                    // Skip if no recipients
                    if (toList.isEmpty()) {
                        log.warn("No recipients found for tag: {}", fileTag);
                        continue;
                    }

                    // Build and send email with attachments
                    try {
                        EmailUtil.EmailMessage message = EmailUtil.EmailMessage.builder()
                                .to(toList)
                                .cc(ccList.isEmpty() ? null : ccList)
                                .subject(subject)
                                .textBody(body)
                                .attachments(files)
                                .build();

                        EmailUtil.sendEmail(message);
                        log.info("Email sent successfully for tag: {} to {} recipients", fileTag, toList.size());
                    } catch (EmailUtil.EmailException e) {
                        log.error("Failed to send email for tag: {} - {}", fileTag, e.getMessage(), e);
                        throw e;
                    }
                }
            }
        }

        return null;
    }

    /**
     * Parses all files in the given directory and extracts tags from filenames.
     * Tags are extracted from the characters between the last underscore (_) and the file extension (.).
     *
     * <p>Example:</p>
     * <ul>
     *   <li>report_2024_Q1.xlsx → tag: "2024_Q1"</li>
     *   <li>summary_finance_monthly.pdf → tag: "finance_monthly"</li>
     *   <li>data_test_important.xlsx → tag: "test_important"</li>
     * </ul>
     *
     * @param attachmentPath the directory path containing attachment files
     * @return a map with tag as key and list of Files as value
     */
    private Map<String, List<File>> parseAttachmentFiles(String attachmentPath) {
        Map<String, List<File>> taggedFiles = new HashMap<>();
        File dir = new File(attachmentPath);

        if (!dir.exists() || !dir.isDirectory()) {
            log.warn("Attachment directory does not exist or is not a directory: {}", attachmentPath);
            return taggedFiles;
        }

        File[] files = dir.listFiles();
        if (files == null) {
            return taggedFiles;
        }

        for (File file : files) {
            if (!file.isFile()) {
                continue;
            }

            String fileName = file.getName();
            int lastUnderscoreIndex = fileName.lastIndexOf('_');
            int lastDotIndex = fileName.lastIndexOf('.');

            // Must have both underscore and dot, and underscore must come before dot
            if (lastUnderscoreIndex > 0 && lastDotIndex > lastUnderscoreIndex) {
                String tag = fileName.substring(lastUnderscoreIndex + 1, lastDotIndex);
                taggedFiles.computeIfAbsent(tag, k -> new ArrayList<>()).add(file);
                log.debug("Parsed file: {} -> tag: {}", fileName, tag);
            } else {
                log.debug("Skipping file (no valid tag format): {}", fileName);
            }
        }

        return taggedFiles;
    }
}
