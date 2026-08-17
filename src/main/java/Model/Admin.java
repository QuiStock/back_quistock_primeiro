package Model;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Admin {
    int id;
    String email;
    String senha;
    String nome;
}
