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

    //Metodo pra imprimir todos os gerentes
    public List<RegionalManager> select() throws SQLException {

        List<RegionalManager> regionalManagers = new ArrayList<>();

        String sql = "SELECT id, name, email, password, region_type_id, region_name FROM regional_manager_vw order by id;";

        //try pra listar tudo de todos os gerentes
        try (Connection conn = connectionFactory.getConnection();
             PreparedStatement sttmt = conn.prepareStatement(sql);
             ResultSet rs = sttmt.executeQuery()) {

            //laço pra repetir sempre q tiver gerente na fila
            while (rs.next()) {

                RegionalManager regionalManager = new RegionalManager(

                        rs.getInt("id"),
                        rs.getString("email"),
                        rs.getString("name"),
                        rs.getString("password"),
                        rs.getString("region_name"),
                        rs.getInt("region_type_id")

                );

                regionalManagers.add(regionalManager);
            }
        }
        return regionalManagers;
    }

    //metodo pra criar um gerente novo
    public void insert(RegionalManager regionalManager) throws SQLException {

        String sql = "INSERT INTO regional_manager (name, email, password, region_type_id) VALUES ( ?, ?, ?, ?)";

        //try pra adicionar informaçoes no novo gerente
        try (Connection conn = connectionFactory.getConnection();
             PreparedStatement sttmt = conn.prepareStatement(sql)){

            sttmt.setString(1, regionalManager.getName());
            sttmt.setString(2, regionalManager.getEmail());
            sttmt.setString(3, regionalManager.getPassword());
            sttmt.setInt(4, regionalManager.getRegionTypeId());
            sttmt.executeUpdate();
        }
    }


    //metodo pra encontrar gerente que deseja ser alterado
    public RegionalManager searchManager(int id) throws SQLException {

        String sql = "SELECT * FROM regional_manager WHERE id = ?";

        //try pra conectar com o banco e executar a query
        try (Connection conn = connectionFactory.getConnection();
             PreparedStatement sttmt = conn.prepareStatement(sql)) {

            sttmt.setInt(1, id);

            //try pra retornar o gerente do id especificado
            try (ResultSet result = sttmt.executeQuery()) {

                if (result.next()) {

                    RegionalManager regionalManager = new RegionalManager();
                    regionalManager.setId(result.getInt("id"));
                    regionalManager.setName(result.getString("name"));
                    regionalManager.setEmail(result.getString("email"));
                    regionalManager.setPassword(result.getString("password"));
                    regionalManager.setRegionTypeId(result.getInt("region_type_id"));

                    return regionalManager;

                }
            }
            return null; //se nao encontrar nenhum gerente com esse id retorna null
        }
    }

    public RegionalManager searchByEmail(String email) throws SQLException {
        String query = "select id, name, email, password, region_type_id from regional_manager where email = ?";

        try (Connection conn = connectionFactory.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {

            stmt.setString(1, email);

            try (ResultSet rs = stmt.executeQuery()) {

                if (rs.next()) {

                    RegionalManager manager = new RegionalManager();
                    manager.setId(rs.getInt("id"));
                    manager.setName(rs.getString("name"));
                    manager.setEmail(rs.getString("email"));
                    manager.setPassword(rs.getString("password"));
                    manager.setRegionTypeId(rs.getInt("region_type_id"));

                    return manager;

                }
            }
        }
        return null;
    }

    //metodo pra atualizar o gerente que encontrou no metodo passado
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

    //metodo pra excluir gerente do banco de acordo com o id
    public void delete(RegionalManager manager) throws SQLException {

        String sql = "DELETE FROM regional_manager WHERE id = ?";

        try (Connection conn = connectionFactory.getConnection();
             PreparedStatement sttmt = conn.prepareStatement(sql)){
            sttmt.setInt(1, manager.getId());
            sttmt.executeUpdate();
        }
    }
}