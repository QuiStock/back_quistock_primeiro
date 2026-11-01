package Controller;

import DAO.OrderDAO;
import DAO.ProductDAO;
import Model.LoggedUser;
import Model.Order;
import Service.AccessService;
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
    //load orders and products for the page
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException{
        req.setCharacterEncoding("UTF-8");

        //fetch both lists before rendering the page
        try {

            Integer scope = AccessService.scope(AccessService.currentUser(req));
            req.setAttribute("itens", dao.select(scope));
            req.setAttribute("produtos", productDAO.select(scope));
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
        LoggedUser user = AccessService.currentUser(req);

        // Reject requests without an action
        if (action == null) {
            resp.sendError(HttpServletResponse.SC_BAD_REQUEST, "Ação não informada");
            return;
        }
        try {
            switch (action) {
                case "add" -> add(req, resp, user);
                case "delete" -> delete(req, resp, user);
                case "edit" -> update(req, resp, user);
                default -> resp.sendError(HttpServletResponse.SC_BAD_REQUEST, "Ação inválida");
            }
        } catch (IllegalArgumentException e) {
            req.setAttribute("erro", e instanceof NumberFormatException
                    ? "Informe valores numéricos válidos para produto e preço total." : e.getMessage());
            doGet(req, resp);
        } catch (SQLException e) {
            //report missing or linked records to the user
            if ("23503".equals(e.getSQLState())) {
                req.setAttribute("erro", "Produto inexistente ou pedido com registros vinculados.");
                doGet(req, resp);
            } else {
                throw new ServletException("Erro ao salvar pedido", e);
            }
        }

    }

    private void add(HttpServletRequest req, HttpServletResponse resp, LoggedUser user)
            throws SQLException, IOException {
        Order order = new Order();

        order.setProductId(readProductId(req));
        order.setTotalOrderPrice(readTotal(req));


        //check the values before saving
        validate(order, user);
        dao.insert(order);

        resp.sendRedirect(req.getContextPath() + "/orders");
    }

    private void delete(HttpServletRequest req, HttpServletResponse resp, LoggedUser user)
            throws SQLException, IOException {
        int id = Integer.parseInt(req.getParameter("id"));

        //only delete orders that exist and belong to the user's region
        Order existing = dao.searchById(id);
        if (existing == null || !AccessService.canAccessProduct(user, existing.getProductId())) {
            resp.sendError(HttpServletResponse.SC_NOT_FOUND, "Pedido não encontrado");
            return;
        }

        Order order = new Order();
        order.setId(id);

        dao.delete(order);

        resp.sendRedirect(req.getContextPath() + "/orders");
    }

    //validate the order before writing it to the database
    private void validate(Order order, LoggedUser user) throws SQLException {
        //require an existing product
        if (order.getProductId() <= 0 || !AccessService.canAccessProduct(user, order.getProductId())) {
            throw new IllegalArgumentException("Selecione um produto válido.");
        }
        //require a valid nonnegative total
        if (!Double.isFinite(order.getTotalOrderPrice()) || order.getTotalOrderPrice() < 0) {
            throw new IllegalArgumentException("Informe um preço total válido, maior ou igual a zero.");
        }
    }

    //update the product and total of an existing order
    private void update(HttpServletRequest req, HttpServletResponse resp, LoggedUser user)
            throws SQLException, IOException {

        //find the order before applying changes
        int id = Integer.parseInt(req.getParameter("id"));
        Order order = dao.searchById(id);

        //return an error when the order does not exist
        if (order == null || !AccessService.canAccessProduct(user, order.getProductId())) {
            resp.sendError(HttpServletResponse.SC_NOT_FOUND, "Pedido não encontrado");
            return;
        }

        order.setProductId(readProductId(req));
        order.setTotalOrderPrice(readTotal(req));
        validate(order, user);
        dao.update(order);
        resp.sendRedirect(req.getContextPath() + "/orders");
    }

    private int readProductId(HttpServletRequest req) {
        try {
            return Integer.parseInt(req.getParameter("product-id"));
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException("Selecione um produto válido.");
        }
    }

    private double readTotal(HttpServletRequest req) {
        //convert the total and report invalid input
        try {
            return Double.parseDouble(req.getParameter("total-order-price"));
        } catch (NullPointerException | NumberFormatException e) {
            throw new IllegalArgumentException("Informe um preço total válido, maior ou igual a zero.");
        }
    }
}
