package Model;
import lombok.*;

// The region name is populated for store listings.
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Store {
    private int id;
    private String email;
    private String password;
    private int regionTypeId;
    private String regionName;
}
