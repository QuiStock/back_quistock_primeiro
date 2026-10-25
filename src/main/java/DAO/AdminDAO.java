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

    //método de inserção de novo admin
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

    //método auxiliar para encontrar id de admin
    public Admin searchAdmin(int id) throws SQLException {
        String query = "select id, name, email, password from super_admin where id = ? order by id;";

        try (Connection conn = factory.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)){

            stmt.setInt(1, id);

            try (ResultSet rs = stmt.executeQuery()){

                if(rs.next()){
                    return new Admin(

                            rs.getInt("id"),
                            rs.getString("email"),
                            rs.getString("password"),
                            rs.getString("name")

                    );
                }

                return null;

            }
        }
    }

    //functional interface to use a lambda method
    private interface Binder {
        //lambda method that is able to reutilize the same query for different searches
        void bind(PreparedStatement stmt) throws SQLException;
    }

    public Admin searchBy(String where, Binder binder) throws SQLException {
        String query = "SELECT id, name, email, password FROM super_admin WHERE email = ?";

        try (
                Connection conn = factory.getConnection();
                PreparedStatement stmt = conn.prepareStatement(query);
        ) {
            binder.bind(stmt);
            try(ResultSet rs = stmt.executeQuery()){
                return rs.next() ?
            }
        }
    }

    //method that searches admin by email
    public Admin searchByEmail(String email) throws SQLException {
        String query = "select id, name, email, password from super_admin where email = ?";

        try (Connection conn = factory.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {

            stmt.setString(1, email);

            try (ResultSet rs = stmt.executeQuery()) {

                if (rs.next()) {
                    return new Admin(

                            rs.getInt("id"),
                            rs.getString("email"),
                            rs.getString("password"),
                            rs.getString("name")

                    );
                }

            }
        }
        return null;
    }

    //Método de update com uso de método auxiliar
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

                Admin adm = new Admin(
                    rs.getInt("id"),
                    rs.getString("email"),
                    rs.getString("password"),
                    rs.getString("name")
                );

                admins.add(adm);
                
            }
        }

        return admins;
    }
}
