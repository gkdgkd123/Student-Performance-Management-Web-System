package redlib.backend.dto.query;

import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 学生查询数据传输对象，用于封装查询条件
 *
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class StudentQueryDTO extends PageQueryDTO {
    private Integer id;
    /**
     * 学生姓名，模糊匹配
     */
    private String name;

    /**
     * 学号，精确匹配
     */
    private String studentNumber;

    /**
     * 学生性别，精确匹配
     */
    private String gender;

    /**
     * 班级ID，精确匹配
     */
    private Integer classId;

    /**
     * 家长姓名，模糊匹配
     */
    private String parentName;
    private String parentPhone;
    private String className;
    private Integer allScore;
}