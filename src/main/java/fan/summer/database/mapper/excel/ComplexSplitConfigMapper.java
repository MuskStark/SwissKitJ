package fan.summer.database.mapper.excel;

import fan.summer.database.entity.excel.ComplexSplitConfigEntity;

/**
 * Mapper interface for ComplexSplitConfigEntity CRUD operations.
 *
 * @author summer
 * @version 1.00
 * @date 2026/3/3
 */
public interface ComplexSplitConfigMapper {

    /**
     * Inserts a new complex split configuration record.
     *
     * @param complexSplitConfigEntity the entity to insert
     */
    void insert(ComplexSplitConfigEntity complexSplitConfigEntity);

    /**
     * Deletes all configuration records for a given task ID.
     *
     * @param taskId the task ID to delete records for
     */
    void deleteAllByTaskId(String taskId);
}
