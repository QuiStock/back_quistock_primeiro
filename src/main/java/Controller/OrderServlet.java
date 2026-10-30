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
    // Load orders and products for the page
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
        throws ServletException, IOException{
        // Read submitted text as UTF 8
        req.setCharacterEncoding("UTF-8");

        // Fetch both lists before rendering the page
        try {

            req.setAttribute("itens", dao.select());
            req.setAttribute("produtos", productDAO.select());
            req.getRequestDispatcher("/WEB-INF/views/pedido.jsp").forward(req, resp);

        } catch (SQLException e) {
            throw new ServletException("Erro ao consultar pedidos", e);
        }
    }

    @Override
    // Route order changes to the requested action
    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        // Read submitted text as UTF 8
        req.setCharacterEncoding("UTF-8");

        String action = req.getParameter("action");

        // Reject requests without an action
        if (action == null) {
            resp.sendError(HttpServletResponse.SC_BAD_REQUEST, "Ação não informada");
            return;
        }
        // Run the selected action and handle validation or database errors
        try {
            switch (action) {
                case "add" -> add(req, resp);
                case "delete" -> delete(req, resp);
                case "edit" -> update(req, resp);
                default -> resp.sendError(HttpServletResponse.SC_BAD_REQUEST, "Ação inválida");
            }
        } catch (IllegalArgumentException e) {
            req.setAttribute("erro", e instanceof NumberFormatException
                    ? "Informe valores numéricos válidos para produto e preço total." : e.getMessage());
            doGet(req, resp);
        } catch (SQLException e) {
            // Report missing or linked records to the user
            if ("23503".equals(e.getSQLState())) {
                req.setAttribute("erro", "Produto inexistente ou pedido com registros vinculados.");
                doGet(req, resp);
            } else {
                throw new ServletException("Erro ao salvar pedido", e);
            }
        }

    }

    // Create an order from the submitted values
    private void add(HttpServletRequest req, HttpServletResponse resp)
        throws SQLException, IOException {
        // Create the order and assign its product and total
        Order order = new Order();

        order.setProductId(readProductId(req));
        order.setTotalOrderPrice(readTotal(req));


        // Check the values before saving
        validate(order);
        dao.insert(order);

        resp.sendRedirect(req.getContextPath() + "/orders");
    }

    // Delete the selected order
    private void delete(HttpServletRequest req, HttpServletResponse resp)
        throws SQLException, IOException {
        // Create an order with the submitted identifier
        Order order = new Order();

        order.setId(Integer.parseInt(req.getParameter("id")));

        dao.delete(order);

        resp.sendRedirect(req.getContextPath() + "/orders");
    }

    // Validate the order before writing it to the database
    private void validate(Order order) throws SQLException {
        // Require an existing product
        if (order.getProductId() <= 0 || productDAO.searchById(order.getProductId()) == null) {
            throw new IllegalArgumentException("Selecione um produto válido.");
        }
        // Require a valid nonnegative total
        if (!Double.isFinite(order.getTotalOrderPrice()) || order.getTotalOrderPrice() < 0) {
            throw new IllegalArgumentException("Informe um preço total válido, maior ou igual a zero.");
        }
    }

    // Update the product and total of an existing order
    private void update(HttpServletRequest req, HttpServletResponse resp)
            throws SQLException, IOException {
        // Find the order before applying changes
        int id = Integer.parseInt(req.getParameter("id"));
        Order order = dao.searchById(id);
        // Return an error when the order does not exist
        if (order == null) {
            resp.sendError(HttpServletResponse.SC_NOT_FOUND, "Pedido nao encontrado");
            return;
        }
        // Assign and validate the submitted values
        order.setProductId(readProductId(req));
        order.setTotalOrderPrice(readTotal(req));
        validate(order);
        dao.update(order);
        resp.sendRedirect(req.getContextPath() + "/orders");
    }

    // Read the product identifier from the request
    private int readProductId(HttpServletRequest req) {
        // Convert the identifier and report invalid input
        try {
            return Integer.parseInt(req.getParameter("product-id"));
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException("Selecione um produto válido.");
        }
    }

    // Read the order total from the request
    private double readTotal(HttpServletRequest req) {
        // Convert the total and report invalid input
        try {
            return Double.parseDouble(req.getParameter("total-order-price"));
        } catch (NullPointerException | NumberFormatException e) {
            throw new IllegalArgumentException("Informe um preço total válido, maior ou igual a zero.");
        }
    }
}
