package Model;
import lombok.*;
@Getter
@Setter

public class Gerente {
    private int id;
    private String email;
    private String nome;
    private String senha;

    public Gerente(){

    }

    public Gerente(int id, String email, String nome, String senha) {
        this.id = id;
        this.email = email;
        this.nome = nome;
        this.senha = senha;
    }
}
