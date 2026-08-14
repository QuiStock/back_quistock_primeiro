package DAO;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import Model.Endereco;

public class EnderecoDAO {

    ConnectionFactory factory = new ConnectionFactory();

    public void insert(Endereco endereco) throws SQLException {
        String query = "insert into endereco(cep, pais, rua, numero, cidade, estado) values(?,?,?,?,?,?);";

        try (Connection conn = factory.getConnection()){
            PreparedStatement stmt = conn.prepareStatement(query);

            stmt.setString(1, endereco.getCep());
            stmt.setString(2, endereco.getPais());
            stmt.setString(3, endereco.getRua());
            stmt.setInt(4, endereco.getNumero());
            stmt.setString(5, endereco.getCidade());
            stmt.setString(6, endereco.getEstado());

            stmt.executeUpdate();
        }
    }

    private int searchEndereco(Endereco endereco) throws SQLException {
        String query = "select id from endereco where rua = ? and numero = ?;";

        try (Connection conn = factory.getConnection();
        PreparedStatement stmt = conn.prepareStatement(query)){
            ;

            stmt.setString(1, endereco.getRua());
            stmt.setInt(2, endereco.getNumero());

            try (ResultSet rs = stmt.executeQuery()){
                if (rs.next()) {
                    return  rs.getInt("id");
                }
                else {
                    return 0;
                }
            }
        }
    }

    public void update(Endereco endereco) throws SQLException {
        String query = "UPDATE endereco set cep = ?, pais = ?, rua = ?, numero = ?, estado = ?, cidade = ?  WHERE id = ?;";

        try (Connection conn = factory.getConnection();
        PreparedStatement stmt = conn.prepareStatement(query)){
            stmt.setString(1, endereco.getCep());
            stmt.setString(2, endereco.getPais());
            stmt.setString(3, endereco.getRua());
            stmt.setInt(4, endereco.getNumero());
            stmt.setString(5, endereco.getCidade());
            stmt.setString(6, endereco.getEstado());
            stmt.setInt(7, endereco.getId());

            stmt.executeUpdate();
        }
    }

    public void delete(Endereco endereco) throws SQLException {
        String query = "delete from endereco where id = ?;";

        try (Connection conn = factory.getConnection();
        PreparedStatement stmt = conn.prepareStatement(query)){
            stmt.setInt(1, endereco.getId());

            stmt.executeUpdate();
        }
    }
}
