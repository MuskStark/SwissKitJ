package fan.summer.database.entity.excel;

import lombok.Data;

/**
 * 类的详细说明
 *
 * @author summer
 * @version 1.00
 * @Date 2026/3/3
 */
@Data
public class ComplexSplitConfigEntity {
    private Long id;
    private String taskId;
    private String fieldName;
    private String sheetName;
    private Integer headerIndex;
    private Integer columnIndex;
}
