package redlib.backend.service.utils;

import org.springframework.beans.BeanUtils;
import org.springframework.util.Assert;
import redlib.backend.dto.ScoreDTO;
import redlib.backend.model.Score;
import redlib.backend.utils.FormatUtils;
import redlib.backend.vo.ScoreVO;

import java.util.Map;

/**
 * @author 李洪文
 * @description
 * @date 2019/12/3 9:35
 */
public class ScoreUtils {
    /**
     * 规范并校验scoreDTO
     *
     * @param scoreDTO
     */
    public static void validateScore(ScoreDTO scoreDTO) {
        FormatUtils.trimFieldToNull(scoreDTO);
        Assert.notNull(scoreDTO, "成绩输入数据不能为空");
        Assert.hasText(String.valueOf(scoreDTO.getChineseScore()), "语文不能为空");
    }

    /**
     * 将实体对象转换为VO对象
     *
     * @param score 实体对象
     * @param nameMap
     * @return VO对象
     */
    public static ScoreVO convertToVO(Score score, Map<Integer, String> nameMap) {
        ScoreVO scoreVO = new ScoreVO();
        BeanUtils.copyProperties(score, scoreVO);
        return scoreVO;
    }
}