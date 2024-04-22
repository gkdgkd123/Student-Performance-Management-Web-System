package redlib.backend.dto.query;


import lombok.Data;
import lombok.EqualsAndHashCode;


@EqualsAndHashCode(callSuper = true)
@Data
public class AvgQueryDTO extends PageQueryDTO {

    private Integer classId;
    private Double avgChineseScore;
    private Double avgMathScore;
    private Double avgEnglishScore;
    private String className;
    private String schoolYear;
    private String semester;
    private String orderBy;
    private Double totalAvgScore;
    private Double passRate;
    private Double highScoreRate;
}