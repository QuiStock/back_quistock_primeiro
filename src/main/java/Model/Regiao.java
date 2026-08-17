package Model;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Regiao {
    private int id;
    private String nome;
    private int gerente_regional_id;
}
