package Controller;

import DAO.StoreDAO;
import DAO.RegionTypeDAO;
import Model.Store;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.sql.SQLException;

//encaminha pra pagina de lojas
@WebServlet("/stores")
public class StoreServlet extends HttpServlet {

    private final StoreDAO storeDAO = new StoreDAO();
    private final RegionTypeDAO regionTypeDAO = new RegionTypeDAO();

    @Override
    //manda as lojas e as regioes pro formulario
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        try {
            request.setAttribute("storesList", storeDAO.readStores());
            request.setAttribute("regioes", regionTypeDAO.readRegionTypes());
            request.getRequestDispatcher("/WEB-INF/views/lojas.jsp").forward(request, response);
        } catch (SQLException e) {
            throw new ServletException("Erro ao buscar lojas", e);
        }
    }

    @Override
    //verifica qual ação veio do forms
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.setCharacterEncoding("UTF-8");
        String action = request.getParameter("action");

        if (action == null) {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Ação inválida");
            return;
        }

        try {
            switch (action) {
                case "register":
                    createStore(request, response);
                    break;
                case "edit":
                    updateStore(request, response);
                    break;
                case "delete":
                    deleteStore(request, response);
                    break;
                default:
                    response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Ação inválida");
            }
        } catch (NumberFormatException e) {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, "ID inválido");
        } catch (SQLException e) {
            throw new ServletException("Erro ao salvar loja", e);
        }
    }

    //cria a loja ligada a uma regiao que existe no db
    private void createStore(HttpServletRequest request, HttpServletResponse response) throws SQLException, IOException {
        String email = request.getParameter("storeEmail");
        String password = request.getParameter("storePassword");
        String regionTypeId = request.getParameter("regionTypeId");

        if (!validEmail(email) || password == null || password.trim().isEmpty()) {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Email ou senha inválidos");
            return;
        }

        Store store = new Store();
        store.setEmail(email.trim());
        store.setPassword(password);
        store.setRegionTypeId(parseId(regionTypeId));
        if (regionTypeDAO.foundRegionType(store.getRegionTypeId()) == null) {
            response.sendRedirect(request.getContextPath());
            return;
        }
        storeDAO.createStore(store);
        response.sendRedirect(request.getContextPath() + "/stores");
    }

    //atualiza a loja e mantem a senha se o campo ficar vazio
    private void updateStore(HttpServletRequest request, HttpServletResponse response) throws SQLException, IOException {
        int id = parseId(request.getParameter("storeId"));
        String email = request.getParameter("storeEmail");
        String regionTypeId = request.getParameter("regionTypeId");

        //usa metodo pra validar email e retorna o erro
        if (!validEmail(email)) {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Email inválido");
            return;
        }

        //define a loja e retorna erro caso esteja vazia
        Store store = storeDAO.foundStore(id);
        if (store == null) {
            response.sendError(HttpServletResponse.SC_NOT_FOUND, "Loja não encontrada");
            return;
        }

        //define as colunas da loja e retorna null errors
        String password = request.getParameter("storePassword");
        store.setEmail(email.trim());
        store.setRegionTypeId(parseId(regionTypeId));
        if (regionTypeDAO.foundRegionType(store.getRegionTypeId()) == null) {
            response.sendRedirect(request.getContextPath());
            return;
        }
        if (password != null){
            if (password.trim().isEmpty()) {
                response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Senha inválida");
                return;
            }
            store.setPassword(password);
        }

        if (storeDAO.updateStore(store) == 0) {
            response.sendError(HttpServletResponse.SC_NOT_FOUND, "Loja não encontrada");
            return;
        }
        response.sendRedirect(request.getContextPath() + "/stores");
    }

    //remove a loja e confere se o id existe
    private void deleteStore(HttpServletRequest request, HttpServletResponse response) throws SQLException, IOException {
        int id = parseId(request.getParameter("storeId"));
        if (storeDAO.deleteStore(id) == 0) {
            response.sendError(HttpServletResponse.SC_NOT_FOUND, "Loja não encontrada");
            return;
        }
        response.sendRedirect(request.getContextPath() + "/stores");
    }

    //nao deixa id vazio, negativo ou texto passar
    private int parseId(String value) {
        int id = Integer.parseInt(value);
        if (id <= 0) {
            throw new NumberFormatException("ID inválido");
        }
        return id;
    }

    //confere formato e tamanho do email antes de salvar
    private boolean validEmail(String email) {
        return email != null && email.length() <= 320
                && email.trim().matches("^[^\\s@]+@[^\\s@]+\\.[^\\s@]+$");
    }
}
