package Model;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
public class Endereco {
    private int id;
    private String cep;
    private String pais;
    private String rua;
    private int numero;
    private String estado;
    private String cidade;
}