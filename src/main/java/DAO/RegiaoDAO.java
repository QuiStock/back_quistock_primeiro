package DAO;

import Model.Regiao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class RegiaoDAO {

    private final ConnectionFactory connectionFactory = new ConnectionFactory();

    //Metodo pra imprimir todos os gerentes
    public List<Regiao> read() throws SQLException {

        List<Regiao> regioes = new ArrayList<>();

        String sql = "SELECT * FROM regiao";


        //try pra listar tudo de todos os produtos
        try (Connection conn = connectionFactory.getConnection();
             PreparedStatement sttmt = conn.prepareStatement(sql);
             ResultSet result = sttmt.executeQuery()) {

            //laço pra repetir sempre q tiver produto na fila
            while (result.next()) {
                Regiao regiao = new Regiao();
                regiao.setId(result.getInt("id"));
                regiao.setNome(result.getString("nome"));
                regiao.setGerente_regional_id(result.getInt("gerente_regional_id"));
                regioes.add(regiao);
            }
        }
        return regioes;
    }

    //metodo pra criar uma regiao nova
    public void create(Regiao regiao) throws SQLException {

        String sql = "INSERT INTO regiao (id, nome, gerente_regional_id) VALUES (?, ?, ?)";

        //try pra adicionar informaçoes no novo gerente
        try (Connection conn = connectionFactory.getConnection();
             PreparedStatement sttmt = conn.prepareStatement(sql)){

            sttmt.setInt(1, regiao.getId());
            sttmt.setString(2, regiao.getNome());
            sttmt.setInt(3, regiao.getGerente_regional_id());
            sttmt.executeUpdate();
        }
    }


    //metodo pra encontrar regiao que deseja ser alterado
    public Regiao foundRegiao(int id) throws SQLException {

        String sql = "SELECT * FROM regiao WHERE id = ?";

        //try pra conectar com o banco e executar a query
        try (Connection conn = connectionFactory.getConnection();
             PreparedStatement sttmt = conn.prepareStatement(sql)) {

            sttmt.setInt(1, id);

            //try pra retornar a regiao do id especificadox
            try (ResultSet result = sttmt.executeQuery()) {
                if (result.next()) {
                    Regiao regiao = new Regiao();
                    regiao.setId(result.getInt("id"));
                    regiao.setNome(result.getString("nome"));
                    regiao.setGerente_regional_id(result.getInt("gerente_regional_id"));
                    return regiao;
                }
            }
            return null; //se nao encontrar nenhuma regiao com esse id retorna null
        }
    }

    //metodo pra atualizar a regiao que encontrou no metodo passado
    public void update(Regiao regiao) throws SQLException{

        String sql = "UPDATE regiao SET nome = ?, gerente_regional_id = ? WHERE id = ?";

        try (Connection conn = connectionFactory.getConnection();
             PreparedStatement sttmt = conn.prepareStatement(sql)){

            sttmt.setString(1, regiao.getNome());
            sttmt.setInt(2, regiao.getGerente_regional_id());
            sttmt.setInt(3, regiao.getId());
            sttmt.executeUpdate();
        }
    }

    //metodo pra excluir regiao do banco de acordo com o id
    public void delete(int id) throws SQLException {

        String sql = "DELETE FROM regiao WHERE id = ?";

        try (Connection conn = connectionFactory.getConnection();
             PreparedStatement sttmt = conn.prepareStatement(sql)){
            sttmt.setInt(1, id);
            sttmt.executeUpdate();
        }
    }
}