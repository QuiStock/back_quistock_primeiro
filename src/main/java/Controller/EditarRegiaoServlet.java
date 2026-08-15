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

@WebServlet("/editarRegiao")
public class EditarRegiaoServlet extends HttpServlet {

    private RegiaoDAO regiaoDAO = new RegiaoDAO();

    //doGet pra mostrar a regiao que o cara procurou
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        int id = Integer.parseInt(request.getParameter("id"));

        //try pra criar um objeto da regiao de acordo com o ID
        try {
            Regiao regiao = regiaoDAO.foundRegiao(id);

            request.setAttribute("regiao", regiao);

            RequestDispatcher dispatcher = request.getRequestDispatcher("editarRegiao.jsp");//local pra ser encaminhado
            dispatcher.forward(request, response);

        } catch (SQLException e) {
            throw new ServletException("Erro ao buscar regiao", e);
        }
    }

    //doPost pra editar a regiao agr q ja sabe qual é
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {

        int id = Integer.parseInt(request.getParameter("id"));
        String nome = request.getParameter("nome");
        int gerente_regional_id = Integer.parseInt(request.getParameter("gerente_regional_id"));

        //cria um objeto com todas essas info novas
        Regiao regiao = new Regiao();
        regiao.setNome(nome);
        regiao.setGerente_regional_id(gerente_regional_id);


        //try pra atualizar a regiao do banco com o novo objeto q acabou de criar
        try {
            regiaoDAO.update(regiao, id);
            response.sendRedirect("regiao");//direciona de lá para a tabela das regioes para nao atualizar denovo

        } catch (SQLException e) {
            throw new ServletException("Erro ao atualizar regiao", e);
        }
    }
}