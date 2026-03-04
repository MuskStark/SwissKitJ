package fan.summer.database.entity.email;

import lombok.Data;

/**
 * Email settings entity
 *
 * @author summer
 * @version 1.00
 * @date 2026/3/3
 */
@Data
public class SwissKitSettingEmailEntity {
    private Integer id;
    private String email;
    private String password;
    private String smtpAddress;
    private Integer smtpPort;
    private Boolean needTLS;
    private Boolean needSSL;
}
