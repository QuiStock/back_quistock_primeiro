package Controller;

import DAO.RegionTypeDAO;
import DAO.RegionalManagerDAO;
import Model.RegionalManager;
import Service.PasswordService;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.SQLException;

@WebServlet("/regionalManagers")
public class RegionalManagerServlet extends HttpServlet{

    private final RegionalManagerDAO dao = new RegionalManagerDAO();
    private final RegionTypeDAO regionDAO = new RegionTypeDAO();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        String action = req.getParameter("action");

        try {

            if ("new".equals(action)){
                // Load region choices for the form.
                req.setAttribute("regionTypes", regionDAO.select());
                req.getRequestDispatcher("/crud-managers.jsp").forward(req, resp);
            }

            else if ("edit".equals(action)) {
                int id = Integer.parseInt(req.getParameter("id"));
                RegionalManager manager = dao.searchById(id);

                if (manager == null) {
                    resp.sendError(HttpServletResponse.SC_NOT_FOUND, "Manager not found");
                    return;
                }

                req.setAttribute("manager", manager);
                req.setAttribute("regionTypes", regionDAO.select());
                req.getRequestDispatcher("/crud-managers.jsp").forward(req, resp);
            }

            else {
                req.setAttribute("managers", dao.select());
                req.getRequestDispatcher("/regional-managers.jsp").forward(req, resp);
            }

        } catch (SQLException ex) {

            resp.sendError(500, ex.getMessage());

        }

    }


    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException, ServletException {

        // Read submitted names as UTF-8.
        req.setCharacterEncoding("UTF-8");

        String action = req.getParameter("action"); 

        try {
            switch (action) {
                case "add":
                    add(req, resp);
                    break;
                case "edit":
                    update(req, resp);
                    break;
                case "delete":
                    delete(req, resp);
                    break;
                default:
                    doGet(req, resp);
                    break;
            }

        } catch (NumberFormatException e){
            if ("add".equals(action) || "edit".equals(action)) {
                showValidationError(req, resp, "Selecione uma região válida.");
            } else {
                resp.sendError(HttpServletResponse.SC_BAD_REQUEST, "ID Inválido");
            }
        } catch (SQLException e){
            resp.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, e.getMessage());
        } catch (IOException e){
            resp.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, e.getMessage());
        }
    }

    private void add (HttpServletRequest req, HttpServletResponse resp) throws SQLException, ServletException, IOException{
        RegionalManager manager = new RegionalManager();
        String email = req.getParameter("email");
        String password = req.getParameter("password");
        String name = req.getParameter("name");

        if (!validEmail(email)) {
            showValidationError(req, resp, "Informe um email válido (até 320 caracteres).");
            return;
        }
        if (!validPassword(password)) {
            showValidationError(req, resp, "A senha deve ter de 8 a 50 caracteres: letras, números, ponto ou sublinhado.");
            return;
        }
        if(!validName(name)){
            showValidationError(req, resp, "Informe nome e sobrenome com letras e até 60 caracteres.");
            return;
        }

        manager.setEmail(email.trim());
        manager.setName(name.trim());
        manager.setPassword(PasswordService.genHash(password.trim()));

        int regionTypeId = Integer.parseInt(req.getParameter("region-type"));

        if (regionDAO.searchById(regionTypeId) == null) {
            showValidationError(req, resp, "Selecione uma região válida.");
            return;
        }

        manager.setRegionTypeId(regionTypeId);

        dao.insert(manager);

        resp.sendRedirect(req.getContextPath() + "/regionalManagers");
    }

    private void update (HttpServletRequest req, HttpServletResponse resp)
            throws SQLException, ServletException, IOException{

        int regionTypeId = Integer.parseInt(req.getParameter("region-type"));

        if (regionDAO.searchById(regionTypeId) == null) {
            showValidationError(req, resp, "Selecione uma região válida.");
            return;
        }
        int id = Integer.parseInt(req.getParameter("id"));

        RegionalManager manager = dao.searchById(id);



        if (manager == null) {
            resp.sendError(HttpServletResponse.SC_NOT_FOUND, "Admin não encontrado");
            return;
        }

        String email = req.getParameter("email");
        String password = req.getParameter("password");
        String name = req.getParameter("name");

        if (!validEmail(email)) {
            showValidationError(req, resp, "Informe um email válido (até 320 caracteres).");
            return;
        }

        if (!validName(name)) {
            showValidationError(req, resp, "Informe nome e sobrenome com letras e até 60 caracteres.");
            return;
        }

        manager.setName(name.trim());
        manager.setEmail(email.trim());
        manager.setRegionTypeId(regionTypeId);

        if (password != null && !password.trim().isEmpty()) {
            if (!validPassword(password)) {
                showValidationError(req, resp, "A senha deve ter de 8 a 50 caracteres: letras, números, ponto ou sublinhado.");
                return;
            }

            manager.setPassword(PasswordService.genHash(password.trim()));
        }

        dao.update(manager);

        resp.sendRedirect(req.getContextPath() + "/regionalManagers");
    }

    private void delete (HttpServletRequest req, HttpServletResponse resp)
            throws SQLException, ServletException,  IOException{

        int id = Integer.parseInt(req.getParameter("id"));
        dao.delete(id);

        resp.sendRedirect(req.getContextPath() + "/regionalManagers");
    }

    private boolean validEmail(String email) {
        if (email == null) {
            return false;
        }

        String regex = "^[A-Za-z0-9À-ÿ._%+-]+@[A-Za-z0-9À-ÿ-]+\\.[A-Za-z0-9À-ÿ.-]+$";

        return email.trim().length() <= 320 && email.trim().matches(regex);
    }

    private boolean validPassword(String pass){
        if (pass == null){
            return false;
        }

        String regex = "^[a-zA-Z0-9._]{8,50}$";

        return pass.trim().matches(regex);
    }

    private boolean validName(String name){

        if(name == null){
            return false;
        }

        String regex = "^[a-zA-ZÀ-ÿ]+(\s+[a-zA-ZÀ-ÿ]+)+$"; // Require at least two words in the name.

        return name.trim().length() <= 60 && name.trim().matches(regex);

    }

    private void showValidationError(HttpServletRequest req, HttpServletResponse resp, String message)
            throws ServletException, IOException {
        req.setAttribute("erro", message);
        try {
            req.setAttribute("managers", dao.select());
            req.getRequestDispatcher("/regional-managers.jsp").forward(req, resp);
        } catch (SQLException e) {
            throw new ServletException("Erro ao buscar gerentes", e);
        }
    }
}
