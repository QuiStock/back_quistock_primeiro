package Model;

import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class Store {
    private int id;
    private String email;
    private String password;
    private int regionTypeId; //regiao ligada a loja no db
}
