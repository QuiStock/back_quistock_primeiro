package Model;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class RegionType {
    private int id;
    private String name;
    // The JSP uses this property name for region labels.
    public String getNome() {
        return name;
    }
}
