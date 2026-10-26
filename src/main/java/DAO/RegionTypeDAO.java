package DAO;

import Model.RegionType;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class RegionTypeDAO {

    private final ConnectionFactory connectionFactory = new ConnectionFactory();

    //method that simply return the line of a SQL ResultSet
    private RegionType rsLine(ResultSet rs) throws SQLException {

        return new RegionType(
                rs.getInt("id"),
                rs.getString("name")
        );

    }

    //Select method for all regions
    public List<RegionType> select() throws SQLException {

        List<RegionType> regionTypes = new ArrayList<>();

        String sql = "SELECT * FROM region_type";

        try (Connection conn = connectionFactory.getConnection();
             PreparedStatement sttmt = conn.prepareStatement(sql);
             ResultSet result = sttmt.executeQuery()) {

            while (result.next()) {
                RegionType regionType = new RegionType();
                regionType.setId(result.getInt("id"));
                regionType.setName(result.getString("name"));
                regionTypes.add(regionType);
            }
        }
        return regionTypes;
    }

    //Insert method for a region
    public void createRegionType(RegionType regionType) throws SQLException {

        String sql = "INSERT INTO region_type (name) VALUES (?)";

        try (Connection conn = connectionFactory.getConnection();
             PreparedStatement sttmt = conn.prepareStatement(sql)){

            sttmt.setString(1, regionType.getName());
            sttmt.executeUpdate();
        }
    }

    //Search method of a region, based on id
    public RegionType searchById(int id) throws SQLException {

        String sql = "SELECT id, name FROM region_type WHERE id = ?";

        try (Connection conn = connectionFactory.getConnection();
             PreparedStatement sttmt = conn.prepareStatement(sql)) {

            sttmt.setInt(1, id);

            try (ResultSet rs = sttmt.executeQuery()) {
                return rsLine(rs);
            }
        }
    }

    //update method for region
    public void updateRegionType(RegionType regionType) throws SQLException{

        String sql = "UPDATE region_type SET name = ? WHERE id = ?";

        try (Connection conn = connectionFactory.getConnection();
             PreparedStatement sttmt = conn.prepareStatement(sql)){

            sttmt.setString(1, regionType.getName());
            sttmt.setInt(2, regionType.getId());
            sttmt.executeUpdate();
        }
    }

    //delete method
    public void deleteRegionType(int id) throws SQLException {

        String sql = "DELETE FROM region_type WHERE id = ?";

        try (Connection conn = connectionFactory.getConnection();
             PreparedStatement sttmt = conn.prepareStatement(sql)){
            sttmt.setInt(1, id);
            sttmt.executeUpdate();
        }
    }
}