package Controller;

import DAO.RegiaoDAO;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.SQLException;

@WebServlet("/deletarRegiao")
public class DeletarRegiaoServlet extends HttpServlet{

    private RegiaoDAO regiaoDAO = new RegiaoDAO();

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException{

        int id = Integer.parseInt(request.getParameter("id"));

        //try pra apagar a regiao
        try{
            regiaoDAO.delete(id);
            response.sendRedirect("regioes");

        } catch (SQLException e){
            throw new ServletException("Erro ao deletar regiao", e);
        }

    }

}
