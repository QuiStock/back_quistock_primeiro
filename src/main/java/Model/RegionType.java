package Model;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class RegionType {
    private int id;
    private String name;
    //nome que o jsp usa pra mostrar a regiao
    public String getNome() {
        return name;
    }
}
