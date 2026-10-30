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
    protected void doGet(HttpServletRequest req,  HttpServletResponse resp)
            throws ServletException, IOException {
        req.setCharacterEncoding("UTF-8");

        String action = req.getParameter("action");

        try {

            if ("new".equals(action)){
                //carrega as lojas no formulario
                req.setAttribute("stores", storeDAO.readStores());
                req.getRequestDispatcher("/crud-product.jsp").forward(req, resp);
            }

            //carrega o produto pra editar
            else if ("edit".equals(action)) {
                int id = Integer.parseInt(req.getParameter("id"));
                Product product = dao.searchById(id);

                //retorna erro se o produto nao existir
                if (product == null) {
                    resp.sendError(HttpServletResponse.SC_NOT_FOUND, "Product not found");
                    return;
                }

                req.setAttribute("product", product);
                req.getRequestDispatcher("/crud-product.jsp").forward(req, resp);
            }

            //mostra a lista de produtos
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
    protected void doPost(HttpServletRequest req,  HttpServletResponse resp)
            throws ServletException, IOException {
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
            resp.sendError(HttpServletResponse.SC_BAD_REQUEST, "Confira os dados do produto");
        }
    }

    private void add(HttpServletRequest req, HttpServletResponse resp)
            throws SQLException, IOException {
        Product  product = new Product();

        product.setName(req.getParameter("name"));
        product.setPrice(Double.parseDouble(req.getParameter("price")));
        product.setQuantity(Integer.parseInt(req.getParameter("quantity")));
        product.setBatch(req.getParameter("batch"));
        product.setExpirationDate(LocalDate.parse(req.getParameter("expirationDate")));
        product.setStore_id(Integer.parseInt(req.getParameter("store_id")));
        product.setLine(req.getParameter("line"));
        product.setBrand(req.getParameter("brand"));

        validate(product);
        if (storeDAO.foundStore(product.getStore_id()) == null) {
            throw new IllegalArgumentException("Loja inválida");
        }

        dao.insert(product);

        resp.sendRedirect(req.getContextPath() + "/products");
    }

    private void delete(HttpServletRequest req, HttpServletResponse resp)
            throws SQLException, IOException {
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

    private void update (HttpServletRequest req, HttpServletResponse resp)
            throws SQLException, IOException{
        Product product = new Product();

        int id = Integer.parseInt(req.getParameter("id"));
        if (dao.searchById(id) == null) {
            resp.sendError(HttpServletResponse.SC_NOT_FOUND, "Product not found");
            return;
        }

        product.setId(id);
        product.setName(req.getParameter("name"));
        product.setBatch(req.getParameter("batch"));
        product.setExpirationDate(LocalDate.parse(req.getParameter("expirationDate")));
        product.setQuantity(Integer.parseInt(req.getParameter("quantity")));
        product.setPrice(Double.parseDouble(req.getParameter("price")));

        validate(product);

        dao.update(product);

        resp.sendRedirect(req.getContextPath() + "/products");
    }

    //confere os campos antes de salvar no banco
    private void validate(Product product) {
        if (product.getName() == null || product.getName().isBlank()
                || product.getBatch() == null || product.getBatch().isBlank()
                || !Double.isFinite(product.getPrice()) || product.getPrice() < 0
                || product.getQuantity() < 0) {
            throw new IllegalArgumentException("Dados inválidos");
        }
    }
}
