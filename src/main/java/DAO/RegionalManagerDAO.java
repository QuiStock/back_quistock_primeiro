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

    //metodo pra retornar uma lista de todos os gerentes regionais cadastrados
    public List<RegionalManager> select() throws SQLException {

        List<RegionalManager> regionalManagers = new ArrayList<>();

        try (Connection conn = connectionFactory.getConnection();
             PreparedStatement sttmt = conn.prepareStatement(query + "ORDER BY m.id");
             ResultSet rs = sttmt.executeQuery()) {

            //repete o while enquanto ainda tem gerente na fila
            while (rs.next()) {
                regionalManagers.add(rsLine(rs));
            }
        }

        return regionalManagers;
    }

    //metodo pra criar um gerente novo
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

    //interface visual pra usar metodo do lambda
    private interface Binder{
        //metodo do lambda que permite utilizar diferentes querys
        void bind(PreparedStatement stmt) throws SQLException;
    }

    //metodo que procura o gerente de acordo com o ID
    public RegionalManager searchById(int id) throws SQLException {
        return searchBy("where m.id = ?", stmt -> stmt.setInt(1, id));
    }

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

    //procura o metodo baseado no email, utilizando lambda
    public RegionalManager searchByEmail(String email) throws SQLException {
        return searchBy("WHERE m.email = ?",  stmt -> stmt.setString(1, email));
    }

    //atualiza nome, email e regiao sem alterar a senha
    public int update(RegionalManager regionalManager) throws SQLException{

        String sql = "UPDATE regional_manager SET name = ?, email = ?, region_type_id = ? WHERE id = ?";

        try (Connection conn = connectionFactory.getConnection();
             PreparedStatement sttmt = conn.prepareStatement(sql)){

            sttmt.setString(1, regionalManager.getName());
            sttmt.setString(2, regionalManager.getEmail());
            sttmt.setInt(3, regionalManager.getRegionTypeId());
            sttmt.setInt(4, regionalManager.getId());

            return sttmt.executeUpdate();

        }

    }

    //metodo pra excluir gerente do banco de acordo com o id
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
