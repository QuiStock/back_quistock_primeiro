package Controller;

import DAO.RegiaoDAO;
import Model.Regiao;
import jakarta.servlet.ServletException;
import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

//encaminha pra pagina regiao
@WebServlet("/regiao")
public class RegiaoServlet extends HttpServlet{

    private RegiaoDAO regiaoDAO = new RegiaoDAO();

    //doGet pra listar as Regioes
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException{
        try{
            List<Regiao> listaRegioes = regiaoDAO.read();

            request.setAttribute("listaRegioes", listaRegioes);

            RequestDispatcher dispatcher = request.getRequestDispatcher("regioes.jsp");//caminho pro jsp
            dispatcher.forward(request, response);
        }catch (SQLException e){
            throw new ServletException("Erro ao buscar as regioes no banco", e);
        }

    }

}
