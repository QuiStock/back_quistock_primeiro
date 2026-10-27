package DAO;

import Model.RegionalManager;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class RegionalManagerDAO {

    private final ConnectionFactory connectionFactory = new ConnectionFactory();

    private static final String query = "SELECT m.id, m.name, m.email, m.password, m.region_type_id, r.name AS region_name " +
                                        "FROM regional_manager m LEFT JOIN region_type r ON r.id = m.region_type_id ";

    //method that simply return the line of a SQL ResultSet
    private RegionalManager rsLine(ResultSet rs) throws SQLException {

        RegionalManager manager = new RegionalManager(

                rs.getInt("id"),
                rs.getString("email"),
                rs.getString("name"),
                rs.getString("password"),
                rs.getString("region_name"),
                rs.getInt("region_type_id")

        );

        return manager;
    }

    //method that returns a List with all regional managers
    public List<RegionalManager> select() throws SQLException {

        List<RegionalManager> regionalManagers = new ArrayList<>();

        try (Connection conn = connectionFactory.getConnection();
             PreparedStatement sttmt = conn.prepareStatement(query + "ORDER BY m.id");
             ResultSet rs = sttmt.executeQuery()) {

            //repeats while there is another line
            while (rs.next()) {
                regionalManagers.add(rsLine(rs));
            }
        }

        return regionalManagers;
    }

    //method for a new manager
    public void insert(RegionalManager regionalManager) throws SQLException {

        String sql = "INSERT INTO regional_manager (name, email, password, region_type_id) VALUES ( ?, ?, ?, ?)";

        //try pra adicionar informaçoes no novo gerente
        try (
                Connection conn = connectionFactory.getConnection();
                PreparedStatement sttmt = conn.prepareStatement(sql)
        ){

            sttmt.setString(1, regionalManager.getName());
            sttmt.setString(2, regionalManager.getEmail());
            sttmt.setString(3, regionalManager.getPassword());
            sttmt.setInt(4, regionalManager.getRegionTypeId());

            sttmt.executeUpdate();

        }
    }

    //functional interface to use a lambda method
    private interface Binder{
        //lambda method that is able to reutilize the same query for different searches
        void bind(PreparedStatement stmt) throws SQLException;
    }

    //method that searches for a manager based on the id
    public RegionalManager searchById(int id) throws SQLException {
        return searchBy("where m.id = ?", stmt -> stmt.setInt(1, id));
    }

    //a method to be reutilized, can be used for both searches based on id and email
    private RegionalManager searchBy(String where, Binder binder) throws SQLException{

        try (
                Connection conn = connectionFactory.getConnection();
                PreparedStatement sttmt = conn.prepareStatement(query + where);
        ){
            binder.bind(sttmt);
            try (ResultSet rs = sttmt.executeQuery()) {
                return rs.next() ? rsLine(rs) : null;
            }
        }

    }

    //search method based on email, using lambda method
    public RegionalManager searchByEmail(String email) throws SQLException {
        return searchBy("WHERE m.email = ?",  stmt -> stmt.setString(1, email));
    }

    //update method based on search by id
    public void update(RegionalManager regionalManager) throws SQLException{

        String sql = "UPDATE regional_manager SET name = ?, email = ?, password = ?, region_type_id = ? WHERE id = ?";

        try (Connection conn = connectionFactory.getConnection();
             PreparedStatement sttmt = conn.prepareStatement(sql)){

            sttmt.setString(1, regionalManager.getName());
            sttmt.setString(2, regionalManager.getEmail());
            sttmt.setString(3, regionalManager.getPassword());
            sttmt.setInt(4, regionalManager.getRegionTypeId());
            sttmt.setInt(5, regionalManager.getId());

            sttmt.executeUpdate();

        }

    }

    //delete method
    public void delete(RegionalManager manager) throws SQLException {

        String sql = "DELETE FROM regional_manager WHERE id = ?";

        try (
                Connection conn = connectionFactory.getConnection();
                PreparedStatement sttmt = conn.prepareStatement(sql)
        ){

            sttmt.setInt(1, manager.getId());
            sttmt.executeUpdate();

        }

    }
}