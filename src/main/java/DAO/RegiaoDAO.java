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

    //Metodo pra imprimir todas as regioes
    public List<Regiao> read() throws SQLException {

        List<Regiao> regioes = new ArrayList<>();

        String sql = "SELECT * FROM regiao";


        //try pra listar tudo de todas as regioes
        try (Connection conn = connectionFactory.getConnection();
             PreparedStatement sttmt = conn.prepareStatement(sql);
             ResultSet result = sttmt.executeQuery()) {

            //laço pra repetir sempre q tiver regiao na fila
            while (result.next()) {
                Regiao regiao = new Regiao();
                regiao.setCodigo(result.getInt("codigo"));
                regiao.setNome(result.getString("nome"));
                regiao.setLoja_codigo(result.getInt("loja_codigo"));
                regioes.add(regiao);
            }
        }
        return regioes;
    }

    //metodo pra criar uma regiao nova
    public void create(Regiao regiao) throws SQLException {

        String sql = "INSERT INTO regiao (nome, loja_codigo) VALUES (?, ?)";

        //try pra adicionar informaçoes na nova regiao
        try (Connection conn = connectionFactory.getConnection();
             PreparedStatement sttmt = conn.prepareStatement(sql)){

            sttmt.setString(1, regiao.getNome());
            sttmt.setInt(2, regiao.getLoja_codigo());
            sttmt.executeUpdate();
        }
    }


    //metodo pra encontrar regiao que deseja ser alterada
    /*public Regiao foundRegiao(int codigo) throws SQLException {

        String sql = "SELECT * FROM regiao WHERE codigo = ?";

        //try pra conectar com o banco e executar a query
        try (Connection conn = connectionFactory.getConnection();
             PreparedStatement sttmt = conn.prepareStatement(sql)) {

            sttmt.setInt(1, codigo);

            //try pra retornar a regiao do id especificadox
            try (ResultSet result = sttmt.executeQuery()) {
                if (result.next()) {
                    Regiao regiao = new Regiao();
                    regiao.setCodigo(result.getInt("codigo"));
                    regiao.setNome(result.getString("nome"));
                    regiao.setLoja_codigo(result.getInt("loja_codigo"));
                    return regiao;
                }
            }
            return null; //se nao encontrar nenhuma regiao com esse codigo retorna null
        }
    }*/

    //metodo pra atualizar a regiao que encontrou no metodo passado
    public void update(Regiao regiao) throws SQLException{

        String sql = "UPDATE regiao SET nome = ?, loja_codigo = ? WHERE codigo = ?";

        try (Connection conn = connectionFactory.getConnection();
             PreparedStatement sttmt = conn.prepareStatement(sql)){

            sttmt.setString(1, regiao.getNome());
            sttmt.setInt(2, regiao.getLoja_codigo());
            sttmt.setInt(3, regiao.getCodigo());
            sttmt.executeUpdate();
        }
    }

    //metodo pra excluir regiao do banco de acordo com o id
    public void delete(Regiao regiao) throws SQLException {

        String sql = "DELETE FROM regiao WHERE codigo = ?";

        try (Connection conn = connectionFactory.getConnection();
             PreparedStatement sttmt = conn.prepareStatement(sql)){
            sttmt.setInt(1, regiao.getCodigo());
            sttmt.executeUpdate();
        }
    }
}