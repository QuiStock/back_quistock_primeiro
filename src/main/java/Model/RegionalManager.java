package Model;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class RegionalManager {
    private int id;
    private String email;
    private String name;
    private String password;
    private int regionTypeId;
}