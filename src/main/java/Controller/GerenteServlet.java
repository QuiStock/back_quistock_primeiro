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

    //doGet pra ver qual das duas ações vai fazer
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException{
        String action = request.getParameter("action");
        if (action == null) action = "gerentesList";

        //switch e cases pra caso for editar, deletar ou listar
        switch (action){
            case "editar":
                showUpdateForm(request, response); //aparecer a interface pra editar
                break;
            case "deletar":
                deleteGerente(request, response);
                break;
            default:
                readGerentes(request, response);
        }
    }

    //doPost pra alterar tabela, seja criando ou editando
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException{
        String action = request.getParameter("action");

        switch (action){
            case "cadastrar":
                createGerente(request, response);
                break;
            case "editar":
                updateGerente(request, response); //agora sim, editando o gerente
                break;
        }
    }

    //metodo pra listar os gerentes
    private void readGerentes(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException{
        try {
            List<Gerente> gerentesList = gerenteDAO.read();

            request.setAttribute("gerentesList", gerentesList); //action do form
            RequestDispatcher dispatcher = request.getRequestDispatcher("gerentes.jsp"); //onde vai redirecionar
            dispatcher.forward(request, response);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    //metodo pra criar os gerentes
    private void createGerente(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException{
        //pegando info do forms
        String nome =  request.getParameter("gerenteName");
        String email = request.getParameter("gerenteEmail");
        String senha = request.getParameter("gerentePassword");
        int regiao_codigo = Integer.parseInt(request.getParameter("gerenteRegiaoCodigo"));

        //adicionadno as info num objeto do gerente
        Gerente gerente = new Gerente();
        gerente.setNome(nome);
        gerente.setEmail(email);
        gerente.setSenha(senha);
        gerente.setRegiao_codigo(regiao_codigo);

        //criando o gerente novo no db
        try {
                gerenteDAO.create(gerente);
                response.sendRedirect("gerentes"); //redireciona pra gerentes.jsp dnv
        }catch (Exception e){
            throw new RuntimeException(e);
        }
    }

    //mostra o formulario pro usuario editar o gerente
    private void showUpdateForm(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException{
        int codigo = Integer.parseInt(request.getParameter("gerenteCode"));

        request.setAttribute("gerenteCode", codigo); //manda o codigo pro JSP

        RequestDispatcher dispatcher = request.getRequestDispatcher("editarGerente.jsp");
        dispatcher.forward(request, response);
    }

    //

    private void updateGerente(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException{
        Gerente gerente = new Gerente();

        //definindo os valores novos do gerente
        int codigo = Integer.parseInt(request.getParameter("gerenteCode"));
        String nome = request.getParameter("gerenteName");
        String email = request.getParameter("gerenteEmail");
        String senha = request.getParameter("gerentePassword");
        int regiao_codigo = Integer.parseInt(request.getParameter("gerenteRegiaoCodigo"));

        gerente.setCodigo(codigo);
        gerente.setNome(nome);
        gerente.setEmail(email);
        gerente.setSenha(senha);
        gerente.setRegiao_codigo(regiao_codigo);

        //editando o gerente
        try {
            gerenteDAO.update(gerente);
            response.sendRedirect("gerentes");
        }catch (Exception e){
            throw new RuntimeException(e);
        }
    }

    private void deleteGerente(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException{
        int codigo = Integer.parseInt(request.getParameter("gerenteCode"));

        Gerente gerente = new Gerente();
        gerente.setCodigo(codigo);

        try {
            gerenteDAO.delete(gerente);
            response.sendRedirect("gerentes");
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

}
