package Model;

import lombok.*;

// Admin account details used by persistence and login.
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Admin {
    private int id;
    private String email;
    private String password;
    private String name;

    public Admin (String email, String name, String password) {

        this.email = email;
        this.name = name;
        this.password = password;

    }
}
