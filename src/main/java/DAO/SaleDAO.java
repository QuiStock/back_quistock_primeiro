package DAO;

import Model.Sale;
import lombok.NoArgsConstructor;

import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@NoArgsConstructor
public class SaleDAO {

    private final ConnectionFactory factory = new ConnectionFactory();

    // Handles rs, Line
    public Sale rsLine(ResultSet rs) throws SQLException {

        Sale sale = new Sale();
        sale.setId(rs.getInt("id"));
        sale.setSugestionType(rs.getString("suggestion_type"));
        Date suggestionDate = rs.getDate("suggestion_date");
        Date validUntil = rs.getDate("promotion_valid_until");
        sale.setSugestionDate(suggestionDate == null ? null : suggestionDate.toLocalDate());
        sale.setPromotionValidUntil(validUntil == null ? null : validUntil.toLocalDate());
        sale.setDiscountPercentage(rs.getDouble("discount_percentage"));
        sale.setProductName(rs.getString("product_name"));
        sale.setProductId(rs.getInt("product_id"));
        return sale;

    }

    // Handles get, Local, Date
    private LocalDate getLocalDate(ResultSet rs, String column)
            throws SQLException {
        // Applying the method logic, using the provided values
        Date date = rs.getDate(column);
        return date == null ? null : date.toLocalDate();
    }

    // Handles select
    public List<Sale> select() throws SQLException {
        // Applying the method logic, using the provided values
        String query = """
                        SELECT id, suggestion_type, suggestion_date, discount_percentage, promotion_valid_until, product_name, product_id FROM suggestions_vw WHERE suggestion_type = 'promotion';
                        """;
        List<Sale> list = new ArrayList<>();

        try (
                Connection conn = factory.getConnection();
                PreparedStatement smt = conn.prepareStatement(query);
                ResultSet rs = smt.executeQuery();
        ) {
            while (rs.next()) {
                list.add(rsLine(rs));
            }
        }

        return list;
    }

    // Handles insert
    public void insert(Sale sale) throws SQLException {
        // Applying the method logic, using the provided values
        String query = """
                    insert into suggestion (suggestion_type, suggestion_date, discount_percentage, promotion_valid_from, promotion_valid_until, product_id) values ('promotion', ?, ?, ?, ?, ?);
                """;

        try (
            Connection conn = factory.getConnection();
            PreparedStatement smt = conn.prepareStatement(query);
        ) {
            LocalDate today = LocalDate.now();
            smt.setDate(1, Date.valueOf(today));
            smt.setDouble(2, sale.getDiscountPercentage());
            // The database requires a start date even though the form does not show one.
            smt.setDate(3, Date.valueOf(today));
            smt.setDate(4, Date.valueOf(sale.getPromotionValidUntil()));
            smt.setInt(5, sale.getProductId());

            smt.executeUpdate();
        }
    }

    // Handles delete
    public void delete(Sale sale) throws SQLException {
        // Applying the method logic, using the provided values
        String query = "delete from suggestion where id = ? and suggestion_type = 'promotion';";

        try (
            Connection conn = factory.getConnection();
            PreparedStatement smt = conn.prepareStatement(query);
        ) {
            smt.setInt(1, sale.getId());

            smt.executeUpdate();
        }
    }

    // Handles update
    public void update (Sale sale) throws SQLException {
        // Applying the method logic, using the provided values
        String query = "update suggestion set discount_percentage = ?, promotion_valid_until = ?, product_id = ? where id = ? and suggestion_type = 'promotion';";

        try (
                Connection conn = factory.getConnection();
                PreparedStatement smt = conn.prepareStatement(query);
        )  {
            smt.setDouble(1, sale.getDiscountPercentage());
            smt.setDate(2, Date.valueOf(sale.getPromotionValidUntil()));
            smt.setInt(3, sale.getProductId());
            smt.setInt(4, sale.getId());
            smt.executeUpdate();
        }
    }

    // Handles search, By, Id
    public Sale searchById(int id) throws SQLException {
        // Applying the method logic, using the provided values
        String query = "select * from suggestions_vw where id = ? and suggestion_type = 'promotion';";

        try (
            Connection conn = factory.getConnection();
            PreparedStatement smt = conn.prepareStatement(query);
        ) {
            smt.setInt(1, id);
            try (ResultSet rs = smt.executeQuery()) {
                return rs.next() ? rsLine(rs) : null;
            }
        }
    }
}
