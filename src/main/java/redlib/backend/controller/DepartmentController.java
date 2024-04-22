package redlib.backend.controller;

import jakarta.servlet.http.HttpServletResponse;
import org.apache.poi.ss.usermodel.Workbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import redlib.backend.annotation.BackendModule;
import redlib.backend.annotation.Privilege;
import redlib.backend.dto.DepartmentDTO;
import redlib.backend.dto.query.DepartmentQueryDTO;
import redlib.backend.model.Page;
import redlib.backend.service.DepartmentService;
import redlib.backend.vo.DepartmentVO;

import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import org.springframework.web.multipart.MultipartFile;


/**
 * 部门管理后端服务模块
 *
 * @author 李洪文
 * @description
 * @date 2019/12/3 11:07
 */

@RestController
@RequestMapping("/api/department")
@BackendModule({"page:页面", "update:修改", "add:创建", "delete:删除"})
public class DepartmentController {
    @Autowired
    private DepartmentService departmentService;

    @PostMapping("listDepartment")
    @Privilege("page")
    public Page<DepartmentVO> listDepartment(@RequestBody DepartmentQueryDTO queryDTO) {
        return departmentService.listByPage(queryDTO);
    }

    @PostMapping("addDepartment")
    @Privilege("add")
    public Integer addDepartment(@RequestBody DepartmentDTO departmentDTO) {
        return departmentService.addDepartment(departmentDTO);
    }

    @PostMapping("updateDepartment")
    @Privilege("update")
    public Integer updateDepartment(@RequestBody DepartmentDTO departmentDTO) {
        return departmentService.updateDepartment(departmentDTO);
    }

    @GetMapping("getDepartment")
    @Privilege("update")
    public DepartmentDTO getDepartment(Integer id) {
        return departmentService.getById(id);
    }

    @PostMapping("deleteDepartment")
    @Privilege("delete")
    public void deleteDepartment(@RequestBody List<Integer> ids) {
        departmentService.deleteByCodes(ids);
    }

    @PostMapping("exportDepartment")
    @Privilege("page")
    public void exportDepartment(@RequestBody DepartmentQueryDTO queryDTO, HttpServletResponse response) throws Exception {
        Workbook workbook = departmentService.export(queryDTO);
        response.setContentType("application/vnd.ms-excel");
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd$HHmmss");
        response.addHeader("Content-Disposition", "attachment;filename=file" + sdf.format(new Date()) + ".xls");
        OutputStream os = response.getOutputStream();
        workbook.write(os);
        os.close();
        workbook.close();
    }
    @PostMapping("importDepartment")
    @Privilege("add")
    public int importUsers(@RequestParam("file") MultipartFile file) throws Exception {
        return departmentService.importDepartment(file.getInputStream(), file.getOriginalFilename());
    }

}
