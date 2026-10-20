package Controller;

import DAO.AdminDAO;
import Model.Admin;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.sql.SQLException;

@WebServlet("/admins")
public class AdminServlet extends HttpServlet {

    AdminDAO dao = new AdminDAO();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        String action = req.getParameter("action");

        try {

            if (action.equals("new")){
                //empty insert forms
                req.getRequestDispatcher("crud-admin.jsp").forward(req, resp);

            }

            else if (action.equals("edit")){
                //forms filled with previous admin data
                int id = Integer.parseInt(req.getParameter("id"));
                Admin admin = dao.searchAdmin(id);

                if (admin != null) {
                    resp.sendError(HttpServletResponse.SC_NOT_FOUND, "Admin não encontrado");
                }

                //filled form
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

            String action = req.getParameter("action");

        if(action == null){
            resp.sendError(HttpServletResponse.SC_BAD_REQUEST, "Invalid action");
            return;
        }

        try {
            switch (action) {

                case "delete": delete(req, resp); break;
                case "add":

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
}
