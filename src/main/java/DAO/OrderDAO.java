package DAO;

import Model.Order;

import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class OrderDAO {

    private final ConnectionFactory factory = new ConnectionFactory();

    private Order rsLine(ResultSet rs) throws SQLException {
        return new Order(
                rs.getInt("id"),
                rs.getString("suggestion_type"),
                rs.getDate("suggestion_date").toLocalDate(),
                rs.getDate("order_date").toLocalDate(),
                rs.getDouble("total_order_price"),
                rs.getString("product_name")
        );
    }

    public List<Order> select() throws SQLException {
        String query = """
                select id, suggestion_type, suggestion_date, order_date, total_order_price, product_name
                from orders_vw;
                """;
        List<Order> orders = new ArrayList<>();

        try (
                Connection conn = factory.getConnection();
                PreparedStatement smt = conn.prepareStatement(query);
                ResultSet rs = smt.executeQuery();
        ) {
            while (rs.next()) {
                orders.add(rsLine(rs));
            }
        }

        return orders;
    }

    public void insert(Order order) throws SQLException {
        String query = """
                insert into suggestion(suggestion_type, suggestion_date, order_date, total_order_price, product_name)
                values ('order', ?, ?, ?, ?)
        """;
        try (
                Connection conn = factory.getConnection();
                PreparedStatement smt = conn.prepareStatement(query);
        ) {
            smt.setDate(1, Date.valueOf(LocalDate.now()));
            smt.setDate(2, Date.valueOf(LocalDate.now()));
            smt.setDouble(3, order.getTotalOrderPrice());
            smt.setString(4, order.getProductName());

            smt.executeUpdate();
        }
    }

    public void delete(Order order) throws SQLException {
        String query = "delete from suggestion where id = ?;";

        try (
                Connection conn = factory.getConnection();
                PreparedStatement smt = conn.prepareStatement(query);
        ) {
            smt.setInt(1, order.getId());

            smt.executeUpdate();
        }
    }

    public Order searchById(int id) throws SQLException{
        String query = "select * from suggestion where id = ?;";

        try (
                Connection conn = factory.getConnection();
                PreparedStatement smt = conn.prepareStatement(query);
                ResultSet rs = smt.executeQuery();
        ) {
            return rsLine(rs);
        }
    }
}