package redlib.backend.controller;

import jakarta.servlet.http.HttpServletResponse;
import org.apache.poi.ss.usermodel.Workbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import redlib.backend.annotation.Privilege;
import redlib.backend.dto.ClassDTO;

import redlib.backend.dto.query.ClassQueryDTO;
import redlib.backend.dto.query.ClassQueryDTO;
import redlib.backend.model.Page;
import redlib.backend.service.ClassService;
import redlib.backend.vo.ClassVO;


import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

@RestController
@RequestMapping("/api/klass")
public class KlassController {
    @Autowired
    private ClassService classService;

    @PostMapping("/listClass")
    public Page<ClassVO> listClass(@RequestBody ClassQueryDTO queryDTO) {
        return classService.listByPage(queryDTO);
    }

    @PostMapping("/addClass")
    public Integer addClass(@RequestBody ClassDTO classDTO) {
        return classService.addClass(classDTO);
    }

    @GetMapping("/getClass")
    public ClassDTO getClass(@RequestParam Integer id) {
        return classService.getById(id);
    }

    @PostMapping("/deleteClass")
    public void deleteClass(@RequestBody List<Integer> ids) {
        classService.deleteByIds(ids);
    }

    @PostMapping("/updateClass")
    @Privilege("update")
    public Integer updateClass(@RequestBody ClassDTO classDTO) {
        return classService.updateClass(classDTO);
    }

    @PostMapping("exportClass")
    @Privilege("page")
    public void exportClass(@RequestBody ClassQueryDTO queryDTO, HttpServletResponse response) throws Exception {
        Workbook workbook = classService.export(queryDTO);
        response.setContentType("application/vnd.ms-excel");
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd$HHmmss");
        response.addHeader("Content-Disposition", "attachment;filename=file" + sdf.format(new Date()) + ".xls");
        OutputStream os = response.getOutputStream();
        workbook.write(os);
        os.close();
        workbook.close();
    }

    @PostMapping("importClass")
    @Privilege("add")
    public int importUsers(@RequestParam("file") MultipartFile file) throws Exception {
        return classService.importClass(file.getInputStream(), file.getOriginalFilename());
    }


}