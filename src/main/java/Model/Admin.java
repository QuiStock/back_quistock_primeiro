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
}
