package DAO;

import Model.Promotion;
import lombok.NoArgsConstructor;

import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@NoArgsConstructor
public class PromotionDAO {

    private final ConnectionFactory factory = new ConnectionFactory();

    public Promotion rsLine(ResultSet rs) throws SQLException {

        return new Promotion(
            rs.getInt("id"),
            rs.getString("suggestion_type"),
            rs.getDate("suggestion_date").toLocalDate(),
            rs.getDouble("discount_percentage"),
            rs.getDate("promotion_valid_from").toLocalDate(),
            rs.getDate("promotion_valid_until").toLocalDate(),
            rs.getString("product_name")
        );

    }

    public List<Promotion> select() throws SQLException {
        String query = """
                        SELECT id, suggestion_type, suggestion_date, discount_percentage, promotion_valid_from,
                        promotion_valid_until, product_name FROM suggestions_vw;
                        """;
        List<Promotion> list = new ArrayList<>();

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

    public void insert(Promotion promotion) throws SQLException {
        String query = """
                    insert into suggestion (suggestion_type, suggestion_date, discount_percentage, 
                    promotion_valid_from, promotion_valid_until, product_id) values ('promotion', ?, ?, ?, ?, ?);
                """;

        try (
            Connection conn = factory.getConnection();
            PreparedStatement smt = conn.prepareStatement(query);
        ) {
            smt.setDate(1, Date.valueOf(LocalDate.now()));
            smt.setDouble(2, promotion.getDiscountPercentage());
            smt.setDate(3, Date.valueOf(LocalDate.now()));
            smt.setDate(4, Date.valueOf(promotion.getPromotionValidFrom()));
            smt.setDate(5, Date.valueOf(promotion.getPromotionValidUntil()));
            smt.setString(6, promotion.getProductName());

            smt.executeUpdate();
        }
    }

    public void delete(Promotion promotion) throws SQLException {
        String query = "delete from suggestion where id = ?;";

        try (
            Connection conn = factory.getConnection();
            PreparedStatement smt = conn.prepareStatement(query);
        ) {
            smt.setInt(1, promotion.getId());

            smt.executeUpdate();
        }
    }

    public void update (Promotion promotion) throws SQLException {
        String query = "update  suggestion set discount_percentage = ?, promotion_valid_until = ? where id = ?;";

        try (
                Connection conn = factory.getConnection();
                PreparedStatement smt = conn.prepareStatement(query);
        )  {
            smt.setDouble(1, promotion.getDiscountPercentage());
            smt.setDate(2, Date.valueOf(promotion.getPromotionValidUntil()));
            smt.setInt(3, promotion.getId());

            smt.executeUpdate();
        }
    }

    public Promotion searchById(int id) throws SQLException {
        String query = "select * from suggestions_vw where id = ?;";

        try (
            Connection conn = factory.getConnection();
            PreparedStatement smt = conn.prepareStatement(query);
            ResultSet rs = smt.executeQuery();
        ) {
            return rsLine(rs);
        }
    }
}