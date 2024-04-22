package redlib.backend.dao;

import org.apache.ibatis.annotations.Param;
import redlib.backend.dto.query.ClassQueryDTO;

import redlib.backend.model.ClassModel;
import redlib.backend.model.Student;

import java.util.List;

/**
 * 部门数据访问组件
 *
 * @author 李洪文
 * @date 2019/11/14 10:38
 */
public interface ClassMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(ClassModel record);

    int insertSelective(ClassModel record);

    ClassModel selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(ClassModel record);

    int updateByPrimaryKey(ClassModel record);

    Integer count(ClassQueryDTO queryDTO);

    List<ClassModel> list(@Param("queryDTO") ClassQueryDTO queryDTO,
                       @Param("offset") Integer offset,
                       @Param("limit") Integer limit);

    /**
     * 根据ID列表批量删除学生记录
     *
     * @param ids 学生ID列表
     */
    void deleteByIds(@Param("ids") List<Integer> ids);




}