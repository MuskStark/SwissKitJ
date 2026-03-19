package dto;


import lombok.Data;

import java.util.List;

/**
 * Lesson detail containing courseware list and pass status.
 *
 * @since 2026-03-19
 */
@Data
public class LessonDetail {
    private String lessonId;
    private List<UserLearnCourseWareVOList> userlearncoursewareVOList;
    private Integer passed;

}
