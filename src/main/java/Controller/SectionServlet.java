package Controller;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

@WebServlet({"/faq", "/anotacoes"})
public class SectionServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String view = "/faq".equals(request.getServletPath()) ? "faq.jsp" : "anotacoes.jsp";
        request.getRequestDispatcher("/WEB-INF/views/" + view).forward(request, response);
    }
}
