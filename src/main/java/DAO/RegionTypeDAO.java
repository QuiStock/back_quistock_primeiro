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

    // Map one result row to a region type.
    private RegionType rsLine(ResultSet rs) throws SQLException {

        return new RegionType(
                rs.getInt("id"),
                rs.getString("name")
        );

    }

    public List<RegionType> select() throws SQLException {

        List<RegionType> regionTypes = new ArrayList<>();

        String sql = "SELECT id, name FROM region_type ORDER BY id";

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

    public void createRegionType(RegionType regionType) throws SQLException {

        // Region types are stored independently from stores.
        String sql = "INSERT INTO region_type (name) VALUES (?)";

        try (Connection conn = connectionFactory.getConnection();
             PreparedStatement sttmt = conn.prepareStatement(sql)){

            sttmt.setString(1, regionType.getName());
            sttmt.executeUpdate();
        }
    }

    public RegionType searchById(int id) throws SQLException {

        String sql = "SELECT id, name FROM region_type WHERE id = ?";

        try (Connection conn = connectionFactory.getConnection();
             PreparedStatement sttmt = conn.prepareStatement(sql)) {

            sttmt.setInt(1, id);

            try (ResultSet result = sttmt.executeQuery()) {
                return result.next() ? rsLine(result) : null;
            }
        }
    }

    public int updateRegionType(RegionType regionType) throws SQLException{

        String sql = "UPDATE region_type SET name = ? WHERE id = ?";

        try (Connection conn = connectionFactory.getConnection();
             PreparedStatement sttmt = conn.prepareStatement(sql)){

            sttmt.setString(1, regionType.getName());
            sttmt.setInt(2, regionType.getId());
            return sttmt.executeUpdate();
        }
    }

    public int deleteRegionType(int id) throws SQLException {

        String sql = "DELETE FROM region_type WHERE id = ?";

        try (Connection conn = connectionFactory.getConnection();
             PreparedStatement sttmt = conn.prepareStatement(sql)){
            sttmt.setInt(1, id);
            return sttmt.executeUpdate();
        }
    }
}
