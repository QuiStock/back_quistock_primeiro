package Controller;

import DAO.GerenteDAO;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.SQLException;

@WebServlet("/deletarGerente")
public class DeletarGerenteServlet extends HttpServlet{

    private GerenteDAO gerenteDAO = new GerenteDAO();

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException{

        int id = Integer.parseInt(request.getParameter("id"));

        //try pra apagar o gerente
        try{
            gerenteDAO.delete(id);
            response.sendRedirect("gerentes");

        } catch (SQLException e){
            throw new ServletException("Erro ao deletar gerente", e);
        }

    }

}
