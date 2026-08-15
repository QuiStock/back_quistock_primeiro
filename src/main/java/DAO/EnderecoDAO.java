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

    public void update(Endereco end) throws SQLException {
        String query = "UPDATE endereco set cep = ?, pais = ?, rua = ?, numero = ?, estado = ?, cidade = ?  WHERE id = ?;";

        int id =  searchEndereco(end);

        try (Connection conn = factory.getConnection();
        PreparedStatement stmt = conn.prepareStatement(query)){
            stmt.setString(1, end.getCep());
            stmt.setString(2, end.getPais());
            stmt.setString(3, end.getRua());
            stmt.setInt(4, end.getNumero());
            stmt.setString(5, end.getEstado());
            stmt.setString(6, end.getCidade());
            stmt.setInt(7, id);

            stmt.executeUpdate();
        }
    }

    public void delete(Endereco endereco) throws SQLException {
        String query = "delete from endereco where id = ?;";

        int  id =  searchEndereco(endereco);

        try (Connection conn = factory.getConnection();
        PreparedStatement stmt = conn.prepareStatement(query)){
            stmt.setInt(1, id);

            stmt.executeUpdate();
        }
    }
}
