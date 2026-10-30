package Controller;

import Service.LoginResult;
import Service.LoginService;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import lombok.*;

import java.io.IOException;
import java.sql.SQLException;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor

@WebServlet("/login")
public class LoginServlet extends HttpServlet {

    private LoginService loginService = new LoginService();

    // Send each successful login to the right page.
    @Override
    protected void doPost(
            HttpServletRequest req,
            HttpServletResponse resp
    ) throws ServletException, IOException {

        String email = req.getParameter("email");
        String pass = req.getParameter("password");



        try {

            LoginResult login = loginService.validateLogin(email, pass);

            if (!login.isSuccess()) {
                req.setAttribute("error", "Email ou senha inválidos");
                req.getRequestDispatcher("/index.jsp").forward(req, resp);
                return;
            }

            HttpSession old =  req.getSession(false);
            if (old != null)old.invalidate();

            HttpSession session = req.getSession(true);
            session.setAttribute("loggedUser", login.getLoggedUser());

            resp.sendRedirect(req.getContextPath() + "/inicio");

        } catch(SQLException e) {
            e.printStackTrace();
            resp.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR,  e.getMessage());
        } catch(ClassNotFoundException e) {
            e.printStackTrace();
            resp.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR,  e.getMessage());
        }

    }



}
