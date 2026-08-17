package Controller;

import DAO.GerenteDAO;
import Model.Gerente;
import jakarta.servlet.ServletException;
import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

//encaminha pra pagina gerentes
@WebServlet("/gerentes")
public class GerenteServlet extends HttpServlet{

    private GerenteDAO gerenteDAO = new GerenteDAO();

    //doGet pra listar os Gerentes
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException{
        try{
            List<Gerente> listaGerentes = gerenteDAO.read();

            request.setAttribute("listaGerentes", listaGerentes);

            RequestDispatcher dispatcher = request.getRequestDispatcher("gerentes.jsp");//caminho pro jsp
            dispatcher.forward(request, response);
        }catch (SQLException e){
            throw new ServletException("Erro ao buscar os gerentes no banco", e);
        }

    }

}
