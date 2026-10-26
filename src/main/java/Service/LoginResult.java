package Service;

import Model.LoggedUser;
import lombok.*;

@Getter
@AllArgsConstructor
public class LoginResult {
    private boolean success;
    private LoggedUser loggedUser;
}
