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

@WebServlet("/editarGerente")
public class EditarGerenteServlet extends HttpServlet {

    private GerenteDAO gerenteDAO = new GerenteDAO();

    //doGet pra mostrar o gerente que o cara procurou
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        int id = Integer.parseInt(request.getParameter("id"));

        //try pra criar um objeto do gerente de acordo com o ID
        try {
            Gerente gerente = gerenteDAO.foundGerente(id);

            request.setAttribute("gerente", gerente);

            RequestDispatcher dispatcher = request.getRequestDispatcher("editarGerente.jsp");//local pra ser encaminhado
            dispatcher.forward(request, response);

        } catch (SQLException e) {
            throw new ServletException("Erro ao buscar produto", e);
        }
    }

    //doPost pra editar o gerente agr q ja sabe qual é
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {

        int id = Integer.parseInt(request.getParameter("id"));
        String nome = request.getParameter("nome");
        String email = request.getParameter("email");
        String senha = request.getParameter("senha");

        //cria um objeto com todas essas info novas
        Gerente gerente = new Gerente();
        gerente.setNome(nome);
        gerente.setEmail(email);
        gerente.setSenha(senha);

        //try pra atualizar o gerente do banco com o novo objeto q acabou de criar
        try {
            gerenteDAO.update(gerente, id);
            response.sendRedirect("gerentes");//direciona de lá para a tabela de produtos para nao atualizar denovo

        } catch (SQLException e) {
            throw new ServletException("Erro ao atualizar gerente", e);
        }
    }
}