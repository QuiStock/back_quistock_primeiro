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
    private String regionTypeName;
    private int regionTypeId;

    public  RegionalManager(String email, String name, String password, int regionTypeId) {
        this.email = email;
        this.name = name;
        this.password = password;
        this.regionTypeId = regionTypeId;
    }

    //nome usado pela tela de gerentes
    public String getNome() {
        return name;
    }
}
