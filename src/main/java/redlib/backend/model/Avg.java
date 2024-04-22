package redlib.backend.model;

import lombok.Data;

@Data
public class Avg {
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

