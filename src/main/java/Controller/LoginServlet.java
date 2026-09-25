package Controller;

import Service.LoginService;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.*;

import java.io.IOException;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor

@WebServlet("/login")
public class LoginServlet extends HttpServlet {

    private LoginService loginService = new LoginService();

    @Override
    protected void doPost(
            HttpServletRequest req,
            HttpServletResponse resp
    ) throws ServletException, IOException {

        String email = req.getParameter("email");
        String pass = req.getParameter("senha");

        

        try {

            boolean login = loginService.validaLogin(email, pass);

            if (login) {

                req.getRequestDispatcher("home-admin.jsp").forward(req, resp);

            }

            else  {

                req.getRequestDispatcher("login.jsp").forward(req, resp);

            }

        } catch(Exception e) {

            throw new ServletException(e);

        }

    }



}