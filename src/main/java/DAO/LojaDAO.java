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
    public List<Loja> readLojas() throws SQLException {

        List<Loja> lojas = new ArrayList<>();

        String sql = "SELECT * FROM loja";


        //try pra listar tudo de todas as lojas
        try (Connection conn = connectionFactory.getConnection();
             PreparedStatement sttmt = conn.prepareStatement(sql);
             ResultSet result = sttmt.executeQuery()) {

            //laço pra repetir sempre q tiver loja na fila
            while (result.next()) {
                Loja loja = new Loja();
                loja.setCodigo(result.getInt("codigo"));
                loja.setEmail(result.getString("email"));
                loja.setSenha(result.getString("senha"));
                lojas.add(loja);
            }
        }
        return lojas;
    }

    //metodo pra criar uma loja nova
    public void createLoja(Loja loja) throws SQLException {

        String sql = "INSERT INTO loja (email, senha) VALUES ( ?, ?)";

        //try pra adicionar informaçoes na nova loja
        try (Connection conn = connectionFactory.getConnection();
             PreparedStatement sttmt = conn.prepareStatement(sql)){

            sttmt.setString(1, loja.getEmail());
            sttmt.setString(2, loja.getSenha());
            sttmt.executeUpdate();
        }
    }


    //metodo pra encontrar a loja que deseja alterar
    public Loja foundLoja(int codigo) throws SQLException {

        String sql = "SELECT * FROM loja WHERE codigo = ?";

        //try pra conectar com o banco e executar a query
        try (Connection conn = connectionFactory.getConnection();
             PreparedStatement sttmt = conn.prepareStatement(sql)) {

            sttmt.setInt(1, codigo);

            //try pra retornar a loja do id especificadox
            try (ResultSet result = sttmt.executeQuery()) {
                if (result.next()) {
                    Loja loja = new Loja();
                    loja.setCodigo(result.getInt("codigo"));
                    loja.setEmail(result.getString("email"));
                    loja.setSenha(result.getString("senha"));
                    return loja;
                }
            }
            return null; //se nao encontrar nenhuma loja com esse id retorna null
        }
    }

    //metodo pra atualizar a loja que encontrou no metodo passado
    public void updateLoja(Loja loja) throws SQLException{

        String sql = "UPDATE loja SET email = ?, senha = ? WHERE codigo = ?";

        try (Connection conn = connectionFactory.getConnection();
             PreparedStatement sttmt = conn.prepareStatement(sql)){

            sttmt.setString(1, loja.getEmail());
            sttmt.setString(2, loja.getSenha());
            sttmt.setInt(3, loja.getCodigo());
            sttmt.executeUpdate();
        }
    }

    //metodo pra excluir loja do banco de acordo com o id
    public void deleteLoja(int codigo) throws SQLException {

        String sql = "DELETE FROM loja WHERE codigo = ?";

        try (Connection conn = connectionFactory.getConnection();
             PreparedStatement sttmt = conn.prepareStatement(sql)){
            sttmt.setInt(1, codigo);
            sttmt.executeUpdate();
        }
    }
}