package Controller;

import DAO.FaqDAO;
import Model.Faq;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.SQLException;

@WebServlet("/faq")
public class FaqServlet extends HttpServlet {

    private final FaqDAO dao = new FaqDAO();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        req.setCharacterEncoding("UTF-8");

        String action =  req.getParameter("action");

        try {
            if ("add".equals(action)) {
                req.setAttribute("faq", req.getParameter("faq"));
                req.getRequestDispatcher("/crud-faq.jsp").forward(req, resp);
            }

            else if ("edit".equals(action)) {
                int id = Integer.parseInt(req.getParameter("id"));
                Faq faq = dao.searchById(id);

                if (faq == null) {
                    resp.sendError(HttpServletResponse.SC_NOT_FOUND, "Faq not found");
                    return;
                }

                req.setAttribute("faq", faq);
                req.getRequestDispatcher("/WEB-INF/views/faq.jsp").forward(req, resp);
            }
        } catch (SQLException ex) {
            resp.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, ex.getMessage());
        }
    }

    private void add(HttpServletRequest req, HttpServletResponse resp)
            throws SQLException, IOException {

        Faq faq = new Faq();

        String question = req.getParameter("question");
        String answer = req.getParameter("answer");

        faq.setQuestion(question);
        faq.setAnswer(answer);

        dao.insert(faq);

        resp.sendRedirect(req.getContextPath() + "/faq");
    }

    private void delete(HttpServletRequest req, HttpServletResponse resp)
            throws SQLException, IOException {
        Faq faq = new Faq();
        faq.setId(Integer.parseInt(req.getParameter("id")));

        dao.delete(faq);

        resp.sendRedirect(req.getContextPath() + "/faq");
    }

    private  void update(HttpServletRequest req, HttpServletResponse resp)
            throws SQLException, IOException {
        Faq faq = new Faq();

        int id = Integer.parseInt(req.getParameter("id"));

        String question = req.getParameter("question");

        String answer = req.getParameter("answer");

        faq.setId(id);
        faq.setQuestion(question);
        faq.setAnswer(answer);

        dao.update(faq);

        resp.sendRedirect(req.getContextPath() + "/faq");
    }

}
