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

@WebServlet("/editarLoja")
public class EditarLojaServlet extends HttpServlet {

    private LojaDAO lojaDAO = new LojaDAO();

    //doGet pra mostrar a Loja que o cara procurou
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        int id = Integer.parseInt(request.getParameter("id"));

        //try pra criar um objeto da Loja de acordo com o ID
        try {
            Loja loja = lojaDAO.foundLoja(id);

            request.setAttribute("loja", loja);

            RequestDispatcher dispatcher = request.getRequestDispatcher("editarLoja.jsp");//local pra ser encaminhado
            dispatcher.forward(request, response);

        } catch (SQLException e) {
            throw new ServletException("Erro ao buscar Loja", e);
        }
    }

    //doPost pra editar a Loja agr q ja sabe qual é
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {

        int id = Integer.parseInt(request.getParameter("id"));
        String email = request.getParameter("email");
        String senha = request.getParameter("senha");
        String endereco_id = request.getParameter("endereco_id");
        String regiao_id = request.getParameter("regiao_id");

        //cria um objeto com todas essas info novas
        Loja loja = new Loja();
        loja.setEmail(email);
        loja.setSenha(senha);
        loja.setEndereco_id(endereco_id);
        loja.setRegiao_id(regiao_id);

        //try pra atualizar a loja do banco com o novo objeto q acabou de criar
        try {
            lojaDAO.update(loja, id);
            response.sendRedirect("lojas");//direciona de lá para a tabela de lojas para nao atualizar denovo

        } catch (SQLException e) {
            throw new ServletException("Erro ao atualizar loja", e);
        }
    }
}