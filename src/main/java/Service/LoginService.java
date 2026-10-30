package Service;

import Model.LoggedUser;
import lombok.*;

import DAO.AdminDAO;
import DAO.RegionalManagerDAO;

import Model.Admin;
import Model.RegionalManager;

import java.sql.SQLException;
import java.util.List;

@Setter
@NoArgsConstructor
public class LoginService {

    private AdminDAO daoAdm = new AdminDAO();
    private RegionalManagerDAO daoMan = new RegionalManagerDAO();

    public LoginResult validateLogin(String email, String password) throws SQLException, ClassNotFoundException {

        RegionalManager manager = daoMan.searchByEmail(email);
        if (manager != null && PasswordService.verify(password, manager.getPassword())) {
            // Upgrade legacy passwords after a successful login.
            if (PasswordService.needsMigration(manager.getPassword())) {
                manager.setPassword(PasswordService.genHash(password));
                daoMan.update(manager);
            }

            return new LoginResult(

                    true,

                        new LoggedUser(

                                manager.getId(),
                                manager.getName(),
                                manager.getEmail(),
                                "MANAGER",
                                manager.getRegionTypeName(),
                                manager.getRegionTypeId()

                        )

            );
        }

        Admin admin = daoAdm.searchByEmail(email);
        if (admin != null && PasswordService.verify(password, admin.getPassword())) {
            // Apply the same migration to admin accounts.
            if (PasswordService.needsMigration(admin.getPassword())) {
                admin.setPassword(PasswordService.genHash(password));
                daoAdm.update(admin);
            }

            return new LoginResult(

                    true,

                    new LoggedUser(

                            admin.getId(),
                            admin.getName(),
                            admin.getEmail(),
                            "ADMIN",
                            null,
                            null
                    )

            );
        }

        return new LoginResult(false, null);
    }
}
