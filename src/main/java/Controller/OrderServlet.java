package Controller;

import DAO.OrderDAO;
import DAO.ProductDAO;
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
    private final ProductDAO productDAO = new ProductDAO();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
        throws ServletException, IOException{
        req.setCharacterEncoding("UTF-8");

        try {

            req.setAttribute("itens", dao.select());
            req.setAttribute("produtos", productDAO.select());
            req.getRequestDispatcher("/WEB-INF/views/pedido.jsp").forward(req, resp);

        } catch (SQLException e) {
            throw new ServletException("Erro ao consultar pedidos", e);
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
                case "edit" -> update(req, resp);
                default -> resp.sendError(HttpServletResponse.SC_BAD_REQUEST, "Ação inválida");
            }
        } catch (IllegalArgumentException e) {
            req.setAttribute("erro", "Informe um produto e um valor total valido.");
            doGet(req, resp);
        } catch (SQLException e) {
            if ("23503".equals(e.getSQLState())) {
                req.setAttribute("erro", "Produto inexistente ou pedido com registros vinculados.");
                doGet(req, resp);
            } else {
                throw new ServletException("Erro ao salvar pedido", e);
            }
        }

    }

    private void add(HttpServletRequest req, HttpServletResponse resp)
        throws SQLException, IOException {
        Order order = new Order();

        order.setProductId(Integer.parseInt(req.getParameter("product-id")));
        String total = req.getParameter("total-order-price");
        if (total == null) {
            throw new IllegalArgumentException("Valor total nao informado");
        }
        order.setTotalOrderPrice(Double.parseDouble(total));


        validate(order);
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

    //confere os dados antes de salvar o pedido
    private void validate(Order order) throws SQLException {
        if (order.getProductId() <= 0 || productDAO.searchById(order.getProductId()) == null
                || !Double.isFinite(order.getTotalOrderPrice()) || order.getTotalOrderPrice() < 0) {
            throw new IllegalArgumentException("Dados invalidos");
        }
    }

    private void update(HttpServletRequest req, HttpServletResponse resp)
            throws SQLException, IOException {
        int id = Integer.parseInt(req.getParameter("id"));
        Order order = dao.searchById(id);
        if (order == null) {
            resp.sendError(HttpServletResponse.SC_NOT_FOUND, "Pedido nao encontrado");
            return;
        }
        order.setProductId(Integer.parseInt(req.getParameter("product-id")));
        String total = req.getParameter("total-order-price");
        if (total == null) {
            throw new IllegalArgumentException("Valor total nao informado");
        }
        order.setTotalOrderPrice(Double.parseDouble(total));
        validate(order);
        dao.update(order);
        resp.sendRedirect(req.getContextPath() + "/orders");
    }
}
