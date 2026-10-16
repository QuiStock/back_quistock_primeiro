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
    protected void doPost(
            HttpServletRequest req,
            HttpServletResponse resp
    ) throws ServletException, IOException {

            String action = req.getParameter("action");

        if(action == null){
            resp.sendError(HttpServletResponse.SC_BAD_REQUEST, "Invalid action");
            return;
        }

            switch (action) {

                case "add":
                    try {

                        Admin admin = new Admin(
                                Integer.parseInt(req.getParameter("id")),
                                req.getParameter("email"),
                                req.getParameter("name")
                        );

                        dao.insert(admin);

                    } catch (SQLException e){
                        resp.sendError(400, e.getMessage());
                    }

                    break;
/*
                case "update":
                    try {



                    } catch (SQLException e){
                        resp.sendError(HttpServletResponse.SC_BAD_REQUEST, e.getMessage());
                    }*/
            }
    }
}
