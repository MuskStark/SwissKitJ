package plugin.swisskit.hpl.dto;


import com.alibaba.fastjson2.annotation.JSONField;
import lombok.Data;

/**
 * User learning courseware progress information.
 *
 * @since 2026-03-19
 */
@Data
public class UserLearnCourseWareVOList {
    private Long lessonId;
    private Long coursewareId;
    @JSONField(name = "learnprogress")
    private Float learnProgress;
    private Integer passed;
}
