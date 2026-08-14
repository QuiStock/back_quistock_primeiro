package DAO;

import Model.Gerente;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class GerenteDAO {

    private final ConnectionFactory connectionFactory = new ConnectionFactory();

    //Metodo pra imprimir todos os gerentes
    public List<Gerente> read() throws SQLException {

        List<Gerente> gerentes = new ArrayList<>();

        String sql = "SELECT * FROM gerente_regional";


        //try pra listar tudo de todos os produtos
        try (Connection conn = ConnectionFactory.getConnection();
             PreparedStatement sttmt = conn.prepareStatement(sql);
             ResultSet result = sttmt.executeQuery()) {

            //laço pra repetir sempre q tiver produto na fila
            while (result.next()) {
                Gerente gerente = new Gerente();
                gerente.setId(result.getInt("id"));
                gerente.setNome(result.getString("nome"));
                gerente.setEmail(result.getString("email"));
                gerente.setSenha(result.getString("senha"));
                gerentes.add(gerente);
            }
        }
        return gerentes;
    }

    //metodo pra criar um gerente novo
    public void create(Gerente gerente) throws SQLException {

        String sql = "INSERT INTO gerente_regional (id, nome, email, senha) VALUES (?, ?, ?, ?)";

        //try pra adicionar informaçoes no novo gerente
        try (Connection conn = connectionFactory.getConnection();
             PreparedStatement sttmt = conn.prepareStatement(sql)){

            sttmt.setInt(1, gerente.getId());
            sttmt.setString(2, gerente.getNome());
            sttmt.setString(3, gerente.getEmail());
            sttmt.setString(4, gerente.getSenha());
            sttmt.executeUpdate();
        }
    }


    //metodo pra encontrar gerente que deseja ser alterado
    public Gerente foundGerente(int id) throws SQLException {

        String sql = "SELECT * FROM gerente_regional WHERE id = ?";

        //try pra conectar com o banco e executar a query
        try (Connection conn = connectionFactory.getConnection();
             PreparedStatement sttmt = conn.prepareStatement(sql)) {

            sttmt.setInt(1, id);

            //try pra retornar o gerente do id especificadox
            try (ResultSet result = sttmt.executeQuery()) {
                if (result.next()) {
                    Gerente gerente = new Gerente();
                    gerente.setId(result.getInt("id"));
                    gerente.setNome(result.getString("nome"));
                    gerente.setEmail(result.getString("email"));
                    gerente.setSenha(result.getString("senha"));
                    return gerente;
                }
            }
            return null; //se nao encontrar nenhum gerente com esse id retorna null
        }
    }

    //metodo pra atualizar o gerente que encontrou no metodo passado
    public void update(Gerente gerente) throws SQLException{

        String sql = "UPDATE gerente_regional SET nome = ?, email = ?, senha = ? WHERE id = ?";

        try (Connection conn = connectionFactory.getConnection();
             PreparedStatement sttmt = conn.prepareStatement(sql)){

            sttmt.setString(1, gerente.getNome());
            sttmt.setString(2, gerente.getEmail());
            sttmt.setString(3, gerente.getSenha());
            sttmt.setInt(4, gerente.getId());
            sttmt.executeUpdate();
        }
    }

    //metodo pra excluir gerente do banco de acordo com o id
    public void delete(int id) throws SQLException {

        String sql = "DELETE FROM gerente_regional WHERE id = ?";

        try (Connection conn = connectionFactory.getConnection();
            PreparedStatement sttmt = conn.prepareStatement(sql)){
            sttmt.setInt(1, id);
            sttmt.executeUpdate();
        }
    }
}