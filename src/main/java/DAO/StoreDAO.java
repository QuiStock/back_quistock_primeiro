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

    //Metodo pra imprimir todas as lojas
    public List<Store> readStores() throws SQLException {

        List<Store> stores = new ArrayList<>();

        String sql = "SELECT * FROM store";


        //try pra listar tudo de todas as lojas
        try (Connection conn = connectionFactory.getConnection();
             PreparedStatement sttmt = conn.prepareStatement(sql);
             ResultSet result = sttmt.executeQuery()) {

            //laço pra repetir sempre q tiver loja na fila
            while (result.next()) {
                Store store = new Store();
                store.setId(result.getInt("id"));
                store.setEmail(result.getString("email"));
                store.setPassword(result.getString("password"));
                stores.add(store);
            }
        }
        return stores;
    }

    //metodo pra criar uma loja nova
    public void createStore(Store store) throws SQLException {

        String sql = "INSERT INTO store (email, password) VALUES ( ?, ?)";

        //try pra adicionar informaçoes na nova loja
        try (Connection conn = connectionFactory.getConnection();
             PreparedStatement sttmt = conn.prepareStatement(sql)){

            sttmt.setString(1, store.getEmail());
            sttmt.setString(2, store.getPassword());
            sttmt.executeUpdate();
        }
    }


    //metodo pra encontrar a loja que deseja alterar
    public Store foundStore(int id) throws SQLException {

        String sql = "SELECT * FROM store WHERE id = ?";

        //try pra conectar com o banco e executar a query
        try (Connection conn = connectionFactory.getConnection();
             PreparedStatement sttmt = conn.prepareStatement(sql)) {

            sttmt.setInt(1, id);

            //try pra retornar a loja do id especificadox
            try (ResultSet result = sttmt.executeQuery()) {
                if (result.next()) {
                    Store store = new Store();
                    store.setId(result.getInt("id"));
                    store.setEmail(result.getString("email"));
                    store.setPassword(result.getString("password"));
                    return store;
                }
            }
            return null; //se nao encontrar nenhuma loja com esse id retorna null
        }
    }

    //metodo pra atualizar a loja que encontrou no metodo passado
    public void updateStore(Store store) throws SQLException{

        String sql = "UPDATE store SET email = ?, password = ? WHERE id = ?";

        try (Connection conn = connectionFactory.getConnection();
             PreparedStatement sttmt = conn.prepareStatement(sql)){

            sttmt.setString(1, store.getEmail());
            sttmt.setString(2, store.getPassword());
            sttmt.setInt(3, store.getId());
            sttmt.executeUpdate();
        }
    }

    //metodo pra excluir loja do banco de acordo com o id
    public void deleteStore(int id) throws SQLException {

        String sql = "DELETE FROM store WHERE id = ?";

        try (Connection conn = connectionFactory.getConnection();
             PreparedStatement sttmt = conn.prepareStatement(sql)){
            sttmt.setInt(1, id);
            sttmt.executeUpdate();
        }
    }
}