package redlib.backend.service.impl;

import redlib.backend.dto.ScoreDTO;
import redlib.backend.model.Student;
import org.apache.poi.ss.usermodel.Workbook;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;
import redlib.backend.dao.StudentMapper;
import redlib.backend.dto.StudentDTO;
import redlib.backend.dto.query.StudentQueryDTO;
import redlib.backend.model.Page;
import redlib.backend.service.StudentService;
import redlib.backend.service.utils.StudentUtils;
import redlib.backend.utils.PageUtils;
import redlib.backend.utils.XlsUtils;
import redlib.backend.vo.StudentVO;

import java.io.InputStream;
import java.util.*;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

/**
 * 学生模块服务实现
 *
 */
@Service
public class StudentServiceImpl implements StudentService {

    @Autowired
    private StudentMapper studentMapper;


    @Override
    public Page<StudentVO> listByPage(StudentQueryDTO queryDTO) {
        if (queryDTO == null) {
            queryDTO = new StudentQueryDTO();
        }
        Integer size = studentMapper.count(queryDTO);
        PageUtils pageUtils = new PageUtils(queryDTO.getCurrent(), queryDTO.getPageSize(), size);

        if (size == 0) {
            return new Page<>(queryDTO.getCurrent(), queryDTO.getPageSize(), size, Collections.emptyList());
        }

        List<Student> students = studentMapper.list(queryDTO, pageUtils.getOffset(), pageUtils.getLimit());
        List<StudentVO> studentVOS = students.stream()
                .map(this::convertToStudentVO)
                .collect(Collectors.toList());

        return new Page<>(queryDTO.getCurrent(), queryDTO.getPageSize(), size, studentVOS);
    }

    private StudentVO convertToStudentVO(Student student) {
        StudentVO studentVO = new StudentVO();
        BeanUtils.copyProperties(student, studentVO);
        // HINT: Populate additional properties in 'classVO' as required.
        return studentVO;
    }

    @Override
    public Integer addStudent(StudentDTO studentDTO) {
        StudentUtils.validateStudent(studentDTO);
        Student student = new Student();
        BeanUtils.copyProperties(studentDTO, student);
        student.setCreatedAt(new Date());
        student.setUpdatedAt(new Date());
        studentMapper.insert(student);
        return student.getId();
    }

    @Override
    public StudentDTO getById(Integer id) {
        Assert.notNull(id, "学生id不能为空");
        Student student = studentMapper.selectByPrimaryKey(id);
        Assert.notNull(student, "未找到对应的学生");
        StudentDTO dto = new StudentDTO();
        BeanUtils.copyProperties(student, dto);
        return dto;
    }


    @Override
    public int countStudents(StudentQueryDTO queryDTO) {
        return studentMapper.count(queryDTO);
    }

    @Override
    public Integer updateStudent(StudentDTO studentDTO) {
        StudentUtils.validateStudent(studentDTO);
//        Assert.notNull(studentDTO.getId(), "学生id不能为空");
        Student student = studentMapper.selectByPrimaryKey(studentDTO.getId());
        Assert.notNull(student, "未找到对应的学生");
        BeanUtils.copyProperties(studentDTO, student);
        student.setUpdatedAt(new Date()); // 示例中未包含更新人id，实际业务中应补充此信息
        studentMapper.updateByPrimaryKey(student);
        return student.getId();
    }

    @Override
    public void deleteByIds(List<Integer> ids) {
        Assert.notEmpty(ids, "学生id列表不能为空");
        studentMapper.deleteByIds(ids);
    }


    @Override
    public Workbook export(StudentQueryDTO queryDTO) {
        queryDTO.setPageSize(100);
        Map<String, String> columnHeadings = new LinkedHashMap<>();
//        columnHeadings.put("id", "学生ID");
        columnHeadings.put("name", "学生姓名");
        columnHeadings.put("studentNumber", "学号");
        columnHeadings.put("gender", "性别");
        columnHeadings.put("parentName", "家长姓名");
        columnHeadings.put("parentPhone", "家长电话");
        columnHeadings.put("className", "班级名称");

        final AtomicBoolean finalPage = new AtomicBoolean(false);
        Workbook workbook = XlsUtils.exportToExcel(page -> {
            if (finalPage.get()) {
                return null;
            }
            queryDTO.setCurrent(page);
            List<StudentVO> list = listByPage(queryDTO).getList();
            System.out.println(list);
            if (list.size() != 100) {
                finalPage.set(true);
            }
            return list;
        }, columnHeadings);

        return workbook;
    }

    @Override
    public Page<StudentDTO> listStudentsByQuery(StudentQueryDTO queryDTO) {
        // 添加必要的业务逻辑，比如校验查询参数等

        // 模拟实现，您需要按照您项目的具体情况来实现这部分逻辑
        // 调用 StudentMapper 的方法
        List<Student> students = studentMapper.listByQuery(queryDTO);
        // 转换实体列表为 DTO 列表
        List<StudentDTO> studentDTOs = convertStudentListToDTOs(students);
        System.out.println(students);
        System.out.println(studentDTOs);
        // 获取总数用于分页
        int totalCount = studentMapper.count(queryDTO);
        // 创建分页对象并返回
        return new Page<>(queryDTO.getCurrent(), queryDTO.getPageSize(), totalCount,studentDTOs);
    }

    private List<StudentDTO> convertStudentListToDTOs(List<Student> students) {
        List<StudentDTO> studentDTOs = new ArrayList<>();
        for (Student student : students) {
            StudentDTO dto = new StudentDTO();
            dto.setId(student.getId());
            dto.setName(student.getName());
            dto.setStudentNumber(student.getStudentNumber());
            dto.setGender(student.getGender());
            dto.setParentName(student.getParentName());
            dto.setParentPhone(student.getParentPhone());
            dto.setClassId(student.getClassId());

            studentDTOs.add(dto);
        }
        return studentDTOs;
    }

    @Override
    public int importStudent(InputStream inputStream, String fileName) throws Exception {
        Assert.hasText(fileName, "文件名不能为空");

        Map<String, String> map = new LinkedHashMap<>();


        map.put("学生姓名","name");
        map.put( "学号","studentNumber");
        map.put( "性别","gender");
        map.put( "家长姓名","parentName");
        map.put( "家长电话","parentPhone");
        map.put("班级名称","className");

        AtomicInteger row = new AtomicInteger(0);
        XlsUtils.importFromExcel(inputStream, fileName, (studentDTO) -> {
            addStudent((StudentDTO) studentDTO);
            row.incrementAndGet();
        }, map, StudentDTO.class);

        return row.get();
    }
}