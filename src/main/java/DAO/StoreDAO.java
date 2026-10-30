package DAO;

import Model.Store;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class StoreDAO {

    private final ConnectionFactory connectionFactory = new ConnectionFactory();

    public List<Store> readStores() throws SQLException {

        List<Store> stores = new ArrayList<>();

        // The list only needs public store details.
        String sql = "SELECT s.id, s.email, s.region_type_id, r.name AS region_name " +
                "FROM store s JOIN region_type r ON r.id = s.region_type_id ORDER BY s.id";


        try (Connection conn = connectionFactory.getConnection();
             PreparedStatement sttmt = conn.prepareStatement(sql);
             ResultSet result = sttmt.executeQuery()) {

            while (result.next()) {
                Store store = new Store();
                store.setId(result.getInt("id"));
                store.setEmail(result.getString("email"));
                store.setRegionTypeId(result.getInt("region_type_id"));
                store.setRegionName(result.getString("region_name"));
                stores.add(store);
            }
        }
        return stores;
    }

    public void createStore(Store store) throws SQLException {

        String sql = "INSERT INTO store (email, password, region_type_id) VALUES (?, ?, ?)";

        try (Connection conn = connectionFactory.getConnection();
             PreparedStatement sttmt = conn.prepareStatement(sql)){

            sttmt.setString(1, store.getEmail());
            sttmt.setString(2, store.getPassword());
            sttmt.setInt(3, store.getRegionTypeId());
            sttmt.executeUpdate();
        }
    }


    public Store foundStore(int id) throws SQLException {

        String sql = "SELECT s.id, s.email, s.password, s.region_type_id, r.name AS region_name " +
                "FROM store s JOIN region_type r ON r.id = s.region_type_id WHERE s.id = ?";

        try (Connection conn = connectionFactory.getConnection();
             PreparedStatement sttmt = conn.prepareStatement(sql)) {

            sttmt.setInt(1, id);

            try (ResultSet result = sttmt.executeQuery()) {
                if (result.next()) {
                    Store store = new Store();
                    store.setId(result.getInt("id"));
                    store.setEmail(result.getString("email"));
                    store.setPassword(result.getString("password"));
                    store.setRegionTypeId(result.getInt("region_type_id"));
                    store.setRegionName(result.getString("region_name"));
                    return store;
                }
            }
            return null;
        }
    }

    public int updateStore(Store store) throws SQLException{

        String sql = "UPDATE store SET email = ?, password = ?, region_type_id = ? WHERE id = ?";

        try (Connection conn = connectionFactory.getConnection();
             PreparedStatement sttmt = conn.prepareStatement(sql)){

            sttmt.setString(1, store.getEmail());
            sttmt.setString(2, store.getPassword());
            sttmt.setInt(3, store.getRegionTypeId());
            sttmt.setInt(4, store.getId());
            return sttmt.executeUpdate();
        }
    }

    public int deleteStore(int id) throws SQLException {

        String sql = "DELETE FROM store WHERE id = ?";

        try (Connection conn = connectionFactory.getConnection();
             PreparedStatement sttmt = conn.prepareStatement(sql)){
            sttmt.setInt(1, id);
            return sttmt.executeUpdate();
        }
    }
}
