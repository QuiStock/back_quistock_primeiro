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

    //Metodo pra imprimir todos os tipos de regiao
    public List<RegionType> readRegionTypes() throws SQLException {

        List<RegionType> regionTypes = new ArrayList<>();

        String sql = "SELECT * FROM region_type";

        try (Connection conn = connectionFactory.getConnection();
             PreparedStatement sttmt = conn.prepareStatement(sql);
             ResultSet result = sttmt.executeQuery()) {

            while (result.next()) {
                RegionType regionType = new RegionType();
                regionType.setId(result.getInt("id"));
                regionType.setName(result.getString("name"));
                regionType.setStoreId(result.getInt("store_id"));
                regionTypes.add(regionType);
            }
        }
        return regionTypes;
    }

    //metodo pra criar um tipo de regiao novo
    public void createRegionType(RegionType regionType) throws SQLException {

        String sql = "INSERT INTO region_type (name, store_id) VALUES ( ?, ?)";

        try (Connection conn = connectionFactory.getConnection();
             PreparedStatement sttmt = conn.prepareStatement(sql)){

            sttmt.setString(1, regionType.getName());
            sttmt.setInt(2, regionType.getStoreId());
            sttmt.executeUpdate();
        }
    }

    //metodo pra encontrar o tipo de regiao que deseja alterar
    public RegionType foundRegionType(int id) throws SQLException {

        String sql = "SELECT * FROM region_type WHERE id = ?";

        try (Connection conn = connectionFactory.getConnection();
             PreparedStatement sttmt = conn.prepareStatement(sql)) {

            sttmt.setInt(1, id);

            try (ResultSet result = sttmt.executeQuery()) {
                if (result.next()) {
                    RegionType regionType = new RegionType();
                    regionType.setId(result.getInt("id"));
                    regionType.setName(result.getString("name"));
                    regionType.setStoreId(result.getInt("store_id"));
                    return regionType;
                }
            }
            return null; //se nao encontrar nenhum tipo de regiao com esse id retorna null
        }
    }

    //metodo pra atualizar o tipo de regiao que encontrou no metodo passado
    public void updateRegionType(RegionType regionType) throws SQLException{

        String sql = "UPDATE region_type SET name = ?, store_id = ? WHERE id = ?";

        try (Connection conn = connectionFactory.getConnection();
             PreparedStatement sttmt = conn.prepareStatement(sql)){

            sttmt.setString(1, regionType.getName());
            sttmt.setInt(2, regionType.getStoreId());
            sttmt.setInt(3, regionType.getId());
            sttmt.executeUpdate();
        }
    }

    //metodo pra excluir tipo de regiao do banco de acordo com o id
    public void deleteRegionType(int id) throws SQLException {

        String sql = "DELETE FROM region_type WHERE id = ?";

        try (Connection conn = connectionFactory.getConnection();
             PreparedStatement sttmt = conn.prepareStatement(sql)){
            sttmt.setInt(1, id);
            sttmt.executeUpdate();
        }
    }
}