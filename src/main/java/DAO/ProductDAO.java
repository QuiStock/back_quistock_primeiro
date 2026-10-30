package DAO;

import Model.Product;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class ProductDAO {

    private final ConnectionFactory factory = new ConnectionFactory();

    private Product rsLine(ResultSet rs) throws SQLException {
        Product product = new Product(
            rs.getInt("id"),
            rs.getString("name"),
            rs.getDouble("price"),
            rs.getInt("quantity"),
            rs.getString("brand"),
            rs.getInt("store_id"),
            rs.getString("store_name")
        );
        return product;
    }

    public List<Product> select() throws SQLException {
        String query = "SELECT id, name, price, stock_quantity, store_id, email FROM product_vw;";
        List<Product> products = new ArrayList<>();

        try (Connection conn = factory.getConnection();
             PreparedStatement smt = conn.prepareStatement(query);
             ResultSet rs = smt.executeQuery()) {
            while (rs.next()) {

            }
        }

        return products;
    }

    public void delete (Product product) throws SQLException {
        String query = "DELETE FROM product WHERE id = ?;";

        try (
                Connection conn = factory.getConnection();
                PreparedStatement smt = conn.prepareStatement(query);
        ) {
            smt.setInt(1, product.getId());

            smt.executeUpdate();
        }
    }

    public void update  (Product product) throws SQLException {
        String query = "UPDATE product SET name = ? , price = ?, stock_quantity = ?, brand = ? WHERE id = ?;";

        try (
                Connection conn = factory.getConnection();
                PreparedStatement smt = conn.prepareStatement(query);
        ) {
            smt.setString(1, product.getName());
            smt.setDouble(2, product.getPrice());
            smt.setInt(3, product.getQuantity());
            smt.setString(4, product.getBrand());
            smt.setInt(5, product.getId());

            smt.executeUpdate();
        }
    }

    public Product searchById(int id) throws SQLException {
        String query = "SELECT id, name FROM product WHERE id = ?";

        try (Connection conn = factory.getConnection();
             PreparedStatement smt = conn.prepareStatement(query)) {
            smt.setInt(1, id);
            try (ResultSet rs = smt.executeQuery()) {
                return rs.next() ? rsLine(rs) : null;
            }
        }
    }
}
