package Controller;

import DAO.AdminDAO;
import Model.Admin;
import Service.PasswordService;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.sql.SQLException;

@WebServlet("/admins")
public class AdminServlet extends HttpServlet {

    private final AdminDAO dao = new AdminDAO();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        String action = req.getParameter("action");

        try {

            if ("new".equals(action)){
                req.getRequestDispatcher("crud-admin.jsp").forward(req, resp);

            }

            else if ("edit".equals(action)){
                int id = Integer.parseInt(req.getParameter("id"));
                Admin admin = dao.searchById(id);

                if (admin == null) {
                    resp.sendError(HttpServletResponse.SC_NOT_FOUND, "Admin not found");
                    return;
                }

                req.setAttribute("admin", admin);
                req.getRequestDispatcher("/crud-admin.jsp").forward(req, resp);
            }

            else {
                req.setAttribute("admins", dao.select());
                req.getRequestDispatcher("/admins.jsp").forward(req, resp);
            }

        } catch (SQLException ex) {

            resp.sendError(500, ex.getMessage());

        }

    }

    @Override
    protected void doPost(
            HttpServletRequest req,
            HttpServletResponse resp
    ) throws ServletException, IOException {

        req.setCharacterEncoding("UTF-8");

        String action = req.getParameter("action");

        if(action == null){
            resp.sendError(HttpServletResponse.SC_BAD_REQUEST, "Invalid action");
            return;
        }

        try {
            switch (action) {

                case "delete": delete(req, resp); break;
                case "update": update(req, resp); break;
                case "add": add(req, resp); break;

            }
        } catch (NumberFormatException e){
            resp.sendError(HttpServletResponse.SC_BAD_REQUEST, "ID Inválido");
        } catch (SQLException e){
            resp.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, e.getMessage());
        } catch (IOException e){
            resp.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, e.getMessage());
        }

    }

    private void delete (HttpServletRequest req, HttpServletResponse resp)
            throws SQLException, ServletException,  IOException{

        Admin admin = new Admin();
        admin.setId(Integer.parseInt(req.getParameter("id")));

        dao.delete(admin);

        resp.sendRedirect(req.getContextPath() + "/admins");
    }

    private void update (HttpServletRequest req, HttpServletResponse resp)
            throws SQLException, ServletException, IOException{

        int id = Integer.parseInt(req.getParameter("id"));
        Admin admin = dao.searchById(id);

        if (admin == null) {
            resp.sendError(HttpServletResponse.SC_NOT_FOUND, "Admin não encontrado");
            return;
        }

        String name = req.getParameter("name");
        String email = req.getParameter("email");
        if (!validName(name)) {
            showValidationError(req, resp, "Informe um nome válido com até 100 caracteres.");
            return;
        }
        if (!validEmail(email)) {
            showValidationError(req, resp, "Informe um email válido com até 150 caracteres.");
            return;
        }
        admin.setName(name.trim());
        admin.setEmail(email.trim());
        // Only replace the password hash when a new password was entered.
        String newPass = req.getParameter("password");
        if (newPass != null && !newPass.isBlank() && !newPass.equals(admin.getPassword())) {
            if (!validPassword(newPass)) {
                showValidationError(req, resp, "A senha deve ter de 8 a 50 caracteres: letras, números, ponto ou sublinhado.");
                return;
            }
            admin.setPassword(PasswordService.genHash(newPass.trim()));
        }

        dao.update(admin);

        resp.sendRedirect(req.getContextPath() + "/admins");
    }

    private void add (HttpServletRequest req, HttpServletResponse resp)
            throws SQLException, ServletException, IOException{
        String name = req.getParameter("name");
        String email = req.getParameter("email");
        String password = req.getParameter("password");
        if (!validName(name)) {
            showValidationError(req, resp, "Informe um nome válido com até 100 caracteres.");
            return;
        }
        if (!validEmail(email)) {
            showValidationError(req, resp, "Informe um email válido com até 150 caracteres.");
            return;
        }
        if (!validPassword(password)) {
            showValidationError(req, resp, "A senha deve ter de 8 a 50 caracteres: letras, números, ponto ou sublinhado.");
            return;
        }
        Admin admin = new Admin(
                email.trim(),
                name.trim(),
                PasswordService.genHash(password.trim())
        );

        dao.insert(admin);

        resp.sendRedirect(req.getContextPath() + "/admins");
    }

    private boolean validName(String name) {
        return name != null && name.trim().length() <= 100
                && name.trim().matches("^[\\p{L}]+(?:[ '\\u2019-][\\p{L}]+)*$");
    }

    private boolean validEmail(String email) {
        return email != null && email.trim().length() <= 150
                && email.trim().matches("^[A-Za-z0-9À-ÿ._%+-]+@[A-Za-z0-9À-ÿ-]+\\.[A-Za-z0-9À-ÿ.-]+$");
    }

    private boolean validPassword(String password) {
        return password != null && password.trim().matches("^[a-zA-Z0-9._]{8,50}$");
    }

    private void showValidationError(HttpServletRequest req, HttpServletResponse resp, String message)
            throws ServletException, IOException {
        req.setAttribute("erro", message);
        doGet(req, resp);
    }
}
