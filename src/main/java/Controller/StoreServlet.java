package Controller;

import DAO.StoreDAO;
import Model.Store;
import jakarta.servlet.ServletException;
import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

//encaminha pra pagina stores
@WebServlet("/stores")
public class StoreServlet extends HttpServlet{

    private StoreDAO storeDAO = new StoreDAO();

    //doGet pra ver qual das duas ações vai fazer
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException{
        String action = request.getParameter("action");
        if (action == null) action = "storesList";

        //averigua qual action que é e redireciona para o servlet correto
        switch (action){
            case "edit":
                showUpdateForm(request, response); //aparecer a interface pra editar
                break;
            default:
                readStores(request, response);
        }
    }

    //doPost pra alterar tabela, seja criando, editando ou deletando
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {

        //traz pro teclado brasileiro
        request.setCharacterEncoding("UTF-8");

        String action = request.getParameter("action");
        if (action == null) action = "";

        //averigua qual action que é e redireciona para o servlet correto
        switch (action) {
            case "register":
                createStore(request, response);
                break;
            case "edit":
                updateStore(request, response); //editar de fato
                break;
            case "delete":
                deleteStore(request, response);
                break;
        }
    }

    //metodo pra listar as lojas
    private void readStores(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException{
        try {
            List<Store> storesList = storeDAO.readStores();

            request.setAttribute("storesList", storesList); //action do form
            RequestDispatcher dispatcher = request.getRequestDispatcher("stores.jsp"); //onde vai redirecionar
            dispatcher.forward(request, response);
        } catch (SQLException e) {
            throw new ServletException("Erro ao buscar lojas", e);
        }
    }

    //metodo pra criar as lojas
    private void createStore(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException{
        //pegando info do forms
        String email = request.getParameter("storeEmail");
        String password = request.getParameter("storePassword");

        //adicionando as info num objeto da loja
        Store store = new Store();
        store.setEmail(email);
        store.setPassword(password);

        //criando a loja nova no db
        try {
            storeDAO.createStore(store);
            response.sendRedirect("stores"); //redireciona pra stores.jsp dnv
        }catch (SQLException e){
            throw new ServletException("Erro ao criar loja", e);
        }
    }

    //mostra o formulario pro usuario editar a loja
    private void showUpdateForm(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException{
        int id = Integer.parseInt(request.getParameter("storeId"));

        try {
            Store store = storeDAO.foundStore(id);
            request.setAttribute("store", store); // agora manda o objeto inteiro

            RequestDispatcher dispatcher = request.getRequestDispatcher("editStore.jsp");
            dispatcher.forward(request, response);
        } catch (SQLException e) {
            throw new ServletException("Erro ao buscar loja", e);
        }
    }


    //metodo pra atualizar a loja
    private void updateStore(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException{
        Store store = new Store();

        //definindo os valores novos da loja
        int id = Integer.parseInt(request.getParameter("storeId"));
        String email = request.getParameter("storeEmail");
        String password = request.getParameter("storePassword");

        store.setId(id);
        store.setEmail(email);
        store.setPassword(password);

        //editando a loja
        try {
            storeDAO.updateStore(store);
            response.sendRedirect("stores");
        }catch (SQLException e){
            throw new ServletException("Erro ao atualizar loja", e);
        }
    }

    //metodo pra deletar loja
    private void deleteStore(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException{
        int id = Integer.parseInt(request.getParameter("storeId"));

        try {
            storeDAO.deleteStore(id);
            response.sendRedirect("stores");
        } catch (SQLException e) {
            throw new ServletException("Erro ao deletar loja", e);
        }
    }

}