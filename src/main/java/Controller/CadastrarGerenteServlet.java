package Controller;

import DAO.GerenteDAO;
import Model.Gerente;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

@WebServlet("/cadastrarGerente")
public class CadastrarGerenteServlet extends HttpServlet{

    private GerenteDAO gerenteDAO = new GerenteDAO();

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException{

        //define as variáveis conforme o usuario colocou
        String nome = request.getParameter("nome");
        String email = request.getParameter("email");
        String senha = request.getParameter("senha");

        Gerente gerente = new Gerente();
        gerente.setNome(nome);
        gerente.setEmail(email);
        gerente.setSenha(senha);

        //salva o gerente no banco de dados
        try{
            gerenteDAO.create(gerente);
            response.sendRedirect("gerentes");// manda devolta pra pagina dos gerentes

        } catch (SQLException e){
            throw new ServletException("Erro ao criar gerente", e);
        }

    }

}
