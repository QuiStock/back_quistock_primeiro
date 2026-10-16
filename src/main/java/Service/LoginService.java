package Service;

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

        List<RegionalManager> managers = daoMan.readRegionalManagers();

        for (RegionalManager man : managers) {

            if (man.getEmail().equals(email) && man.getPassword().equals(password)) {

                LoginResult result = new LoginResult(true, "MANAGER");

                return result;

            }

        }

        List<Admin> admins = daoAdm.select();

        for (Admin adm : admins) {
            if (adm.getEmail().equals(email) && adm.getPassword().equals(password)) {

                LoginResult result = new LoginResult(true, "ADMIN");

                return result;

            }
        }

        return null;
    }
}
