package Model;
import lombok.*;
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter

public class Loja {
    private int id;
    private String email;
    private String senha;
    private String endereco_id;
    private String regiao_id;
}
