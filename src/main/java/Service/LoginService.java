package Service;

import lombok.*;

import DAO.AdminDAO;
import DAO.GerenteDAO;

import Model.Admin;
import Model.Gerente;

import java.sql.SQLException;
import java.util.List;

@NoArgsConstructor
public class LoginService {

    private AdminDAO daoAdm = new AdminDAO();
    private GerenteDAO daoGer = new GerenteDAO();

    private Gerente gerente = new Gerente();
    private Admin admin = new Admin();

    public LoginService(Gerente gerente) {
        this.gerente = gerente;
    }

    public LoginService(Admin admin) {
        this.admin = admin;
    }

    public Gerente validaLogin(String email, String senha) throws SQLException, ClassNotFoundException {

        List<Gerente> gerentes = daoGer.read();

        for (Gerente ger : gerentes) {
            if (ger.getEmail().equals(email) && ger.getSenha().equals(senha)) {
                return true;
            }
        }

        return false;

    }
}
