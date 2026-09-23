package Model;

import lombok.*;
import Enum.TipoLogin;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Gerente {
    private int codigo;
    private String email;
    private String nome;
    private String senha;
    private TipoLogin tipoLogin;
}
