package Controller;

import DAO.SaleDAO;
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

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        req.setCharacterEncoding("UTF-8");
        try {
            req.setAttribute("itens", saleDAO.select());
            req.getRequestDispatcher("/WEB-INF/views/promocao.jsp").forward(req, resp);
        } catch (SQLException e) {
            throw new ServletException("Erro ao consultar promocoes", e);
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
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

            //pegando e conferindo os dados do formulario
            if (!"delete".equals(action)) {
                String discount = req.getParameter("discountPercentage");
                String until = req.getParameter("promotion-valid-until");
                if (discount == null || until == null) {
                    throw new IllegalArgumentException("Dados incompletos");
                }
                sale.setDiscountPercentage(Double.parseDouble(discount));
                sale.setPromotionValidUntil(LocalDate.parse(until));
                LocalDate from = "add".equals(action) ? LocalDate.now() : sale.getSugestionDate();
                if (!Double.isFinite(sale.getDiscountPercentage()) || sale.getDiscountPercentage() <= 0
                        || sale.getDiscountPercentage() > 100
                        || (from != null && sale.getPromotionValidUntil().isBefore(from))) {
                    throw new IllegalArgumentException("Desconto ou validade invalida");
                }
            }

            switch (action) {
                case "add":
                    int productId = Integer.parseInt(req.getParameter("product-id"));
                    if (productId <= 0) {
                        throw new IllegalArgumentException("Produto invalido");
                    }
                    sale.setProductId(productId);
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
            req.setAttribute("erro", "Informe um produto valido, desconto entre 0 e 100 e validade a partir do inicio da promocao.");
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
}
