package DAO;

import Model.Faq;
import lombok.NoArgsConstructor;

import java.sql.*;
import java.time.LocalDate;
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
                rs.getDate("created_at").toLocalDate(),
                rs.getDate("updated_at").toLocalDate()
        );
    }

    public List<Faq> select() throws SQLException {
        String query = "SELECT id, question, answer, created_at FROM faq ORDER BY id;";
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
        String query = "INSERT INTO faq(question, answer, created_at, updated_at) VALUES(?, ?, ?, ?);";

        try (
                Connection conn = factory.getConnection();
                PreparedStatement smt = conn.prepareStatement(query);
        ) {
            smt.setString(1, faq.getQuestion());
            smt.setString(2, faq.getAnswer());
            smt.setDate(3, Date.valueOf(LocalDate.now()));
            smt.setDate(4, Date.valueOf(LocalDate.now()));

            smt.executeUpdate();
        }
    }

    public void update(Faq faq) throws SQLException {
        String query = "UPDATE faq set question =  ?, answer = ?, updated_at = ? WHERE id = ?;";

        try (
                Connection conn = factory.getConnection();
                PreparedStatement smt = conn.prepareStatement(query);
        ) {
            smt.setString(1, faq.getQuestion());
            smt.setString(2, faq.getAnswer());
            smt.setDate(3, Date.valueOf(LocalDate.now()));

            smt.executeUpdate();
        }
    }

    public void delete(Faq faq) throws SQLException{
        String query = "DELETE FROM faq WHERE id = ?;";

        try (
                Connection conn = factory.getConnection();
                PreparedStatement smt = conn.prepareStatement(query);
        ) {
            smt.setInt(1, faq.getId());

            smt.executeUpdate();
        }
    }

    public Faq searchById(int id) throws SQLException{
        String query = "SELECT id, question, answer, created_ad, updated_at FROM faq WHERE id = ?;";

        try (
                Connection conn = factory.getConnection();
                PreparedStatement smt = conn.prepareStatement(query);
        ) {
            smt.setInt(1, id);

            try (
                    ResultSet rs = smt.executeQuery();
            ){
                return rsLine(rs);
            }
        }
    }
}