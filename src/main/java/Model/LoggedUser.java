package Model;

import lombok.*;
import java.io.Serializable;

// Keep the identity and role needed across requests.
@Getter
@AllArgsConstructor
public class LoggedUser implements Serializable {
    private int id;
    private String name;
    private String email;
    private String type;
    private String regionType;
    private Integer regionTypeId;
}
