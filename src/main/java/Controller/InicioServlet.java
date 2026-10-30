package Controller;

import Model.LoggedUser;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

@WebServlet("/inicio")
public class InicioServlet extends HttpServlet {

    @Override
    // Handles do, Get
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        // Applying the method logic, using the provided values
        // Show the signed-in users name on the home page.
        LoggedUser user = (LoggedUser) request.getSession().getAttribute("loggedUser");
        request.setAttribute("nome", user.getName());
        request.getRequestDispatcher("/WEB-INF/views/inicio.jsp").forward(request, response);
    }
}
