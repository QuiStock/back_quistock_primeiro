package DAO;

import Model.Faq;
import lombok.NoArgsConstructor;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

@NoArgsConstructor
public class FaqDAO {

    ConnectionFactory factory = new ConnectionFactory();

    public Faq rsLine(ResultSet rs) throws SQLException {
        return new Faq(
                rs.getInt("id"),
                rs.getString("question"),
                rs.getString("answer"),
                rs.getTimestamp("created_at").toLocalDateTime()
        );
    }

    public List<Faq> select() throws SQLException {
        String query = "select id, question, answer, created_at from faq";
        List<Faq> faqs = new ArrayList<>();

        try (
                Connection conn = factory.getConnection();
                PreparedStatement smt = conn.prepareStatement(query);
                ResultSet rs = smt.executeQuery();
             ){
            while (rs.next()) {
                faqs.add(rsLine(rs));
            }
        }

        return faqs;
    }
}
