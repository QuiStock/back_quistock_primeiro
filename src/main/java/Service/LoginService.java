package Service;

import lombok.*;

import DAO.AdminDAO;
import DAO.GerenteDAO;

import Model.Admin;
import Model.Gerente;

import java.sql.SQLException;
import java.util.List;

@Getter
@Setter
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

    public boolean validaLogin(Admin admin) throws SQLException, ClassNotFoundException {

        List<Admin> admins = daoAdm.select();

        for (Admin adm : admins) {
            if (admin.getEmail().equals(adm.getEmail()) && admin.getSenha().equals(adm.getSenha())){
                return true;
            }
        }

        return false;

    }

    public boolean validaLogin(Gerente gerente) throws SQLException, ClassNotFoundException {

        List<Gerente> gerentes = daoGer.read();

        for (Gerente ger : gerentes) {
            if (admin.getEmail().equals(ger.getEmail()) && admin.getSenha().equals(ger.getSenha())){
                return true;
            }
        }

        return false;

    }
}
