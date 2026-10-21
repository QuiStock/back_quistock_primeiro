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

        String sql = "SELECT id, name, email, password, region FROM regional_manager_vw order by id;";

        //try pra listar tudo de todos os gerentes
        try (Connection conn = connectionFactory.getConnection();
             PreparedStatement sttmt = conn.prepareStatement(sql);
             ResultSet result = sttmt.executeQuery()) {

            //laço pra repetir sempre q tiver gerente na fila
            while (result.next()) {
                RegionalManager regionalManager = new RegionalManager();
                regionalManager.setId(result.getInt("id"));
                regionalManager.setName(result.getString("name"));
                regionalManager.setEmail(result.getString("email"));
                regionalManager.setPassword(result.getString("password"));
                regionalManager.setRegionType(result.getString("region"));
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
            sttmt.setString(4, regionalManager.getRegionType());
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
                    regionalManager.setRegionType(result.getString("region_type_id"));
                    return regionalManager;
                }
            }
            return null; //se nao encontrar nenhum gerente com esse id retorna null
        }
    }

    //metodo pra atualizar o gerente que encontrou no metodo passado
    public void update(RegionalManager regionalManager) throws SQLException{

        String sql = "UPDATE regional_manager SET name = ?, email = ?, password = ?, region_type_id = ? WHERE id = ?";

        try (Connection conn = connectionFactory.getConnection();
             PreparedStatement sttmt = conn.prepareStatement(sql)){

            sttmt.setString(1, regionalManager.getName());
            sttmt.setString(2, regionalManager.getEmail());
            sttmt.setString(3, regionalManager.getPassword());
            sttmt.setString(4, regionalManager.getRegionType());
            sttmt.setInt(5, regionalManager.getId());
            sttmt.executeUpdate();
        }
    }

    //metodo pra excluir gerente do banco de acordo com o id
    public void delete(int id) throws SQLException {

        String sql = "DELETE FROM regional_manager WHERE id = ?";

        try (Connection conn = connectionFactory.getConnection();
             PreparedStatement sttmt = conn.prepareStatement(sql)){
            sttmt.setInt(1, id);
            sttmt.executeUpdate();
        }
    }
}