package redlib.backend.service.impl;

import org.apache.poi.ss.usermodel.Workbook;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;
import redlib.backend.dao.ClassMapper;
import redlib.backend.dto.ClassDTO;
import redlib.backend.dto.ClassDTO;
import redlib.backend.dto.ScoreDTO;
import redlib.backend.dto.query.ClassQueryDTO;
import redlib.backend.dto.query.ClassQueryDTO;
import redlib.backend.model.ClassModel;
import redlib.backend.model.Page;
import redlib.backend.model.Token;
import redlib.backend.service.ClassService;
import redlib.backend.service.utils.ClassUtils;
import redlib.backend.utils.PageUtils;
import redlib.backend.utils.ThreadContextHolder;
import redlib.backend.utils.XlsUtils;
import redlib.backend.vo.ClassVO;
import redlib.backend.vo.ClassVO;

import java.io.InputStream;
import java.util.*;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

@Service
public class ClassServiceImpl implements ClassService {

    @Autowired
    private ClassMapper classMapper;

    @Override
    public Page<ClassVO> listByPage(ClassQueryDTO queryDTO) {
        if (queryDTO == null) {
            queryDTO = new ClassQueryDTO();
        }
        Integer size = classMapper.count(queryDTO);
        PageUtils pageUtils = new PageUtils(queryDTO.getCurrent(), queryDTO.getPageSize(), size);

        if (size == 0) {
            return new Page<>(queryDTO.getCurrent(), queryDTO.getPageSize(), size, Collections.emptyList());
        }

        List<ClassModel> classModels = classMapper.list(queryDTO, pageUtils.getOffset(), pageUtils.getLimit());
        List<ClassVO> classVOS = classModels.stream()
                .map(this::convertToClassVO)
                .collect(Collectors.toList());

        return new Page<>(queryDTO.getCurrent(), queryDTO.getPageSize(), size, classVOS);
    }

    private ClassVO convertToClassVO(ClassModel classModel) {
        ClassVO classVO = new ClassVO();
        BeanUtils.copyProperties(classModel, classVO);
        // HINT: Populate additional properties in 'classVO' as required.
        return classVO;
    }

    @Override
    public ClassDTO getById(Integer id) {
        ClassModel classModel = classMapper.selectByPrimaryKey(id);
        if (classModel == null) {
            return null;
        }
        ClassDTO classDTO = new ClassDTO();
        BeanUtils.copyProperties(classModel, classDTO);
        return classDTO;
    }

    @Override
    public Integer addClass(ClassDTO classDTO) {
        Token token = ThreadContextHolder.getToken();
        System.out.println(classDTO);
        // 校验输入数据正确性
        ClassUtils.validateClass(classDTO);
        // 创建实体对象，用以保存到数据库
        ClassModel classModel = new ClassModel();
        // 将输入的字段全部复制到实体对象中
        BeanUtils.copyProperties(classDTO, classModel);

        // 调用DAO方法保存到数据库表
        classMapper.insert(classModel);
        return classModel.getId();
    }


    @Override
    public void deleteByIds(List<Integer> codes) {
        classMapper.deleteByIds(codes);
    }


    @Override
    public Integer updateClass(ClassDTO classDTO) {
        // 校验输入数据正确性
        Token token = ThreadContextHolder.getToken();
//        ClassUtils.validateClass(classDTO);
        Assert.notNull(classDTO.getId(), "班级id不能为空");
        ClassModel classes = classMapper.selectByPrimaryKey(classDTO.getId());
        Assert.notNull(classes, "没有找到班级，Id为：" + classDTO.getId());

        BeanUtils.copyProperties(classDTO, classes);
        classMapper.updateByPrimaryKey(classes);
        return classes.getId();
    }

    @Override
    public Workbook export(ClassQueryDTO queryDTO) {
        queryDTO.setPageSize(100);
        Map<String, String> map = new LinkedHashMap<>();
        map.put("id", "班级ID");
        map.put("className", "班级名称");


        final AtomicBoolean finalPage = new AtomicBoolean(false);
        Workbook workbook = XlsUtils.exportToExcel(page -> {
            if (finalPage.get()) {
                return null;
            }
            queryDTO.setCurrent(page);
            List<ClassVO> list = listByPage(queryDTO).getList();
            if (list.size() != 100) {
                finalPage.set(true);
            }
            return list;
        }, map);

        return workbook;
    }


    @Override
    public int importClass(InputStream inputStream, String fileName) throws Exception {
        Assert.hasText(fileName, "文件名不能为空");

        Map<String, String> map = new LinkedHashMap<>();

        map.put("班级名称","className" );
        AtomicInteger row = new AtomicInteger(0);
        XlsUtils.importFromExcel(inputStream, fileName, (classDTO) -> {
            System.out.println("here"+classDTO);
            addClass(classDTO);
            row.incrementAndGet();
        }, map, ClassDTO.class);

        return row.get();
    }



}