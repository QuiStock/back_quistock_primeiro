package DAO;

import java.sql.Connection;
import io.github.cdimascio.dotenv.Dotenv;
import java.sql.DriverManager;
import java.sql.SQLException;

//Classe que cria as conexões
public class ConnectionFactory {
    //Ler informações do .env
    Dotenv dotenv = Dotenv.load();

    private final String url =  dotenv.get("url");
    private final String user = dotenv.get("user");
    private final String password = dotenv.get("password");

    //retornar Connection, lança exceção para ser tratada por outra classe
    public Connection getConnection() throws SQLException {
        return DriverManager.getConnection(url, user, password);
    }
}