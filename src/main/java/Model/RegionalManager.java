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
    private String regionType;

    public  RegionalManager(String email, String name, String password, String regionTypeId) {
        this.email = email;
        this.name = name;
        this.password = password;
        this.regionType = regionTypeId;
    }
}