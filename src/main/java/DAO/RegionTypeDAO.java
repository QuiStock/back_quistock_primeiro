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

        String sql = "SELECT id, name FROM region_type ORDER BY id";

        //try pra conectar com o banco e definir as variaveis do tipo de regiao pra listar
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

        //retorna o tipo de regiao criado pra aparecer na lista
        return regionTypes;
    }

    //Insert method for a region
    public void createRegionType(RegionType regionType) throws SQLException {

        //regiao agora guarda so o nome, sem vinculo com loja
        String sql = "INSERT INTO region_type (name) VALUES (?)";

        //try pra criar tipos de regiao
        try (Connection conn = connectionFactory.getConnection();
             PreparedStatement sttmt = conn.prepareStatement(sql)){

            sttmt.setString(1, regionType.getName());
            sttmt.executeUpdate();
        }
    }

    //Search method of a region, based on id
    public RegionType searchById(int id) throws SQLException {

        String sql = "SELECT id, name FROM region_type WHERE id = ?";

        //try pra conectar com o banco e definir o tipo de regiao que a pessoa quer editar pra aparecer no popup
        try (Connection conn = connectionFactory.getConnection();
             PreparedStatement sttmt = conn.prepareStatement(sql)) {

            sttmt.setInt(1, id);

            try (ResultSet result = sttmt.executeQuery()) {
                return result.next() ? rsLine(result) : null;
            }
        }
    }

    //metodo pra atualizar o tipo de regiao que encontrou no metodo passado
    public int updateRegionType(RegionType regionType) throws SQLException{

        String sql = "UPDATE region_type SET name = ? WHERE id = ?";

        //try pra conectar com o banco, e atualizar as informações do tipo de regiao
        try (Connection conn = connectionFactory.getConnection();
             PreparedStatement sttmt = conn.prepareStatement(sql)){

            sttmt.setString(1, regionType.getName());
            sttmt.setInt(2, regionType.getId());
            return sttmt.executeUpdate();
        }
    }

    //metodo pra excluir tipo de regiao do banco de acordo com o id
    public int deleteRegionType(int id) throws SQLException {

        String sql = "DELETE FROM region_type WHERE id = ?";

        //try pra encontrar o tipo de regiao baseado no id e exclui-lo
        try (Connection conn = connectionFactory.getConnection();
             PreparedStatement sttmt = conn.prepareStatement(sql)){
            sttmt.setInt(1, id);
            return sttmt.executeUpdate();
        }
    }
}
