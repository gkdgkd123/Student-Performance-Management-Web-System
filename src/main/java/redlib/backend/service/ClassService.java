package redlib.backend.service;
import org.apache.poi.ss.usermodel.Workbook;
import redlib.backend.dto.ClassDTO;
import redlib.backend.dto.DepartmentDTO;
import redlib.backend.dto.query.ClassQueryDTO;
import redlib.backend.dto.query.DepartmentQueryDTO;
import redlib.backend.model.Page;
import redlib.backend.vo.ClassVO;

import java.io.InputStream;
import java.util.List;

public interface ClassService {
    Page<ClassVO> listByPage(ClassQueryDTO queryDTO);


    Integer addClass(ClassDTO classDTO);


    ClassDTO getById(Integer id);

    Integer updateClass(ClassDTO classDTO);


    Workbook export(ClassQueryDTO queryDTO);

    void deleteByIds(List<Integer> ids);

    int importClass(
            InputStream inputStream,
            String fileName) throws Exception;


}