package redlib.backend.service;

import org.apache.poi.ss.usermodel.Workbook;
import redlib.backend.dto.StudentDTO;
import redlib.backend.dto.query.StudentQueryDTO;
import redlib.backend.model.Page;
import redlib.backend.vo.StudentVO;

import java.io.InputStream;
import java.util.List;

/**
 * 学生模块服务接口
 *
 */
public interface StudentService {
    int countStudents(StudentQueryDTO queryDTO);
    Page<StudentVO> listByPage(StudentQueryDTO queryDTO);

    /**
     * 新建学生
     *
     * @param studentDTO 学生输入对象
     * @return 学生编码
     */
    Integer addStudent(StudentDTO studentDTO);

    /**
     * 根据ID获取学生数据
     *
     * @param id 学生编码
     * @return 学生传输对象
     */
    StudentDTO getById(Integer id);

    /**
     * 更新学生数据
     *
     * @param studentDTO 学生输入对象
     * @return 学生编码
     */
    Integer updateStudent(StudentDTO studentDTO);

    /**
     * 根据编码列表，批量删除学生
     *
     * @param ids 编码列表
     */
    void deleteByIds(List<Integer> ids);

    /**
     * 根据查询条件导出学生数据到Excel
     *
     * @param queryDTO 学生查询过滤条件
     * @return 工作簿Workbook对象，用于Excel导出
     */
    Workbook export(StudentQueryDTO queryDTO);

    Page<StudentDTO> listStudentsByQuery(StudentQueryDTO queryDTO);

    int importStudent(InputStream inputStream, String fileName) throws Exception;

}