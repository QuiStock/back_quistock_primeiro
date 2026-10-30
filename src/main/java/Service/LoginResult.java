package Service;

import Model.LoggedUser;
import lombok.*;

// Carry the login outcome and the user identity together.
@Getter
@AllArgsConstructor
public class LoginResult {
    private boolean success;
    private LoggedUser loggedUser;
}
