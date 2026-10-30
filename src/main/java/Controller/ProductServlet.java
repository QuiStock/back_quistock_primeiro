package Controller;

import DAO.ProductDAO;
import DAO.StoreDAO;
import Model.Product;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.format.DateTimeParseException;

@WebServlet("/products")
public class ProductServlet extends HttpServlet {

    private final ProductDAO dao = new ProductDAO();
    private final StoreDAO storeDAO = new StoreDAO();

    @Override
    // Handles do, Get
    protected void doGet(HttpServletRequest req,  HttpServletResponse resp)
            throws ServletException, IOException {
        // Applying the method logic, using the provided values
        req.setCharacterEncoding("UTF-8");

        String action = req.getParameter("action");

        try {

            if ("new".equals(action)){
                // Load stores for the product form.
                req.setAttribute("stores", storeDAO.readStores());
                req.getRequestDispatcher("/crud-product.jsp").forward(req, resp);
            }

            else if ("edit".equals(action)) {
                int id = Integer.parseInt(req.getParameter("id"));
                Product product = dao.searchById(id);

                if (product == null) {
                    resp.sendError(HttpServletResponse.SC_NOT_FOUND, "Product not found");
                    return;
                }

                req.setAttribute("product", product);
                req.getRequestDispatcher("/crud-product.jsp").forward(req, resp);
            }

            else {
                req.setAttribute("products", dao.select());
                req.getRequestDispatcher("/WEB-INF/views/products.jsp").forward(req, resp);
            }
        } catch (SQLException e){
            throw new ServletException("Erro ao buscar produtos", e);
        } catch (IllegalArgumentException e) {
            resp.sendError(HttpServletResponse.SC_BAD_REQUEST, "ID inválido");
        }
    }

    @Override
    // Handles do, Post
    protected void doPost(HttpServletRequest req,  HttpServletResponse resp)
            throws ServletException, IOException {
        // Applying the method logic, using the provided values
        req.setCharacterEncoding("UTF-8");

        String action = req.getParameter("action");
        if (action == null) {
            resp.sendError(HttpServletResponse.SC_BAD_REQUEST, "Ação inválida");
            return;
        }
        try {

            switch (action) {

                case "new": add(req, resp); break;
                case "edit": update(req, resp); break;
                case "delete": delete(req, resp); break;
                default: resp.sendError(HttpServletResponse.SC_BAD_REQUEST, "Ação inválida");

            }
        } catch (SQLException e){
            throw new ServletException("Erro ao salvar produto", e);
        } catch (IllegalArgumentException | DateTimeParseException e) {
            req.setAttribute("erro", e instanceof DateTimeParseException
                    ? "Informe uma data de validade válida."
                    : e instanceof NumberFormatException
                    ? "Informe preço, quantidade e loja válidos." : e.getMessage());
            try {
                req.setAttribute("products", dao.select());
                req.getRequestDispatcher("/WEB-INF/views/products.jsp").forward(req, resp);
            } catch (SQLException ex) {
                throw new ServletException("Erro ao buscar produtos", ex);
            }
        }
    }

    // Handles add
    private void add(HttpServletRequest req, HttpServletResponse resp)
            throws SQLException, IOException {
        // Applying the method logic, using the provided values
        Product  product = new Product();

        product.setName(req.getParameter("name"));
        product.setPrice(Double.parseDouble(requiredParameter(req, "price")));
        product.setQuantity(Integer.parseInt(requiredParameter(req, "quantity")));
        product.setBatch(req.getParameter("batch"));
        product.setExpirationDate(LocalDate.parse(requiredParameter(req, "expirationDate")));
        product.setStore_id(Integer.parseInt(requiredParameter(req, "store_id")));
        product.setLine(req.getParameter("line"));
        product.setBrand(req.getParameter("brand"));

        validate(product);
        if (storeDAO.foundStore(product.getStore_id()) == null) {
            throw new IllegalArgumentException("Loja inválida");
        }

        dao.insert(product);

        resp.sendRedirect(req.getContextPath() + "/products");
    }

    // Handles delete
    private void delete(HttpServletRequest req, HttpServletResponse resp)
            throws SQLException, IOException {
        // Applying the method logic, using the provided values
        Product product = new Product();
        int id = Integer.parseInt(req.getParameter("id"));

        if (dao.searchById(id) == null) {
            resp.sendError(HttpServletResponse.SC_NOT_FOUND, "Product not found");
            return;
        }

        product.setId(id);

        dao.delete(product);

        resp.sendRedirect(req.getContextPath() + "/products");
    }

    // Handles update
    private void update (HttpServletRequest req, HttpServletResponse resp)
            throws SQLException, IOException{
        // Applying the method logic, using the provided values
        Product product = new Product();

        int id = Integer.parseInt(req.getParameter("id"));
        if (dao.searchById(id) == null) {
            resp.sendError(HttpServletResponse.SC_NOT_FOUND, "Product not found");
            return;
        }

        product.setId(id);
        product.setName(req.getParameter("name"));
        product.setBatch(req.getParameter("batch"));
        product.setBrand(req.getParameter("brand"));
        product.setLine(req.getParameter("line"));
        product.setExpirationDate(LocalDate.parse(requiredParameter(req, "expirationDate")));
        product.setQuantity(Integer.parseInt(requiredParameter(req, "quantity")));
        product.setPrice(Double.parseDouble(requiredParameter(req, "price")));

        validate(product);
        validateText(product.getBrand(), "Marca");
        validateText(product.getLine(), "Linha");

        dao.update(product);

        resp.sendRedirect(req.getContextPath() + "/products");
    }

    // Check required fields before saving the product.
    private void validate(Product product) {
        validateAlphaText(product.getName(), "Nome");
        validateText(product.getBrand(), "Marca");
        validateText(product.getLine(), "Linha");
        if (product.getBatch() == null || !product.getBatch().matches("^[0-9]{1,50}$")) {
            throw new IllegalArgumentException("Lote inválido: informe apenas números (até 50 dígitos).");
        }
        if (!Double.isFinite(product.getPrice()) || product.getPrice() < 0) {
            throw new IllegalArgumentException("Informe um preço válido maior ou igual a zero.");
        }
        if (product.getQuantity() < 0) {
            throw new IllegalArgumentException("Informe uma quantidade válida maior ou igual a zero.");
        }
    }

    // Handles validate, Text
    private void validateText(String value, String field) {
        // Applying the method logic, using the provided values
        validateAlphaText(value, field);
    }

    // Handles validate, Alpha, Text
    private void validateAlphaText(String value, String field) {
        // Applying the method logic, using the provided values
        if (value == null || !value.trim().matches("^[\\p{L}]+(?: +[\\p{L}]+)*$")) {
            throw new IllegalArgumentException(field + " inválido: use somente letras e espaços.");
        }
    }

    // Handles required, Parameter
    private String requiredParameter(HttpServletRequest req, String name) {
        // Applying the method logic, using the provided values
        String value = req.getParameter(name);
        if (value == null || value.isBlank()) {
            throw new IllegalArgumentException("Preencha todos os campos antes de salvar.");
        }
        return value;
    }
}
