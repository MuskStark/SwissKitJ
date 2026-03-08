package fan.summer.database.entity.setting.email;

import lombok.Data;

import java.util.List;

/**
 * Entity class representing an email address book entry.
 * Stores email address, nickname, and associated tags.
 *
 * @author phoebej
 * @version 1.00
 * @Date 2026/3/7
 */
@Data
public class EmailAddressBookEntity {
    private Integer id;
    private String emailAddress;
    private String nickname;
    private String tags;
}
