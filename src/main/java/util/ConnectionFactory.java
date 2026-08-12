package util;

import java.sql.Connection;
import io.github.cdimascio.dotenv.Dotenv;
import java.sql.DriverManager;
import java.sql.SQLException;

public class ConnectionFactory {
    Dotenv dotenv = Dotenv.load();

    private final String url =  dotenv.get("url");
    private final String user = dotenv.get("user");
    private final String password = dotenv.get("password");

    public Connection getConnection() throws SQLException {
        return DriverManager.getConnection(url, user, password);
    }
}