package Controller;

import DAO.StoreDAO;
import DAO.RegionTypeDAO;
import Model.Store;
import Service.PasswordService;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.sql.SQLException;

@WebServlet("/stores")
public class StoreServlet extends HttpServlet {

    private final StoreDAO storeDAO = new StoreDAO();
    private final RegionTypeDAO regionTypeDAO = new RegionTypeDAO();

    @Override
    // Load stores and region choices for the page.
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        try {
            request.setAttribute("storesList", storeDAO.readStores());
            request.setAttribute("regioes", regionTypeDAO.select());
            request.getRequestDispatcher("/WEB-INF/views/lojas.jsp").forward(request, response);
        } catch (SQLException e) {
            throw new ServletException("Erro ao buscar lojas", e);
        }
    }

    @Override
    // Handles do, Post
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        // Applying the method logic, using the provided values
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
            if ("register".equals(action) || "edit".equals(action)) {
                response.sendRedirect(request.getContextPath() + "/stores?erro=regiao");
            } else {
                response.sendError(HttpServletResponse.SC_BAD_REQUEST, "ID inválido");
            }
        } catch (SQLException e) {
            throw new ServletException("Erro ao salvar loja", e);
        }
    }

    // Handles create, Store
    private void createStore(HttpServletRequest request, HttpServletResponse response) throws SQLException, ServletException, IOException {
        // Applying the method logic, using the provided values
        String email = request.getParameter("storeEmail");
        String password = request.getParameter("storePassword");
        String regionTypeId = request.getParameter("regionTypeId");

        if (!validEmail(email)) {
            showValidationError(request, response, "email");
            return;
        }
        if (!validPassword(password)) {
            showValidationError(request, response, "senha");
            return;
        }

        Store store = new Store();
        store.setEmail(email.trim());
        store.setPassword(PasswordService.genHash(password.trim()));
        store.setRegionTypeId(parseId(regionTypeId));
        if (regionTypeDAO.searchById(store.getRegionTypeId()) == null) {
            response.sendRedirect(request.getContextPath() + "/stores?erro=regiao");
            return;
        }
        storeDAO.createStore(store);
        response.sendRedirect(request.getContextPath() + "/stores");
    }

    // Keep the current password when the field is left blank.
    private void updateStore(HttpServletRequest request, HttpServletResponse response) throws SQLException, ServletException, IOException {
        int id = parseId(request.getParameter("storeId"));
        String email = request.getParameter("storeEmail");
        String regionTypeId = request.getParameter("regionTypeId");

        if (!validEmail(email)) {
            showValidationError(request, response, "email");
            return;
        }

        Store store = storeDAO.foundStore(id);
        if (store == null) {
            response.sendError(HttpServletResponse.SC_NOT_FOUND, "Loja não encontrada");
            return;
        }

        String password = request.getParameter("storePassword");
        store.setEmail(email.trim());
        store.setRegionTypeId(parseId(regionTypeId));
        if (regionTypeDAO.searchById(store.getRegionTypeId()) == null) {
            response.sendRedirect(request.getContextPath() + "/stores?erro=regiao");
            return;
        }

        if (password != null && !password.trim().isEmpty()) {
            if (!validPassword(password)) {
                showValidationError(request, response, "senha");
                return;
            }

            store.setPassword(PasswordService.genHash(password.trim()));
        }

        if (storeDAO.updateStore(store) == 0) {
            response.sendError(HttpServletResponse.SC_NOT_FOUND, "Loja não encontrada");
            return;
        }
        response.sendRedirect(request.getContextPath() + "/stores");
    }

    // Handles delete, Store
    private void deleteStore(HttpServletRequest request, HttpServletResponse response) throws SQLException, IOException {
        // Applying the method logic, using the provided values
        int id = parseId(request.getParameter("storeId"));
        if (storeDAO.deleteStore(id) == 0) {
            response.sendError(HttpServletResponse.SC_NOT_FOUND, "Loja não encontrada");
            return;
        }
        response.sendRedirect(request.getContextPath() + "/stores");
    }

    // Reject IDs that are missing, invalid, or nonpositive.
    private int parseId(String value) {
        int id = Integer.parseInt(value);
        if (id <= 0) {
            throw new NumberFormatException("ID inválido");
        }
        return id;
    }

    // Handles valid, Email
    private boolean validEmail(String email) {
        // Applying the method logic, using the provided values
        if (email == null) {
            return false;
        }

        String regex = "^[A-Za-z0-9À-ÿ._%+-]+@[A-Za-z0-9À-ÿ-]+\\.[A-Za-z0-9À-ÿ.-]+$";

        return email.trim().length() <= 320 && email.trim().matches(regex);
    }

    // Handles valid, Password
    private boolean validPassword(String pass){
        // Applying the method logic, using the provided values
        if (pass == null){
            return false;
        }

        String regex = "^[a-zA-Z0-9._]{8,50}$";

        return pass.trim().matches(regex);
    }

    // Handles show, Validation, Error
    private void showValidationError(HttpServletRequest request, HttpServletResponse response, String code)
            throws ServletException, IOException {
        // Applying the method logic, using the provided values
        request.setAttribute("erro", code);
        doGet(request, response);
    }

}
