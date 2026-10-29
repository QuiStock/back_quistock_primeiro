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

    //metodo pra retornar uma lista de todos os gerentes regionais cadastrados
    public List<RegionalManager> select() throws SQLException {

        List<RegionalManager> regionalManagers = new ArrayList<>();

        try (Connection conn = connectionFactory.getConnection();
             PreparedStatement sttmt = conn.prepareStatement(query + "ORDER BY rm.id");
             ResultSet rs = sttmt.executeQuery()) {

            //repete o while enquanto ainda tem gerente na fila
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

    //visual interface for using the lambda method
    private interface Binder{
        //metodo do lambda que permite utilizar diferentes querys
        void bind(PreparedStatement stmt) throws SQLException;
    }

    //method that searches for a manager by id
    public RegionalManager searchById(int id) throws SQLException {
        return searchBy("where rm.id = ?", stmt -> stmt.setInt(1, id));
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

    //retrieves the method base on email, with lambda
    public RegionalManager searchByEmail(String email) throws SQLException {
        return searchBy("WHERE rm.email = ?",  stmt -> stmt.setString(1, email));
    }

    //update the data and save the password in hash
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

    //method that delete a manager by ID
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
