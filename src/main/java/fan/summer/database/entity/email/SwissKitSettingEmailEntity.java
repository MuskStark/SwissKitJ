package fan.summer.database.entity.email;

import lombok.Data;

/**
 * 类的详细说明
 *
 * @author summer
 * @version 1.00
 * @Date 2026/3/3
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
