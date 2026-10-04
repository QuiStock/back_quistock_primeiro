package Controller;

import Service.LoginResult;
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

    //Método para redirecionamento de acordo com o tipo de login e seu sucesso
    @Override
    protected void doPost(
            HttpServletRequest req,
            HttpServletResponse resp
    ) throws ServletException, IOException {

        String email = req.getParameter("email");
        String pass = req.getParameter("senha");

        

        try {

            LoginResult login = loginService.validateLogin(email, pass);

            if (login==null) {

                req.getRequestDispatcher("login.jsp").forward(req, resp);

            }

            else if (login.isSuccess() && login.getType().equals("ADMIN")) {

                req.getRequestDispatcher("home-admin.jsp").forward(req, resp);

            }

            else if(login.isSuccess() && login.getType().equals("MANAGER")) {
                req.getRequestDispatcher("home-manager.jsp").forward(req, resp);
            }

        } catch(Exception e) {

            throw new ServletException(e);

        }

    }



}