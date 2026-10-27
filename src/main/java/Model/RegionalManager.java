package Model;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class RegionalManager {
    private int id;
    private String email;
    private String name;
    private String password;
    private String regionTypeName;
    private int regionTypeId;
}