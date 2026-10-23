package Model;

import lombok.*;
import java.io.Serializable;

@Getter
@AllArgsConstructor
public class LoggedUser implements Serializable {
    private int id;
    private String name;
    private String email;
    private String type;
    private Integer regionTypeId;
}