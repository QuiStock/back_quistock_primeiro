package Model;
import lombok.*;
@Getter
@Setter

public class Regiao {
    private int id;
    private String nome;
    private int gerente_regional_id;

    public Regiao(){
    }

    public Regiao(int id, String nome, int gerente_regional_id) {
        this.id = id;
        this.nome = nome;
        this.gerente_regional_id = gerente_regional_id;
    }
}
