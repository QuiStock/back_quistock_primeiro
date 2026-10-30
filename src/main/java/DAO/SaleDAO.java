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

    public Sale rsLine(ResultSet rs) throws SQLException {

        return new Sale(
            rs.getInt("id"),
            rs.getString("suggestion_type"),
            getLocalDate(rs, "suggestion_date"),
            rs.getDouble("discount_percentage"),
            rs.getDate("promotion_valid_from").toLocalDate(),
            rs.getDate("promotion_valid_until").toLocalDate(),
            rs.getString("product_name"),
            rs.getInt("product_id")
        );

    }

    private LocalDate getLocalDate(ResultSet rs, String column)
            throws SQLException {
        Date date = rs.getDate(column);
        return date == null ? null : date.toLocalDate();
    }

    public List<Sale> select() throws SQLException {
        String query = """
                        SELECT id, suggestion_type, suggestion_date, discount_percentage, promotion_valid_from,
                        promotion_valid_until, product_name FROM suggestions_vw;
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

    public void insert(Sale sale) throws SQLException {
        String query = """
                    insert into suggestion (suggestion_type, suggestion_date, discount_percentage, 
                    promotion_valid_from, promotion_valid_until, product_id) values ('sale', ?, ?, ?, ?, ?);
                """;

        try (
            Connection conn = factory.getConnection();
            PreparedStatement smt = conn.prepareStatement(query);
        ) {
            smt.setDate(1, Date.valueOf(LocalDate.now()));
            smt.setDouble(2, sale.getDiscountPercentage());
            smt.setDate(3, Date.valueOf(LocalDate.now()));
            smt.setDate(4, Date.valueOf(sale.getPromotionValidUntil()));
            smt.setInt(5, sale.getProductId());

            smt.executeUpdate();
        }
    }

    public void delete(Sale sale) throws SQLException {
        String query = "delete from suggestion where id = ?;";

        try (
            Connection conn = factory.getConnection();
            PreparedStatement smt = conn.prepareStatement(query);
        ) {
            smt.setInt(1, sale.getId());

            smt.executeUpdate();
        }
    }

    public void update (Sale sale) throws SQLException {
        String query = "update  suggestion set discount_percentage = ?, promotion_valid_until = ? where id = ?;";

        try (
                Connection conn = factory.getConnection();
                PreparedStatement smt = conn.prepareStatement(query);
        )  {
            smt.setDouble(1, sale.getDiscountPercentage());
            smt.setDate(2, Date.valueOf(sale.getPromotionValidUntil()));
            smt.setInt(3, sale.getId());

            smt.executeUpdate();
        }
    }

    public Sale searchById(int id) throws SQLException {
        String query = "select * from suggestions_vw where id = ?;";

        try (
            Connection conn = factory.getConnection();
            PreparedStatement smt = conn.prepareStatement(query);
        ) {
            smt.setInt(1, id);

            ResultSet rs = smt.executeQuery();

            return rs.next() ? rsLine(rs) : null;
        }
    }
}