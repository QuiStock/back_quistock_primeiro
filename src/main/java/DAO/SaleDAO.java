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

        Sale sale = new Sale();
        sale.setId(rs.getInt("id"));
        sale.setSugestionType(rs.getString("suggestion_type"));
        Date suggestionDate = rs.getDate("suggestion_date");
        Date validUntil = rs.getDate("promotion_valid_until");
        sale.setSugestionDate(suggestionDate == null ? null : suggestionDate.toLocalDate());
        sale.setPromotionValidUntil(validUntil == null ? null : validUntil.toLocalDate());
        sale.setDiscountPercentage(rs.getDouble("discount_percentage"));
        sale.setProductName(rs.getString("product_name"));
        return sale;

    }

    public List<Sale> select() throws SQLException {
        String query = """
                        SELECT id, suggestion_type, suggestion_date, discount_percentage, promotion_valid_until, product_name FROM suggestions_vw WHERE suggestion_type = 'promotion';
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
                    insert into suggestion (suggestion_type, suggestion_date, discount_percentage, promotion_valid_from, promotion_valid_until, product_id) values ('promotion', ?, ?, ?, ?, ?);
                """;

        try (
            Connection conn = factory.getConnection();
            PreparedStatement smt = conn.prepareStatement(query);
        ) {
            LocalDate today = LocalDate.now();
            smt.setDate(1, Date.valueOf(today));
            smt.setDouble(2, sale.getDiscountPercentage());
            //o banco exige a data inicial mesmo sem mostrar esse campo na tela
            smt.setDate(3, Date.valueOf(today));
            smt.setDate(4, Date.valueOf(sale.getPromotionValidUntil()));
            smt.setInt(5, sale.getProductId());

            smt.executeUpdate();
        }
    }

    public void delete(Sale sale) throws SQLException {
        String query = "delete from suggestion where id = ? and suggestion_type = 'promotion';";

        try (
            Connection conn = factory.getConnection();
            PreparedStatement smt = conn.prepareStatement(query);
        ) {
            smt.setInt(1, sale.getId());

            smt.executeUpdate();
        }
    }

    public void update (Sale sale) throws SQLException {
        String query = "update  suggestion set discount_percentage = ?, promotion_valid_until = ? where id = ? and suggestion_type = 'promotion';";

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
