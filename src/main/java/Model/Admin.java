package Model;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Admin {
    private int id;
    private String email;
    private String senha;
    private String name;

    public Admin (String email, String senha, String name) {

        this.email = email;
        this.senha = senha;
        this.name = name;

    }
    private String password;
    private String name;
}
