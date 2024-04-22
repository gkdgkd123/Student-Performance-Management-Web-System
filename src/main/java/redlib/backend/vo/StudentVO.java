package redlib.backend.vo;

import lombok.Data;

import java.util.Date;

/**
 * 学生视图对象
 *
 */
@Data
public class StudentVO {
    /**
     * 学生id
     */
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

    /**
     * 班级名称
     */
    private String className;

    /**
     * 创建日期
     */
    private Date createdAt;

    /**
     * 修改日期
     */
    private Date updatedAt;

    /**
     * 创建人
     */
    private Integer createdBy;

    /**
     * 修改人
     */
    private Integer updatedBy;

    /**
     * 描述信息（如果需要）
     */
    private String description;

}