package Controller;

import DAO.RegionalManagerDAO;
import Model.RegionalManager;
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
@WebServlet("/regionalManagers")
public class RegionalManagerServlet extends HttpServlet{

    private RegionalManagerDAO regionalManagerDAO = new RegionalManagerDAO();

    //doGet pra ver qual das duas ações vai fazer
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException{
        String action = request.getParameter("action");
        if (action == null) action = "regionalManagersList";

        //switch e cases pra caso for editar, deletar ou listar
        switch (action){
            case "edit":
                showUpdateForm(request, response); //aparecer a interface pra editar
                break;
            case "delete":
                deleteRegionalManager(request, response);
                break;
            default:
                readRegionalManagers(request, response);
        }
    }

    //doPost pra alterar tabela, seja criando ou editando
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException{
        String action = request.getParameter("action");
        if (action == null) action = "";

        switch (action){
            case "register":
                createRegionalManager(request, response);
                break;
            case "edit":
                updateRegionalManager(request, response); //agora sim, editando o gerente
                break;
        }
    }

    //metodo pra listar os gerentes
    private void readRegionalManagers(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException{
        try {
            List<RegionalManager> regionalManagersList = regionalManagerDAO.readRegionalManagers();

            request.setAttribute("regionalManagersList", regionalManagersList); //action do form
            RequestDispatcher dispatcher = request.getRequestDispatcher("regionalManagers.jsp"); //onde vai redirecionar
            dispatcher.forward(request, response);
        } catch (SQLException e) {
            throw new ServletException("Erro ao buscar gerentes", e);
        }
    }

    //metodo pra criar os gerentes
    private void createRegionalManager(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException{
        //pegando info do forms
        String name =  request.getParameter("regionalManagerName");
        String email = request.getParameter("regionalManagerEmail");
        String password = request.getParameter("regionalManagerPassword");
        int regionTypeId = Integer.parseInt(request.getParameter("regionalManagerRegionTypeId"));

        //adicionadno as info num objeto do gerente
        RegionalManager regionalManager = new RegionalManager();
        regionalManager.setName(name);
        regionalManager.setEmail(email);
        regionalManager.setPassword(password);
        regionalManager.setRegionTypeId(regionTypeId);

        //criando o gerente novo no db
        try {
            regionalManagerDAO.createRegionalManager(regionalManager);
            response.sendRedirect("regionalManagers"); //redireciona pra regionalManagers.jsp dnv
        }catch (SQLException e){
            throw new ServletException("Erro ao criar gerente", e);
        }
    }

    //mostra o formulario pro usuario editar o gerente
    private void showUpdateForm(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        int id = Integer.parseInt(request.getParameter("regionalManagerId"));

        try {
            RegionalManager regionalManager = regionalManagerDAO.foundRegionalManager(id);
            request.setAttribute("regionalManager", regionalManager); // agora manda o objeto inteiro
            RequestDispatcher dispatcher = request.getRequestDispatcher("editRegionalManager.jsp");
            dispatcher.forward(request, response);
        } catch (SQLException e) {
            throw new ServletException("Erro ao buscar gerente", e);
        }
    }

    private void updateRegionalManager(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException{
        RegionalManager regionalManager = new RegionalManager();

        //definindo os valores novos do gerente
        int id = Integer.parseInt(request.getParameter("regionalManagerId"));
        String name = request.getParameter("regionalManagerName");
        String email = request.getParameter("regionalManagerEmail");
        String password = request.getParameter("regionalManagerPassword");
        int regionTypeId = Integer.parseInt(request.getParameter("regionalManagerRegionTypeId"));

        regionalManager.setId(id);
        regionalManager.setName(name);
        regionalManager.setEmail(email);
        regionalManager.setPassword(password);
        regionalManager.setRegionTypeId(regionTypeId);

        //editando o gerente
        try {
            regionalManagerDAO.updateRegionalManager(regionalManager);
            response.sendRedirect("regionalManagers");
        }catch (SQLException e){
            throw new ServletException("Erro ao atualizar gerente", e);
        }
    }

    private void deleteRegionalManager(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException{
        int id = Integer.parseInt(request.getParameter("regionalManagerId"));

        try {
            regionalManagerDAO.deleteRegionalManager(id);
            response.sendRedirect("regionalManagers");
        } catch (SQLException e) {
            throw new ServletException("Erro ao deletar gerente", e);
        }
    }

}