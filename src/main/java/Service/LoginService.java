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
        if (manager != null && manager.getPassword().equals(password)) {
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

        
        for (Admin adm : admins) {
            if (adm.getEmail().equals(email) && adm.getPassword().equals(password)) {

                LoginResult result = new LoginResult(true, "ADMIN");

                return result;
            }
        }

        return null;
    }
}