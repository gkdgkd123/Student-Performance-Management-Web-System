package redlib.backend.service.utils;

import org.springframework.beans.BeanUtils;
import org.springframework.util.Assert;
import redlib.backend.dto.ClassDTO;
import redlib.backend.model.ClassModel;
import redlib.backend.model.ClassModel;
import redlib.backend.utils.FormatUtils;
import redlib.backend.vo.ClassVO;

import java.util.Map;

/**
 * @author 李洪文
 * @description
 * @date 2019/12/3 9:35
 */
public class ClassUtils {
    /**
     * 规范并校验classDTO
     *
     * @param classDTO
     */
    public static void validateClass(ClassDTO classDTO) {
        FormatUtils.trimFieldToNull(classDTO);
        Assert.notNull(classDTO, "班级输入数据不能为空");
        Assert.hasText(classDTO.getClassName(), "班级名称不能为空");
    }

    /**
     * 将实体对象转换为VO对象
     *
     * @param class 实体对象
     * @param nameMap
     * @return VO对象
     */
    public static ClassVO convertToVO(ClassModel classModel, Map<Integer, String> nameMap) {
        ClassVO classVO = new ClassVO();
        BeanUtils.copyProperties(classModel, classVO);


        return classVO;
    }
}
