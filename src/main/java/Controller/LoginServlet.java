package Controller;

import DAO.AdminDAO;
import DAO.GerenteDAO;
import jakarta.servlet.annotation.WebServlet;

import Model.Gerente;
import Model.Admin;
import lombok.*;

import java.sql.SQLException;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor

@WebServlet("/login")
public class LoginServlet {

    AdminDAO daoA = new AdminDAO();
    GerenteDAO daoG = new GerenteDAO();

    public boolean validate(String user, String password) throws SQLException, ClassNotFoundException {

        List<Admin> admins = daoA.select();
        List<Gerente> gerentes = daoG.read();

        for (Admin admin : admins) {

            if (admin.getEmail().equals(user) && admin.getSenha().equals(password)){

                return true;

            }

        }

        for (Gerente gerente : gerentes) {
            if (gerente.getEmail().equals(user) && gerente.getSenha().equals(password)){

                return true;

            }
        }

        return false;

    }
}
