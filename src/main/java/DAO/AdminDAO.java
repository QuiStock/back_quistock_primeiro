package DAO;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import Model.Admin;

public class AdminDAO {
    ConnectionFactory factory = new ConnectionFactory();

    //método de inserção de novo admin
    public void insert (Admin admin) throws SQLException {
        String query = "insert into admin (email, senha, nome) values(?, ?, ?); ";

        try (Connection conn = factory.getConnection();
        PreparedStatement stmt = conn.prepareStatement(query)){
            stmt.setString(1, admin.getEmail());
            stmt.setString(2, admin.getSenha());
            stmt.setString(3, admin.getNome());

            stmt.executeUpdate();
        }
    }

    //método auxiliar para encontrar id de admin
    private int searchAdmin(Admin admin) throws SQLException {
        String query = "select id from admin where email = ?;";

        try (Connection conn = factory.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)){
            ;

            stmt.setString(1, admin.getEmail());

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

    //Método de update com uso de método auxiliar
    public void update (Admin admin) throws SQLException {
        String query = "update admin set email = ?, senha = ?, nome = ? where id = ?;";

        int id = searchAdmin(admin);

        try (Connection conn = factory.getConnection();
        PreparedStatement stmt = conn.prepareStatement(query)){
            stmt.setString(1, admin.getEmail());
            stmt.setString(2, admin.getSenha());
            stmt.setString(3, admin.getNome());
            stmt.setInt(4, id);

            stmt.executeUpdate();
        }
    }

    public void delete(Admin admin) throws SQLException{
        String query = "delete from admin where id = ?;";

        int id = searchAdmin(admin);

        try (Connection conn = factory.getConnection();
        PreparedStatement stmt = conn.prepareStatement(query)){
            stmt.setInt(1, id);

            stmt.executeUpdate();
        }
    }
}
