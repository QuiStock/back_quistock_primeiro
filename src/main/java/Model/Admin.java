package Model;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Admin {
    private int id;
    private String email;
    private String password;
    private String name;

    public Admin (int id, String email, String name) {

        this.id = id;
        this.email = email;
        this.name = name;

    }
}
