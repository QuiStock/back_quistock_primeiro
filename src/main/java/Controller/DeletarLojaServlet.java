package Controller;

import DAO.LojaDAO;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.SQLException;

@WebServlet("/deletarLoja")
public class DeletarLojaServlet extends HttpServlet{

    private LojaDAO lojaDAO = new LojaDAO();

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException{

        int id = Integer.parseInt(request.getParameter("id"));

        //try pra apagar a loja
        try{
            lojaDAO.delete(id);
            response.sendRedirect("lojas");

        } catch (SQLException e){
            throw new ServletException("Erro ao deletar loja", e);
        }

    }

}
