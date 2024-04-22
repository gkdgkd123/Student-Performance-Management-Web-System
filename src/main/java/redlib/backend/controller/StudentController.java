package redlib.backend.controller;

import jakarta.servlet.http.HttpServletResponse;
import org.apache.poi.ss.usermodel.Workbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import redlib.backend.annotation.BackendModule;
import redlib.backend.annotation.Privilege;
import redlib.backend.dto.ClassDTO;
import redlib.backend.dto.StudentDTO;
import redlib.backend.dto.query.StudentQueryDTO;
import redlib.backend.model.Page;
import redlib.backend.service.StudentService;
import redlib.backend.vo.StudentVO;

import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

/**
 * 学生管理后端服务模块
 *
 */
@RestController
@RequestMapping("/api/student")
@BackendModule({"page:页面", "update:修改", "add:创建", "delete:删除"})
public class StudentController {
    @Autowired
    private StudentService studentService;

    @PostMapping("listStudent")
    @Privilege("page")
    public Page<StudentVO> listStudents(@RequestBody StudentQueryDTO queryDTO) {
        return studentService.listByPage(queryDTO);
    }
    @GetMapping("/getStudent")
    public StudentDTO getStudent(@RequestParam Integer id) {
        return studentService.getById(id);
    }

    @PostMapping("countStudent")
    public int countStudents(@RequestBody StudentQueryDTO queryDTO) {
        return studentService.countStudents(queryDTO);
    }

    @PostMapping("addStudent")
    @Privilege("add")
    public Integer addStudent(@RequestBody StudentDTO studentDTO) {
        return studentService.addStudent(studentDTO);
    }

    @PostMapping("updateStudent")
    @Privilege("update")
    public Integer updateStudent(@RequestBody StudentDTO studentDTO) {
        return studentService.updateStudent(studentDTO);
    }

//    StudentController

    @PostMapping("deleteStudent")
    @Privilege("delete")
    public void deleteStudents(@RequestBody List<Integer> ids) {
        studentService.deleteByIds(ids);
    }

    @PostMapping("exportStudent")
    @Privilege("page")
    public void exportStudents(@RequestBody StudentQueryDTO queryDTO, HttpServletResponse response) throws Exception {
        Workbook workbook = studentService.export(queryDTO);
        response.setContentType("application/vnd.ms-excel");
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
        response.addHeader("Content-Disposition", "attachment;filename=students_" + sdf.format(new Date()) + ".xls");
        OutputStream os = response.getOutputStream();
        workbook.write(os);
        os.close();
        workbook.close();
    }

    @PostMapping("/getStudents")
    @Privilege("page")
    public Page<StudentDTO> getStudents(@RequestBody StudentQueryDTO queryDTO) {
        return studentService.listStudentsByQuery(queryDTO);
    }

    @PostMapping("/importStudent")
    @Privilege("add")
    public int importUsers(@RequestParam("file") MultipartFile file) throws Exception {
        return studentService.importStudent(file.getInputStream(), file.getOriginalFilename());
    }

}