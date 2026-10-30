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

    public List<Product> select() throws SQLException {
        String query = "SELECT id, name FROM product ORDER BY name, id";
        List<Product> products = new ArrayList<>();

        try (Connection conn = factory.getConnection();
             PreparedStatement smt = conn.prepareStatement(query);
             ResultSet rs = smt.executeQuery()) {
            while (rs.next()) {
                products.add(new Product(rs.getInt("id"), rs.getString("name")));
            }
        }

        return products;
    }

    public Product searchById(int id) throws SQLException {
        String query = "SELECT id, name FROM product WHERE id = ?";

        try (Connection conn = factory.getConnection();
             PreparedStatement smt = conn.prepareStatement(query)) {
            smt.setInt(1, id);
            try (ResultSet rs = smt.executeQuery()) {
                return rs.next() ? new Product(rs.getInt("id"), rs.getString("name")) : null;
            }
        }
    }
}
