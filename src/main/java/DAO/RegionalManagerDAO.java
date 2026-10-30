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

    private static final String query = "SELECT rm.id, rm.name, rm.email, rm.password, rm.region_type_id, rt.name AS region_name " +
            "FROM regional_manager rm JOIN region_type rt ON rt.id = rm.region_type_id ";

    // Map one result row to a regional manager.
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

    // Handles select
    public List<RegionalManager> select() throws SQLException {

        List<RegionalManager> regionalManagers = new ArrayList<>();

        try (Connection conn = connectionFactory.getConnection();
             PreparedStatement sttmt = conn.prepareStatement(query + "ORDER BY rm.id");
             ResultSet rs = sttmt.executeQuery()) {

            while (rs.next()) {
                regionalManagers.add(rsLine(rs));
            }
        }

        return regionalManagers;
    }

    // Handles insert
    public void insert(RegionalManager regionalManager) throws SQLException {

        String sql = "INSERT INTO regional_manager (name, email, password, region_type_id) VALUES ( ?, ?, ?, ?)";

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

    private interface Binder{
        void bind(PreparedStatement stmt) throws SQLException;
    }

    // Handles search, By, Id
    public RegionalManager searchById(int id) throws SQLException {
        // Applying the method logic, using the provided values
        return searchBy("where rm.id = ?", stmt -> stmt.setInt(1, id));
    }

    // Share the lookup logic between ID and email searches.
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

    // Handles search, By, Email
    public RegionalManager searchByEmail(String email) throws SQLException {
        // Applying the method logic, using the provided values
        return searchBy("WHERE rm.email = ?",  stmt -> stmt.setString(1, email));
    }

    // Handles update
    public int update(RegionalManager regionalManager) throws SQLException{

        String sql = "UPDATE regional_manager SET name = ?, email = ?, password = ?, region_type_id = ? WHERE id = ?";

        try (Connection conn = connectionFactory.getConnection();
             PreparedStatement sttmt = conn.prepareStatement(sql)){

            sttmt.setString(1, regionalManager.getName());
            sttmt.setString(2, regionalManager.getEmail());
            sttmt.setString(3, regionalManager.getPassword());
            sttmt.setInt(4, regionalManager.getRegionTypeId());
            sttmt.setInt(5, regionalManager.getId());

            return sttmt.executeUpdate();

        }

    }

    // Handles delete
    public int delete(int id) throws SQLException {

        String sql = "DELETE FROM regional_manager WHERE id = ?";

        try (
                Connection conn = connectionFactory.getConnection();
                PreparedStatement sttmt = conn.prepareStatement(sql)
        ){

            sttmt.setInt(1, id);
            return sttmt.executeUpdate();

        }

    }
}
