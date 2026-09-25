package DAO;

import Model.TipoRegiao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class TipoRegiaoDAO {

    private final ConnectionFactory connectionFactory = new ConnectionFactory();

    //Metodo pra imprimir todos os tipos de regiao
    public List<TipoRegiao> readTiposRegiao() throws SQLException {

        List<TipoRegiao> tiposRegiao = new ArrayList<>();

        String sql = "SELECT * FROM tipo_regiao";

        try (Connection conn = connectionFactory.getConnection();
             PreparedStatement sttmt = conn.prepareStatement(sql);
             ResultSet result = sttmt.executeQuery()) {

            while (result.next()) {
                TipoRegiao tipoRegiao = new TipoRegiao();
                tipoRegiao.setCodigo(result.getInt("codigo"));
                tipoRegiao.setNome(result.getString("nome"));
                tipoRegiao.setLojaCodigo(result.getInt("loja_codigo"));
                tiposRegiao.add(tipoRegiao);
            }
        }
        return tiposRegiao;
    }

    //metodo pra criar um tipo de regiao novo
    public void createTipoRegiao(TipoRegiao tipoRegiao) throws SQLException {

        String sql = "INSERT INTO tipo_regiao (nome, loja_codigo) VALUES ( ?, ?)";

        try (Connection conn = connectionFactory.getConnection();
             PreparedStatement sttmt = conn.prepareStatement(sql)){

            sttmt.setString(1, tipoRegiao.getNome());
            sttmt.setInt(2, tipoRegiao.getLojaCodigo());
            sttmt.executeUpdate();
        }
    }

    //metodo pra encontrar o tipo de regiao que deseja alterar
    public TipoRegiao foundTipoRegiao(int codigo) throws SQLException {

        String sql = "SELECT * FROM tipo_regiao WHERE codigo = ?";

        try (Connection conn = connectionFactory.getConnection();
             PreparedStatement sttmt = conn.prepareStatement(sql)) {

            sttmt.setInt(1, codigo);

            try (ResultSet result = sttmt.executeQuery()) {
                if (result.next()) {
                    TipoRegiao tipoRegiao = new TipoRegiao();
                    tipoRegiao.setCodigo(result.getInt("codigo"));
                    tipoRegiao.setNome(result.getString("nome"));
                    tipoRegiao.setLojaCodigo(result.getInt("loja_codigo"));
                    return tipoRegiao;
                }
            }
            return null; //se nao encontrar nenhum tipo de regiao com esse codigo retorna null
        }
    }

    //metodo pra atualizar o tipo de regiao que encontrou no metodo passado
    public void updateTipoRegiao(TipoRegiao tipoRegiao) throws SQLException{

        String sql = "UPDATE tipo_regiao SET nome = ?, loja_codigo = ? WHERE codigo = ?";

        try (Connection conn = connectionFactory.getConnection();
             PreparedStatement sttmt = conn.prepareStatement(sql)){

            sttmt.setString(1, tipoRegiao.getNome());
            sttmt.setInt(2, tipoRegiao.getLojaCodigo());
            sttmt.setInt(3, tipoRegiao.getCodigo());
            sttmt.executeUpdate();
        }
    }

    //metodo pra excluir tipo de regiao do banco de acordo com o codigo
    public void deleteTipoRegiao(int codigo) throws SQLException {

        String sql = "DELETE FROM tipo_regiao WHERE codigo = ?";

        try (Connection conn = connectionFactory.getConnection();
             PreparedStatement sttmt = conn.prepareStatement(sql)){
            sttmt.setInt(1, codigo);
            sttmt.executeUpdate();
        }
    }
}