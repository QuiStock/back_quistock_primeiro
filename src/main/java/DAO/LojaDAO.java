package DAO;

import Model.Loja;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class LojaDAO {

    private final ConnectionFactory connectionFactory = new ConnectionFactory();

    //Metodo pra imprimir todas as lojas
    public List<Loja> read() throws SQLException {

        List<Loja> lojas = new ArrayList<>();

        String sql = "SELECT * FROM loja";


        //try pra listar tudo de todas as lojas
        try (Connection conn = connectionFactory.getConnection();
             PreparedStatement sttmt = conn.prepareStatement(sql);
             ResultSet result = sttmt.executeQuery()) {

            //laço pra repetir sempre q tiver loja na fila
            while (result.next()) {
                Loja loja = new Loja();
                loja.setId(result.getInt("id"));
                loja.setEmail(result.getString("email"));
                loja.setSenha(result.getString("senha"));
                loja.setEndereco_id(result.getString("endereco_id"));
                loja.setRegiao_id(result.getString("regiao_id"));
                lojas.add(loja);
            }
        }
        return lojas;
    }

    //metodo pra criar uma loja nova
    public void create(Loja loja) throws SQLException {

        String sql = "INSERT INTO loja (id, email, senha, endereco_id, regiao_id) VALUES (?, ?, ?, ?, ?)";

        //try pra adicionar as informaçoes na nova loja
        try (Connection conn = connectionFactory.getConnection();
             PreparedStatement sttmt = conn.prepareStatement(sql)){

            sttmt.setInt(1, loja.getId());
            sttmt.setString(2, loja.getEmail());
            sttmt.setString(3, loja.getSenha());
            sttmt.setString(4, loja.getEndereco_id());
            sttmt.setString(5, loja.getRegiao_id());
            sttmt.executeUpdate();
        }
    }


    //metodo pra encontrar a loja que deseja ser alterada
    public Loja foundLoja(int id) throws SQLException {

        String sql = "SELECT * FROM loja WHERE id = ?";

        //try pra conectar com o banco e executar a query
        try (Connection conn = connectionFactory.getConnection();
             PreparedStatement sttmt = conn.prepareStatement(sql)) {

            sttmt.setInt(1, id);

            //try pra retornar o gerente do id especificadox
            try (ResultSet result = sttmt.executeQuery()) {
                if (result.next()) {
                    Loja loja = new Loja();
                    loja.setId(result.getInt("id"));
                    loja.setEmail(result.getString("email"));
                    loja.setSenha(result.getString("senha"));
                    loja.setEndereco_id(result.getString("endereco_id"));
                    loja.setRegiao_id(result.getString("regiao_id"));
                    return loja;
                }
            }
            return null; //se nao encontrar nenhuma loja com esse id retorna null
        }
    }

    //metodo pra atualizar a loja que encontrou no metodo passado
    public void update(Loja loja, int id) throws SQLException{

        String sql = "UPDATE loja SET email = ?, senha = ?, endereco_id = ?, regiao_id = ? WHERE id = ?";

        try (Connection conn = connectionFactory.getConnection();
             PreparedStatement sttmt = conn.prepareStatement(sql)){
            
            sttmt.setString(1, loja.getEmail());
            sttmt.setString(2, loja.getSenha());
            sttmt.setString(3, loja.getEndereco_id());
            sttmt.setString(4, loja.getRegiao_id());
            sttmt.setInt(5, id);
            sttmt.executeUpdate();
        }
    }

    //metodo pra excluir a loja do banco de acordo com o id
    public void delete(int id) throws SQLException {

        String sql = "DELETE FROM loja WHERE id = ?";

        try (Connection conn = connectionFactory.getConnection();
             PreparedStatement sttmt = conn.prepareStatement(sql)){
            sttmt.setInt(1, id);
            sttmt.executeUpdate();
        }
    }
}