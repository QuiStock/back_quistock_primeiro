package Controller;

import DAO.OrderDAO;
import Model.Order;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.sql.SQLException;

@WebServlet("/orders")
public class OrderServlet extends HttpServlet {
    private final OrderDAO dao = new OrderDAO();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
        throws ServletException, IOException{
        req.setCharacterEncoding("UTF-8");

        String action = req.getParameter("action");

        try {

            if ("add".equals(action)) {
                req.getRequestDispatcher("/crud-order.jsp").forward(req, resp);
            } else {
                req.setAttribute("orders", dao.select());
                req.getRequestDispatcher("/WEB-INF/views/pedido.jsp").forward(req, resp);
            }

        } catch (SQLException e) {
            resp.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, e.getMessage());
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        req.setCharacterEncoding("UTF-8");

        String action = req.getParameter("action");

        if (action == null) {
            resp.sendError(HttpServletResponse.SC_BAD_REQUEST, "Ação não informada");
            return;
        }
        try {
            switch (action) {
                case "add" -> add(req, resp);
                case "delete" -> delete(req, resp);
                default -> resp.sendError(HttpServletResponse.SC_BAD_REQUEST, "Ação inválida");
            }
        } catch (SQLException e) {
            resp.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, e.getMessage());
        }

    }

    private void add(HttpServletRequest req, HttpServletResponse resp)
        throws SQLException, IOException {
        Order order = new Order();

        order.setProductName(req.getParameter("product-name"));
        order.setTotalOrderPrice(Double.parseDouble(req.getParameter("total-order-price")));


        dao.insert(order);

        resp.sendRedirect(req.getContextPath() + "/orders");
    }

    private void delete(HttpServletRequest req, HttpServletResponse resp)
        throws SQLException, IOException {
        Order order = new Order();

        order.setId(Integer.parseInt(req.getParameter("id")));

        dao.delete(order);

        resp.sendRedirect(req.getContextPath() + "/orders");
    }
}
