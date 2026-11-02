package Controller;

import Service.LoginAttemptService;
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
    // Handles do, Post
    protected void doPost(
            HttpServletRequest req,
            HttpServletResponse resp
    ) throws ServletException, IOException {
        String email = req.getParameter("email");
        String pass = req.getParameter("password");
        String ip = req.getRemoteAddr();

        if (email == null || pass == null) {
            resp.sendError(HttpServletResponse.SC_BAD_REQUEST);
            return;
        }

        //checks if user already attempted too many times
        if (LoginAttemptService.isBlocked(ip, email)) {
            req.setAttribute("error", "Muitas tentativas. Tente novamente em alguns minutos.");
            resp.setStatus(429);
            req.getRequestDispatcher("/login.jsp").forward(req, resp);
            return;
        }

        try {

            LoginResult login = loginService.validateLogin(email, pass);

            if (!login.isSuccess()) {
                LoginAttemptService.recordFailure(ip, email);
                req.setAttribute("error", "E-mail ou senha inválidos");
                req.getRequestDispatcher("/login.jsp").forward(req, resp);
                return;
            }

            LoginAttemptService.recordSuccess(ip, email);

            // ... o resto continua igual (invalidate da sessão antiga, nova sessão, redirect)

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
