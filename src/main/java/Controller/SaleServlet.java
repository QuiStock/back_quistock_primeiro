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
import java.time.format.DateTimeFormatter;

@WebServlet("/sales")
public class SaleServlet extends HttpServlet {
    private final SaleDAO saleDAO = new SaleDAO();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        req.setCharacterEncoding("UTF-8");
        String action = req.getParameter("action");

        try {
            if ("add".equals(action)) {
                req.getRequestDispatcher("/crud-sale.jsp").forward(req, resp);
                return;
            }
            if ("edit".equals(action)) {
                int id  = Integer.parseInt(req.getParameter("id"));
                Sale sale = saleDAO.searchById(id);
                if (sale == null) {
                    resp.sendError(HttpServletResponse.SC_NOT_FOUND, "Venda não encontrada");
                    return;
                }
                req.setAttribute("sale", sale);
                req.getRequestDispatcher("/crud-sale.jsp").forward(req, resp);
                return;
            }

            req.setAttribute("itens", saleDAO.select());
            req.getRequestDispatcher("/WEB-INF/views/promocao.jsp").forward(req, resp);
        } catch (NumberFormatException e) {
            resp.sendError(HttpServletResponse.SC_BAD_REQUEST, "ID inválido");
        } catch (SQLException e) {
            throw new ServletException("Erro ao consultar vendas", e);
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
                case "add": add(req, resp);
                case "edit": update(req, resp);
                case "delete": delete(req, resp);
            }
            resp.sendRedirect(req.getContextPath() + "/sales");
        } catch (NumberFormatException | java.time.DateTimeException e) {
            resp.sendError(HttpServletResponse.SC_BAD_REQUEST, "Dados da venda inválidos");
        } catch (SQLException e) {
            throw new ServletException("Erro ao salvar venda", e);
        }
    }

    private void add (HttpServletRequest req, HttpServletResponse resp)
            throws SQLException, IOException {
        Sale sale = new Sale();

        String discountPercentage = req.getParameter("discountPercentage");
        LocalDate promotionValidUntil = LocalDate.parse(req.getParameter("promotion-valid-until"));
        int id = Integer.parseInt(req.getParameter("id"));

        if (saleDAO.searchById(id) == null) {
            resp.sendError(HttpServletResponse.SC_NOT_FOUND, "Promoção não encontrada");
            return;
        }

        if (!validDate(promotionValidUntil)){
            req.setAttribute("erro", "A data deve estar no formato dd/mm/yyyy");
            return;
        }

        if (!validPrice(discountPercentage)){
            req.setAttribute("erro", "O preço deve estar no formato: 00.00");
            return;
        }

        double discount = Double.parseDouble(discountPercentage);

        sale.setDiscountPercentage(discount);
        sale.setPromotionValidUntil(promotionValidUntil);
        sale.setProductId(id);

        saleDAO.insert(sale);

        resp.sendRedirect(req.getContextPath() + "/sales");
    }

    private void delete(HttpServletRequest req, HttpServletResponse resp)
            throws SQLException, IOException {
        Sale sale = new Sale();

        int id = Integer.parseInt(req.getParameter("id"));

        if (saleDAO.searchById(id) == null) {
            resp.sendError(HttpServletResponse.SC_NOT_FOUND, "Promoção não encontrada");
            return;
        }

        sale.setId(id);

        saleDAO.delete(sale);

        resp.sendRedirect(req.getContextPath() + "/sales");
    }

    private void update(HttpServletRequest req, HttpServletResponse resp)
            throws SQLException, IOException {
        Sale sale = new Sale();

        int id = Integer.parseInt(req.getParameter("id"));
        LocalDate promotionValidUntil = LocalDate.parse(req.getParameter("promotion-valid-until"));
        String discountPercentage = req.getParameter("discountPercentage");

        if (saleDAO.searchById(id) == null) {
            resp.sendError(HttpServletResponse.SC_NOT_FOUND, "Promoção não encontrada");
            return;
        }

        if (!validDate(promotionValidUntil)){
            req.setAttribute("erro", "A data deve estar no formato dd/mm/yyyy");
            return;
        }

        if (!validPrice(discountPercentage)){
            req.setAttribute("erro", "O preço deve estar no formato: 00.00");
            return;
        }

        double discount = Double.parseDouble(discountPercentage);

        sale.setDiscountPercentage(discount);
        sale.setPromotionValidUntil(promotionValidUntil);
        sale.setProductId(id);

        saleDAO.update(sale);

        resp.sendRedirect(req.getContextPath() + "/sales");
    }

    private boolean validDate(LocalDate promotionValidUntil) {
        if (promotionValidUntil == null) {
            return false;
        }

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        String date = promotionValidUntil.format(formatter);
        return date.matches("\\d{2}/\\d{2}/\\d{4}");
    }

    private boolean validPrice(String valor) {
        return valor != null && valor.matches("^\\d+\\.\\d{2}$");
    }
}
