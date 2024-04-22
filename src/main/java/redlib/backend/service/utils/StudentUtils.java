package redlib.backend.service.utils;

import org.springframework.beans.BeanUtils;
import org.springframework.util.Assert;
import redlib.backend.dto.StudentDTO;
import redlib.backend.utils.FormatUtils;
import redlib.backend.vo.StudentVO;

import redlib.backend.model.Student;

import java.util.Map;

/**
 * 学生工具类，提供学生相关的实用方法
 *
 */
public class StudentUtils {

    /**
     * 规范并校验StudentDTO
     *
     * @param studentDTO 学生数据传输对象
     */
    public static void validateStudent(StudentDTO studentDTO) {
        FormatUtils.trimFieldToNull(studentDTO);
        Assert.notNull(studentDTO, "学生输入数据不能为空");
        Assert.hasText(studentDTO.getName(), "学生姓名不能为空");
        Assert.hasText(studentDTO.getStudentNumber(), "学生学号不能为空");

        // 可以增加更多的验证逻辑，如检查学号的唯一性等
    }

    /**
     * 将实体对象转换为VO对象
     *
     * @param student 实体对象
     * @param classMap 班级名称映射表
     * @return 转换后的StudentVO对象
     */
    public static StudentVO convertToVO(Student student, Map<Integer, String> classMap) {
        StudentVO studentVO = new StudentVO();
        BeanUtils.copyProperties(student, studentVO);

        studentVO.setClassName(classMap.get(student.getClassId())); // 设置班级名称
        return studentVO;
    }
}