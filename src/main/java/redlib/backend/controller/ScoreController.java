package redlib.backend.controller;

import jakarta.servlet.http.HttpServletResponse;
import org.apache.poi.ss.usermodel.Workbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import redlib.backend.annotation.Privilege;
import redlib.backend.dto.AvgDTO;
import redlib.backend.dto.ScoreDTO;
import redlib.backend.dto.query.AvgQueryDTO;
import redlib.backend.dto.query.ScoreQueryDTO;
import redlib.backend.model.Page;
import redlib.backend.service.ScoreService;
import redlib.backend.vo.AvgVO;
import redlib.backend.vo.ScoreVO;

import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

@RestController
@RequestMapping("/api/score")
public class ScoreController {

    @Autowired
    private ScoreService scoreService;

    @PostMapping("listScore")
    public Page<ScoreVO> listScores(@RequestBody ScoreQueryDTO queryDTO) {
        return scoreService.listByPage(queryDTO);
    }

    @PostMapping("addScore")
    public Integer addScore(@RequestBody ScoreDTO scoreDTO) {
        return scoreService.addScore(scoreDTO);
    }

    @PostMapping("updateScore")
    public Integer updateScore(@RequestBody ScoreDTO scoreDTO) {
        return scoreService.updateScore(scoreDTO);
    }

    @GetMapping("getScore")
    public ScoreDTO getScore(@RequestParam Integer id) {
        return scoreService.getById(id);
    }

    @PostMapping("deleteScore")
    public void deleteScores(@RequestBody List<Integer> ids) {
        scoreService.deleteByIds(ids);
    }

    @PostMapping("exportScore")
    @Privilege("page")
    public void exportScores(@RequestBody ScoreQueryDTO queryDTO, HttpServletResponse response) throws Exception {
        Workbook workbook = scoreService.export(queryDTO);
        response.setContentType("application/vnd.ms-excel");
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
        String filename = String.format("scores_%s.xls", sdf.format(new Date()));
        response.addHeader("Content-Disposition", "attachment;filename=" + filename);
        OutputStream os = response.getOutputStream();
        workbook.write(os);
        os.flush();
        os.close();
        workbook.close();
    }

    @PostMapping("searchScores")
    @Privilege("page")
    public Page<ScoreDTO> getScores(@RequestBody ScoreQueryDTO queryDTO) {
        return scoreService.listScoresByQuery(queryDTO);
    }

    @PostMapping("/avgScores")
    @Privilege("page")
    public List<AvgDTO> getAverageScores(@RequestParam String schoolYear, @RequestParam String semester) {
        return scoreService.calculateAverageScores(schoolYear, semester);
    }

    @PostMapping("importScore")
    @Privilege("add")
    public int importUsers(@RequestParam("file") MultipartFile file) throws Exception {
        return scoreService.importScore(file.getInputStream(), file.getOriginalFilename());
    }

    @PostMapping("listAvgScore")
    public Page<AvgVO> listAvgScores(@RequestBody AvgQueryDTO queryDTO) {
        System.out.println("controller : "+queryDTO);
        return scoreService.listAvgScore(queryDTO);
    }

}