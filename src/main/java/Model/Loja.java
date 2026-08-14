package Model;
import lombok.*;
@Getter
@Setter

public class Loja {
    private int id;
    private String email;
    private String senha;
    private String endereco_id;
    private String regiao_id;

    public Loja(){
    }

    public Loja(int id, String email, String senha, String endereco_id, String regiao_id) {
        this.id = id;
        this.email = email;
        this.senha = senha;
        this.endereco_id = endereco_id;
        this.regiao_id = regiao_id;
    }
}
