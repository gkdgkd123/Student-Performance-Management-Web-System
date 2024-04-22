package redlib.backend.dto;


import lombok.Data;
import java.util.Date;

@Data
public class ScoreDTO {
    private Integer id;
    private String studentId;         // 学生ID
    private Integer chineseScore;       // 语文成绩
    private Integer mathScore;          // 数学成绩
    private Integer englishScore;       // 英语成绩
    private Date entryDate;            // 录入日期
    private String semester;           // 学期
    private String schoolYear;
    private Double avgScore;
    private String studentName;
    private String studentNumber;
    private String className;
    private Integer classId;
}