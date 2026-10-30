package Controller;

import DAO.SaleDAO;
import DAO.ProductDAO;
import Model.Sale;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.DateTimeException;

@WebServlet("/sales")
public class SaleServlet extends HttpServlet {
    private final SaleDAO saleDAO = new SaleDAO();
    private final ProductDAO productDAO = new ProductDAO();

    @Override
    // Handles do, Get
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        // Applying the method logic, using the provided values
        req.setCharacterEncoding("UTF-8");
        try {
            req.setAttribute("itens", saleDAO.select());
            req.setAttribute("produtos", productDAO.select());
            req.getRequestDispatcher("/WEB-INF/views/promocao.jsp").forward(req, resp);
        } catch (SQLException e) {
            throw new ServletException("Erro ao consultar promocoes", e);
        }
    }

    @Override
    // Handles do, Post
    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        // Applying the method logic, using the provided values
        req.setCharacterEncoding("UTF-8");
        String action = req.getParameter("action");
        if (action == null || !(action.equals("add") || action.equals("edit") || action.equals("delete"))) {
            resp.sendError(HttpServletResponse.SC_BAD_REQUEST, "Acao invalida");
            return;
        }

        try {
            Sale sale = new Sale();
            if (!"add".equals(action)) {
                int id = Integer.parseInt(req.getParameter("id"));
                sale = saleDAO.searchById(id);
                if (sale == null) {
                    resp.sendError(HttpServletResponse.SC_NOT_FOUND, "Promocao nao encontrada");
                    return;
                }
            }

            // Validate form values before creating the sale.
            if (!"delete".equals(action)) {
                int productId = readProductId(req);
                if (productId <= 0 || productDAO.searchById(productId) == null) {
                    throw new IllegalArgumentException("Selecione um produto válido.");
                }
                sale.setProductId(productId);
                sale.setDiscountPercentage(readDiscount(req));
                sale.setPromotionValidUntil(readValidUntil(req));
                LocalDate from = "add".equals(action) ? LocalDate.now() : sale.getSugestionDate();
                if (!Double.isFinite(sale.getDiscountPercentage()) || sale.getDiscountPercentage() <= 0
                        || sale.getDiscountPercentage() > 100) {
                    throw new IllegalArgumentException("Informe um desconto válido, maior que zero e até 100%.");
                }
                if (from != null && sale.getPromotionValidUntil().isBefore(from)) {
                    throw new IllegalArgumentException("A validade deve ser igual ou posterior ao início da promoção.");
                }
            }

            switch (action) {
                case "add":
                    saleDAO.insert(sale);
                    break;
                case "edit":
                    saleDAO.update(sale);
                    break;
                case "delete":
                    saleDAO.delete(sale);
                    break;
                default:
                    return;
            }
            resp.sendRedirect(req.getContextPath() + "/sales");
        } catch (IllegalArgumentException | DateTimeException e) {
            req.setAttribute("erro", e instanceof DateTimeException || e instanceof NumberFormatException
                    ? "Confira os dados da promoção." : e.getMessage());
            doGet(req, resp);
        } catch (SQLException e) {
            if ("23503".equals(e.getSQLState())) {
                req.setAttribute("erro", "Produto inexistente ou promocao com registros vinculados.");
                doGet(req, resp);
            } else if ("23514".equals(e.getSQLState())) {
                req.setAttribute("erro", "Confira o desconto e a validade: a data final deve ser igual ou posterior ao inicio da promocao.");
                doGet(req, resp);
            } else {
                throw new ServletException("Erro ao salvar promocao", e);
            }
        }
    }

    // Handles read, Product, Id
    private int readProductId(HttpServletRequest req) {
        // Applying the method logic, using the provided values
        try {
            return Integer.parseInt(req.getParameter("product-id"));
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException("Selecione um produto válido.");
        }
    }

    // Handles read, Discount
    private double readDiscount(HttpServletRequest req) {
        // Applying the method logic, using the provided values
        try {
            return Double.parseDouble(req.getParameter("discountPercentage"));
        } catch (NullPointerException | NumberFormatException e) {
            throw new IllegalArgumentException("Informe um desconto válido, maior que zero e até 100%.");
        }
    }

    // Handles read, Valid, Until
    private LocalDate readValidUntil(HttpServletRequest req) {
        // Applying the method logic, using the provided values
        try {
            return LocalDate.parse(req.getParameter("promotion-valid-until"));
        } catch (NullPointerException | DateTimeException e) {
            throw new IllegalArgumentException("Informe uma data de validade válida.");
        }
    }
}
