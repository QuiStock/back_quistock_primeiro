package DAO;
import Model.Gerente;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class GerenteDAO {

    private final ConnectionFactory connectionFactory = new ConnectionFactory();

    //Metodo pra imprimir todos os gerentes (Sem mostrar o id e senha)
    public List<Gerente> read() throws SQLException{

        List<Gerente> gerentes = new ArrayList<>();

        String sql = "SELECT * FROM gerente";


        //try pra listar tudo de todos os produtos
        try (Connection conn = connectionFactory.getConnection();
             PreparedStatement sttmt = conn.prepareStatement(sql);
             ResultSet result = sttmt.executeQuery()){

            //laço pra repetir sempre q tiver produto na fila
            while(result.next()){
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


}