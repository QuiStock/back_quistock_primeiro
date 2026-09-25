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

    //doGet pra ver qual das duas ações vai fazer
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException{
        String action = request.getParameter("action");
        if (action == null) action = "lojasList";

        //switch e cases pra caso for editar, deletar ou listar
        switch (action){
            case "editar":
                showUpdateForm(request, response); //aparecer a interface pra editar
                break;
            case "deletar":
                deleteLoja(request, response);
                break;
            default:
                readLojas(request, response);
        }
    }

    //doPost pra alterar tabela, seja criando ou editando
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException{
        String action = request.getParameter("action");

        switch (action){
            case "cadastrar":
                createLoja(request, response);
                break;
            case "editar":
                updateLoja(request, response); //agora sim, editando a loja
                break;
        }
    }

    //metodo pra listar as lojas
    private void readLojas(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException{
        try {
            List<Loja> lojasList = lojaDAO.readLojas();

            request.setAttribute("lojasList", lojasList); //action do form
            RequestDispatcher dispatcher = request.getRequestDispatcher("lojas.jsp"); //onde vai redirecionar
            dispatcher.forward(request, response);
        } catch (SQLException e) {
            throw new ServletException("Erro ao buscar lojas", e);
        }
    }

    //metodo pra criar as lojas
    private void createLoja(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException{
        //pegando info do forms
        String email = request.getParameter("lojaEmail");
        String senha = request.getParameter("lojaPassword");

        //adicionando as info num objeto da loja
        Loja loja = new Loja();
        loja.setEmail(email);
        loja.setSenha(senha);

        //criando a loja nova no db
        try {
            lojaDAO.createLoja(loja);
            response.sendRedirect("lojas"); //redireciona pra lojas.jsp dnv
        }catch (SQLException e){
            throw new ServletException("Erro ao criar loja", e);
        }
    }

    //mostra o formulario pro usuario editar a loja
    private void showUpdateForm(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException{
        int codigo = Integer.parseInt(request.getParameter("lojaCode"));

        try {
            Loja loja = lojaDAO.foundLoja(codigo);
            request.setAttribute("loja", loja); // agora manda o objeto inteiro

            RequestDispatcher dispatcher = request.getRequestDispatcher("editarLoja.jsp");
            dispatcher.forward(request, response);
        } catch (SQLException e) {
            throw new ServletException("Erro ao buscar loja", e);
        }
    }


    //metodo pra atualizar a loja
    private void updateLoja(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException{
        Loja loja = new Loja();

        //definindo os valores novos da loja
        int codigo = Integer.parseInt(request.getParameter("lojaCode"));
        String email = request.getParameter("lojaEmail");
        String senha = request.getParameter("lojaPassword");

        loja.setCodigo(codigo);
        loja.setEmail(email);
        loja.setSenha(senha);

        //editando a loja
        try {
            lojaDAO.updateLoja(loja);
            response.sendRedirect("lojas");
        }catch (SQLException e){
            throw new ServletException("Erro ao atualizar loja", e);
        }
    }

    //metodo pra deletar loja
    private void deleteLoja(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException{
        int codigo = Integer.parseInt(request.getParameter("lojaCode"));

        try {
            lojaDAO.deleteLoja(codigo);
            response.sendRedirect("lojas");
        } catch (SQLException e) {
            throw new ServletException("Erro ao deletar loja", e);
        }
    }

}