package DAO;

import Model.Product;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ProductDAO {

    private final ConnectionFactory factory = new ConnectionFactory();

    // Keep a missing expiration date as null in the model.
    private Product rsLine(ResultSet rs) throws SQLException {
        Product product = new Product(
            rs.getInt("id"),
            rs.getString("name"),
            rs.getString("batch"),
            rs.getDouble("price"),
            rs.getInt("quantity"),
            rs.getString("brand"),
            rs.getInt("store_id"),
            rs.getString("store_name"),
            rs.getDate("expiration_date") == null ? null : rs.getDate("expiration_date").toLocalDate(),
            rs.getString("line")
        );
        return product;
    }

    public List<Product> select() throws SQLException {
        String query = "SELECT p.id, p.name, p.batch, p.price, p.stock_quantity AS quantity, " +
                "p.brand, p.store_id, s.email AS store_name, p.expiration_date, p.line " +
                "FROM product p LEFT JOIN store s ON s.id = p.store_id ORDER BY p.id;";
        List<Product> products = new ArrayList<>();

        try (Connection conn = factory.getConnection();
             PreparedStatement smt = conn.prepareStatement(query);
             ResultSet rs = smt.executeQuery()) {
            while (rs.next()) {
                products.add(rsLine(rs));
            }
        }

        return products;
    }

    public void insert(Product product) throws SQLException {
        String query = "INSERT INTO product(batch, name, price, expiration_date, brand, line, stock_quantity, store_id) VALUES(?, ?, ?, ?, ?, ?, ?, ?);";

        try (
                Connection conn = factory.getConnection();
                PreparedStatement smt = conn.prepareStatement(query);
        ) {
            smt.setString(1, product.getBatch());
            smt.setString(2, product.getName());
            smt.setDouble(3, product.getPrice());
            smt.setDate(4, Date.valueOf(product.getExpirationDate()));
            smt.setString(5, product.getBrand());
            smt.setString(6, product.getLine());
            smt.setInt(7, product.getQuantity());
            smt.setInt(8, product.getStore_id());

            smt.executeUpdate();
        }
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

    public void update (Product product) throws SQLException {
        String query = "UPDATE product SET name = ?, batch = ?, brand = ?, line = ?, expiration_date = ?, stock_quantity = ?, price = ? WHERE id = ?;";

        try (
                Connection conn = factory.getConnection();
                PreparedStatement smt = conn.prepareStatement(query);
        ) {
            smt.setString(1, product.getName());
            smt.setString(2, product.getBatch());
            smt.setString(3, product.getBrand());
            smt.setString(4, product.getLine());
            smt.setDate(5, Date.valueOf(product.getExpirationDate()));
            smt.setInt(6, product.getQuantity());
            smt.setDouble(7, product.getPrice());
            smt.setInt(8, product.getId());

            smt.executeUpdate();
        }
    }

    public Product searchById(int id) throws SQLException {
        String query = "SELECT p.id, p.name, p.batch, p.price, p.stock_quantity AS quantity, " +
                "p.brand, p.store_id, s.email AS store_name, p.expiration_date, p.line " +
                "FROM product p LEFT JOIN store s ON s.id = p.store_id WHERE p.id = ?";

        try (Connection conn = factory.getConnection();
             PreparedStatement smt = conn.prepareStatement(query)) {
            smt.setInt(1, id);
            try (ResultSet rs = smt.executeQuery()) {
                return rs.next() ? rsLine(rs) : null;
            }
        }
    }
}
