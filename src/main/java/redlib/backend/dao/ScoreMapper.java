package redlib.backend.dao;
import redlib.backend.dto.AvgDTO;
import redlib.backend.dto.query.AvgQueryDTO;
import redlib.backend.dto.query.ScoreQueryDTO;

import redlib.backend.model.Avg;
import redlib.backend.model.Score;
import java.util.List;
import org.apache.ibatis.annotations.Param;


public interface ScoreMapper {

    // 通过ID删除成绩记录
    int deleteByPrimaryKey(Integer id);

    // 插入成绩记录
    int insert(Score record);

    // 选择性地插入成绩记录
    int insertSelective(Score record);

    // 通过ID查询成绩记录
    Score selectByPrimaryKey(Integer id);

    // 选择性地更新成绩记录
    int updateByPrimaryKeySelective(Score record);

    // 更新成绩记录
    int updateByPrimaryKey(Score record);

    // 根据学生ID查询成绩记录的数量
    Integer count(ScoreQueryDTO queryDTO);

    Integer countAvg(AvgQueryDTO queryDTO);

    // 根据学生ID查询成绩记录列表
    List<Score> list(@Param("queryDTO") ScoreQueryDTO queryDTO,
                                @Param("offset") Integer offset,
                                @Param("limit") Integer limit,
                                @Param("orderBy")String orderBy);
//    List<Student> list(@Param("queryDTO") StudentQueryDTO queryDTO,
//                       @Param("offset") Integer offset,
//                       @Param("limit") Integer limit);
    // 通过成绩记录ID列表批量删除成绩记录
    void deleteByIds(@Param("ids") List<Integer> ids);

    // 根据特定条件（例如：学期）查询成绩记录的列表
    // 需要根据实际条件添加参数
    List<Score> listByQuery(ScoreQueryDTO queryDTO);


    List<AvgDTO> getAvg(@Param("schoolYear") String schoolYear, @Param("semester") String semester);

    List<Avg> listAvg(@Param("queryDTO") AvgQueryDTO queryDTO,
                     @Param("offset") Integer offset,
                     @Param("limit") Integer limit,
                      @Param("orderBy")String orderBy);

}