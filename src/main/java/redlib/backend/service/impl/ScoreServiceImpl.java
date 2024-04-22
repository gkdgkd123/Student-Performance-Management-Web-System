package redlib.backend.service.impl;

import org.apache.poi.ss.usermodel.Workbook;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;
import redlib.backend.dao.ScoreMapper;
import redlib.backend.dto.AvgDTO;
import redlib.backend.dto.ScoreDTO;
import redlib.backend.dto.query.AvgQueryDTO;
import redlib.backend.dto.query.ScoreQueryDTO;
import redlib.backend.model.Avg;
import redlib.backend.model.Page;
import redlib.backend.model.Score;
import redlib.backend.service.ScoreService;
import redlib.backend.service.utils.ScoreUtils;
import redlib.backend.utils.PageUtils;
import redlib.backend.utils.XlsUtils;
import redlib.backend.vo.AvgVO;
import redlib.backend.vo.ScoreVO;

import java.io.InputStream;
import java.util.*;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;



@Service
public class ScoreServiceImpl implements ScoreService {
    @Autowired
    private ScoreMapper scoreMapper;

    @Override
    public Page<ScoreVO> listByPage(ScoreQueryDTO queryDTO) {
        if (queryDTO == null) {
            queryDTO = new ScoreQueryDTO();
        }
        Integer size = scoreMapper.count(queryDTO);
        PageUtils pageUtils = new PageUtils(queryDTO.getCurrent(), queryDTO.getPageSize(), size);

        if (size == 0) {
            return new Page<>(queryDTO.getCurrent(), queryDTO.getPageSize(), size, Collections.emptyList());
        }

        List<Score> scoreModels = scoreMapper.list(queryDTO, pageUtils.getOffset(), pageUtils.getLimit(),queryDTO.getOrderBy());
        List<ScoreVO> scoreVOS = scoreModels.stream()
                .map(this::convertToScoreVO)
                .collect(Collectors.toList());

        return new Page<>(queryDTO.getCurrent(), queryDTO.getPageSize(), size, scoreVOS);
    }

    private ScoreVO convertToScoreVO(Score score) {
        ScoreVO scoreVO = new ScoreVO();
        BeanUtils.copyProperties(score, scoreVO);
        return scoreVO;
    }

    @Override
    public Integer addScore(ScoreDTO scoreDTO) {
        ScoreUtils.validateScore(scoreDTO);
        Score score = new Score();
        System.out.println(scoreDTO);
        BeanUtils.copyProperties(scoreDTO, score);
        score.setEntryDate(new Date()); // 假设成绩录入日期为当前日期
        scoreMapper.insert(score);
        return score.getId();
    }

    @Override
    public ScoreDTO getById(Integer id) {
        Score score = scoreMapper.selectByPrimaryKey(id);
        if (score == null) {
            return null;
        }
        ScoreDTO scoreDTO = new ScoreDTO();
        BeanUtils.copyProperties(score, scoreDTO);
        return scoreDTO;
    }

    @Override
    public Integer updateScore(ScoreDTO scoreDTO) {
        Score score = scoreMapper.selectByPrimaryKey(scoreDTO.getId());
        if (score == null) {
            return null;
        }
        BeanUtils.copyProperties(scoreDTO, score);
        score.setEntryDate(new Date()); // 假设更新时成绩录入日期为当前日期
        scoreMapper.updateByPrimaryKey(score);
        return score.getId();
    }

    @Override
    public void deleteByIds(List<Integer> ids) {
        scoreMapper.deleteByIds(ids);
    }

    @Override
    public int countScoresByStudentId(Integer studentId) {
        // 实现统计学生得分数量的逻辑
        return 0;
    }

    @Override
    public Workbook export(ScoreQueryDTO queryDTO) {
        queryDTO.setPageSize(100);
        Map<String, String> map = new LinkedHashMap<>();
//        map.put("id", "成绩ID");
//        map.put("studentId", "学生ID");
        map.put("studentNumber", "学号");
        map.put("studentName", "姓名");
        map.put("className", "班级名称");
        map.put("chineseScore", "语文成绩");
        map.put("mathScore", "数学成绩");
        map.put("englishScore", "英语成绩");
        map.put("avgScore", "平均成绩");
        map.put("entryDate", "录入日期");
        map.put("semester", "学期");
        map.put("schoolYear", "学年");

        final AtomicBoolean finalPage = new AtomicBoolean(false);
        Workbook workbook = XlsUtils.exportToExcel(page -> {
            if (finalPage.get()) {
                return null;
            }
            queryDTO.setCurrent(page);
            List<ScoreVO> list = listByPage(queryDTO).getList();
            if (list.size() != 100) {
                finalPage.set(true);
            }
            return list;
        }, map);

        return workbook;
    }


    @Override
    public Page<ScoreDTO> listScoresByQuery(ScoreQueryDTO queryDTO) {
        // 添加必要的业务逻辑，比如校验查询参数等

        // 模拟实现，您需要按照您项目的具体情况来实现这部分逻辑
        // 调用 StudentMapper 的方法

        List<Score> scores = scoreMapper.listByQuery(queryDTO);
        // 转换实体列表为 DTO 列表
        List<ScoreDTO> scoreDTOs = convertScoreListToDTOs(scores);
        System.out.println(scores);
        System.out.println(scoreDTOs);
        // 获取总数用于分页
        int totalCount = scoreMapper.count(queryDTO);
        // 创建分页对象并返回
        return new Page<>(queryDTO.getCurrent(), queryDTO.getPageSize(), totalCount,scoreDTOs);
    }


    private List<ScoreDTO> convertScoreListToDTOs(List<Score> scores) {
        List<ScoreDTO> scoreDTOs = new ArrayList<>();
        for (Score score : scores) {
            ScoreDTO dto = new ScoreDTO();
            dto.setId(score.getId());
            dto.setStudentId(score.getStudentId());
            dto.setChineseScore(score.getChineseScore());
            dto.setMathScore(score.getMathScore());
            dto.setEnglishScore(score.getEnglishScore());
            dto.setEntryDate(score.getEntryDate());
            dto.setSemester(score.getSemester());
            dto.setSchoolYear(score.getSchoolYear());

            scoreDTOs.add(dto);
        }
        return scoreDTOs;
    }

    @Override
    public List<AvgDTO> calculateAverageScores(String schoolYear, String semester) {
        return scoreMapper.getAvg(schoolYear, semester);
    }

    @Override
    public int importScore(InputStream inputStream, String fileName) throws Exception {
        Assert.hasText(fileName, "文件名不能为空");

        Map<String, String> map = new LinkedHashMap<>();
        map.put("姓名", "studentName");
        map.put("学号", "studentNumber");
        map.put("语文成绩", "chineseScore");
        map.put("数学成绩", "mathScore");
        map.put("英语成绩", "englishScore");
        map.put("学年", "schoolYear");
        map.put("学期", "semester");


        AtomicInteger row = new AtomicInteger(0);
        XlsUtils.importFromExcel(inputStream, fileName, (scoreDTO) -> {
            addScore(scoreDTO);
            row.incrementAndGet();
        }, map, ScoreDTO.class);

        return row.get();
    }


    @Override
    public Page<AvgVO> listAvgScore(AvgQueryDTO queryDTO) {
        System.out.println("Impl : "+queryDTO);
        if (queryDTO == null) {
            queryDTO = new AvgQueryDTO();
        }
        Integer size = scoreMapper.countAvg(queryDTO);
        PageUtils pageUtils = new PageUtils(queryDTO.getCurrent(), queryDTO.getPageSize(), size);

        if (size == 0) {
            return new Page<>(queryDTO.getCurrent(), queryDTO.getPageSize(), size, Collections.emptyList());
        }

        List<Avg> scoreModels = scoreMapper.listAvg(queryDTO, pageUtils.getOffset(), pageUtils.getLimit(),queryDTO.getOrderBy());
        List<AvgVO> scoreVOS = scoreModels.stream()
                .map(this::convertToAvgVO)
                .collect(Collectors.toList());
        System.out.println("scoreVOS : "+scoreVOS);

        return new Page<>(queryDTO.getCurrent(), queryDTO.getPageSize(), size, scoreVOS);
    }

    private AvgVO convertToAvgVO(Avg score) {
        AvgVO scoreVO = new AvgVO();
        BeanUtils.copyProperties(score, scoreVO);
        return scoreVO;
    }

}