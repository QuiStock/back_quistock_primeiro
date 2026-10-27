package DAO;

import Model.Faq;
import lombok.NoArgsConstructor;

import java.sql.*;
import java.time.LocalDateTime;
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
        String query = "select id, question, answer, created_at from faq order by id;";
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

    public void insert(Faq faq) throws SQLException {
        String query = "insert into faq(question, answer, created_at) values(?, ?, ?);";

        try (
                Connection conn = factory.getConnection();
                PreparedStatement smt = conn.prepareStatement(query);
                ResultSet rs = smt.executeQuery();
        ) {
            smt.setString(1, faq.getQuestion());
            smt.setString(2, faq.getAnswer());
            smt.setTimestamp(3, Timestamp.valueOf(LocalDateTime.now()));
        }
    }
}
