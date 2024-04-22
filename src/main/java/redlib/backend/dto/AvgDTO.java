package redlib.backend.dto;

import lombok.Data;

@Data
public class AvgDTO {
    private Integer classId;
    private Double avgChineseScore;
    private Double avgMathScore;
    private Double avgEnglishScore;
    private String className;
    private String schoolYear;
    private String semester;
    private Double totalAvgScore;
    private Double passRate;
    private Double highScoreRate;
}
