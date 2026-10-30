package Controller;

import DAO.ProductDAO;
import Model.Product;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.sql.SQLException;
import java.time.LocalDate;

public class ProductServlet extends HttpServlet {

    private final ProductDAO dao = new ProductDAO();

    @Override
    protected void doGet(HttpServletRequest req,  HttpServletResponse resp)
            throws ServletException, IOException {
        req.setCharacterEncoding("UTF-8");

        String action = req.getParameter("action");

        try {

            if ("new".equals(action)){
                //carrega as regioes no formulario
                req.setAttribute("products", dao.select());
                req.getRequestDispatcher("/crud-product.jsp").forward(req, resp);
            }

            //else if pra editar o gerente
            else if ("edit".equals(action)) {
                int id = Integer.parseInt(req.getParameter("id"));
                Product product = dao.searchById(id);

                //caso o gerente venha vazio, retorna tela de erro
                if (product == null) {
                    resp.sendError(HttpServletResponse.SC_NOT_FOUND, "Product not found");
                    return;
                }

                //manda o gerente e as regioes pro formulario
                req.setAttribute("product", product);
                req.getRequestDispatcher("/crud-product.jsp").forward(req, resp);
            }

            //caso nao seja nenhum dos actions acima, retorna lista de gerentes
            else {
                req.setAttribute("managers", dao.select());
                req.getRequestDispatcher("/regional-managers.jsp").forward(req, resp);
            }
        } catch (SQLException e){
            resp.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, e.getMessage());
        }
    }

    @Override
    protected void doPost(HttpServletRequest req,  HttpServletResponse resp)
            throws ServletException, IOException {
        req.setCharacterEncoding("UTF-8");

        String action = req.getParameter("action");
        try {

            switch (action) {

                case "new": add(req, resp); break;
                case "edit": update(req, resp); break;
                case "delete": delete(req, resp); break;

            }
        } catch (SQLException e){
            resp.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, e.getMessage());
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
        product.setQuantity(Integer.parseInt(req.getParameter("quantity")));
        product.setStore_id(Integer.parseInt(req.getParameter("store_id")));
        product.setLine(req.getParameter("line"));

        dao.insert(product);

        resp.sendRedirect(req.getContextPath() + "/products");
    }

    private void delete(HttpServletRequest req, HttpServletResponse resp)
            throws SQLException, IOException {
        Product product = new Product();
        int id = Integer.parseInt(req.getParameter("id"));

        if (dao.searchById(id) == null) {
            resp.sendError(HttpServletResponse.SC_NOT_FOUND, "Product not found");
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
        }

        product.setId(id);
        product.setName(req.getParameter("name"));
        product.setBatch(req.getParameter("batch"));
        product.setLine(req.getParameter("line"));
        product.setExpirationDate(LocalDate.parse(req.getParameter("expirationDate")));
        product.setQuantity(Integer.parseInt(req.getParameter("quantity")));
        product.setPrice(Double.parseDouble(req.getParameter("price")));
        product.setBrand(req.getParameter("brand"));

        dao.update(product);

        resp.sendRedirect(req.getContextPath() + "/products");
    }
}
