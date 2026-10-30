package DAO;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import Model.Admin;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@NoArgsConstructor
public class AdminDAO {
    ConnectionFactory factory = new ConnectionFactory();

    public void insert (Admin admin) throws SQLException {

        String query = "insert into super_admin (email, password, name) values(?, ?, ?); ";

        try (Connection conn = factory.getConnection();
        PreparedStatement stmt = conn.prepareStatement(query)){

            stmt.setString(1, admin.getEmail());
            stmt.setString(2, admin.getPassword());
            stmt.setString(3, admin.getName());

            stmt.executeUpdate();
        }
    }

    // Map one result row to an admin.
    private Admin rsLine(ResultSet rs) throws SQLException {

        Admin admin = new Admin(
                rs.getInt("id"),
                rs.getString("email"),
                rs.getString("password"),
                rs.getString("name")
        );

        return admin;
    }

    private interface Binder {
        void bind(PreparedStatement stmt) throws SQLException;
    }

    // Share the lookup logic between ID and email searches.
    public Admin searchBy(String where, Binder binder) throws SQLException {
        String query = "SELECT id, name, email, password FROM super_admin " + where;

        try (
                Connection conn = factory.getConnection();
                PreparedStatement stmt = conn.prepareStatement(query);
        ) {

            binder.bind(stmt);

            try(ResultSet rs = stmt.executeQuery()){

                return rs.next() ? rsLine(rs) : null;

            }
        }
    }

    public Admin searchByEmail(String email) throws SQLException {
        return searchBy("where email = ?", stmt -> stmt.setString(1, email));
    }

    public Admin searchById(int id) throws SQLException {
        return searchBy("where id = ?", stmt -> stmt.setInt(1, id));
    }

    public void update (Admin admin) throws SQLException {

        String query = "update super_admin set email = ?, password = ?, name = ? where id = ?;";

        try (
                Connection conn = factory.getConnection();
                PreparedStatement stmt = conn.prepareStatement(query)
        ){

            stmt.setString(1, admin.getEmail());
            stmt.setString(2, admin.getPassword());
            stmt.setString(3, admin.getName());
            stmt.setInt(4, admin.getId());

            stmt.executeUpdate();

        }
    }

    public void delete(Admin admin) throws SQLException{

        String query = "delete from super_admin where id = ?;";

        try (
                Connection conn = factory.getConnection();
                PreparedStatement stmt = conn.prepareStatement(query)
        ){

            stmt.setInt(1, admin.getId());

            stmt.executeUpdate();

        }
    }

    public List<Admin> select() throws SQLException{

        String query = "select * from super_admin;";

        List<Admin> admins = new ArrayList<>();

        try (
                Connection conn = factory.getConnection();
                 PreparedStatement stmt = conn.prepareStatement(query);
                ResultSet rs = stmt.executeQuery()
        ){

            while(rs.next()){

                admins.add(rsLine(rs));
                
            }
        }

        return admins;
    }
}
