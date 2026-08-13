package DAO;
import Model.Gerente;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
public class GerenteDAO {

    //cria o metodo pra imprimir todos os produtos
    public List<Gerente> read() throws SQLException{

        //cria a lista dos produtos
        List<Gerente> gerentes = new ArrayList<>();

        String sql = "SELECT * FROM gerente";


        //try pra listar tudo de todos os produtos
        try (Connection connection = connect(); //chama o metodo connect pra variavel
             PreparedStatement statement = connection.prepareStatement(sql);//mei q executa o que tava no sql
             ResultSet result = statement.executeQuery()){ // pra devolver dados tipo get

            //laço pra repetir sempre q tiver produto na fila
            while(result.next()){
                Gerente gerente = new Gerente();
                gerente.setId(result.getInt("id")); //define o id do produto como esta a informação no bd, os de baixo é as mesmas coisas
                gerente.setNome(result.getString("nome"));
                gerente.setPreco(result.getDouble("preco"));

                produtos.add(produto);//adiciona o objetox na lista
            }
        }
        return produtos;//retorna a lista dos produtos

    }


}