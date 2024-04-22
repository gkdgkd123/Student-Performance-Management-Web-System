package redlib.backend.dto.query;

import lombok.Data;
import lombok.EqualsAndHashCode;


@EqualsAndHashCode(callSuper = true)
@Data
public class ClassQueryDTO extends PageQueryDTO {

    private String className;

    private Integer id;

}