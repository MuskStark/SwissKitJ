package fan.summer.database.mapper.excel;

import fan.summer.database.entity.excel.ComplexSplitConfigEntity;

public interface ComplexSplitConfigMapper {
    void insert(ComplexSplitConfigEntity complexSplitConfigEntity);
    void deleteAllByTaskId(String taskId);
}
