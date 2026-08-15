package Controller;

import DAO.LojaDAO;
import Model.Loja;
import jakarta.servlet.ServletException;
import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

//encaminha pra pagina lojas
@WebServlet("/lojas")
public class LojaServlet extends HttpServlet{

    private LojaDAO lojaDAO = new LojaDAO();

    //doGet pra listar as lojas
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException{
        try{
            List<Loja> listaLojas = lojaDAO.read();

            request.setAttribute("listaLojas", listaLojas);

            RequestDispatcher dispatcher = request.getRequestDispatcher("lojas.jsp");//caminho pro jsp
            dispatcher.forward(request, response);
        }catch (SQLException e){
            throw new ServletException("Erro ao buscar os gerentes no banco", e);
        }

    }

}
