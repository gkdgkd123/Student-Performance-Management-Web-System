package redlib.backend.service;

import org.apache.poi.ss.usermodel.Workbook;
import redlib.backend.dto.AvgDTO;
import redlib.backend.dto.ScoreDTO;
import redlib.backend.dto.StudentDTO;
import redlib.backend.dto.query.AvgQueryDTO;
import redlib.backend.dto.query.ClassQueryDTO;
import redlib.backend.dto.query.ScoreQueryDTO;
import redlib.backend.dto.query.StudentQueryDTO;
import redlib.backend.model.Page;
import redlib.backend.model.Score;
import redlib.backend.vo.AvgVO;
import redlib.backend.vo.ScoreVO;

import java.io.InputStream;
import java.util.List;

/**
 * 成绩模块服务接口
 *
 */
public interface ScoreService {
    int countScoresByStudentId(Integer studentId);
    Page<ScoreVO> listByPage(ScoreQueryDTO queryDTO);

    /**
     * 新建成绩
     *
     * @param scoreDTO 成绩输入对象
     * @return 成绩ID
     */
    Integer addScore(ScoreDTO scoreDTO);

    /**
     * 根据ID获取成绩数据
     *
     * @param id 成绩ID
     * @return 成绩传输对象
     */
    ScoreDTO getById(Integer id);

    /**
     * 更新成绩数据
     *
     * @param scoreDTO 成绩输入对象
     * @return 成绩ID
     */
    Integer updateScore(ScoreDTO scoreDTO);

    /**
     * 根据ID列表，批量删除成绩
     *
     * @param ids ID列表
     */
    void deleteByIds(List<Integer> ids);

    /**
     * 根据学生ID和其他条件导出成绩数据到Excel（如果需要的话）
     *
     * @param studentId 学生ID
     * @param otherConditions 其他过滤条件
     * @return 工作簿Workbook对象，用于Excel导出（如果需要的话）
     */
    Workbook export(ScoreQueryDTO queryDTO);

    /**
     * 根据特定条件查询成绩列表
     *
     * @param conditions 查询条件
     * @param offset 分页查询起始位置
     * @param limit 分页查询限制数量
     * @return 符合条件的成绩列表
     */
//    List<ScoreVO> listByCondition(Object conditions, int offset, int limit);

    Page<ScoreDTO> listScoresByQuery(ScoreQueryDTO queryDTO);

    List<AvgDTO> calculateAverageScores(String schoolYear, String semester);

    int importScore(InputStream inputStream, String fileName) throws Exception;

    Page<AvgVO> listAvgScore(AvgQueryDTO queryDTO);


}