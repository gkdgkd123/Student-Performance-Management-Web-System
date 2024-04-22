package redlib.backend.dto;

import lombok.Data;

/**
 * 学生数据传输对象
 *
 */
@Data
public class StudentDTO {
    private Integer id;

    /**
     * 学生姓名
     */
    private String name;

    /**
     * 学号
     */
    private String studentNumber;

    /**
     * 学生性别
     */
    private String gender;

    /**
     * 家长姓名
     */
    private String parentName;

    /**
     * 家长联系电话
     */
    private String parentPhone;

    /**
     * 班级ID
     */
    private Integer classId;
    private String className;
    private Integer allScore;
}