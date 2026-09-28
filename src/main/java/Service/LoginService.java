package Service;

import lombok.*;

import DAO.AdminDAO;
import DAO.GerenteDAO;

import Model.Admin;
import Model.RegionalManager;

import java.sql.SQLException;
import java.util.List;

@NoArgsConstructor
public class LoginService {

    private AdminDAO daoAdm = new AdminDAO();
    private GerenteDAO daoGer = new GerenteDAO();

    private RegionalManager gerente = new RegionalManager();
    private Admin admin = new Admin();

    public LoginService(RegionalManager gerente) {
        this.gerente = gerente;
    }

    public LoginService(Admin admin) {
        this.admin = admin;
    }

    public RegionalManager validaLogin(String email, String senha) throws SQLException, ClassNotFoundException {

        List<RegionalManager> gerentes = daoGer.read();

        for (RegionalManager ger : gerentes) {
            if (ger.getEmail().equals(email) && ger.getSenha().equals(senha)) {
                return true;
            }
        }

        return false;

    }
}
