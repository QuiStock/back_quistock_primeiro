package DAO;

import java.sql.Connection;
import io.github.cdimascio.dotenv.Dotenv;
import java.sql.DriverManager;
import java.sql.SQLException;

// Creates database connections from the configured environment.
public class ConnectionFactory {

    // Load local settings when available.
    private final Dotenv dotenv = Dotenv.configure().ignoreIfMissing().load();

    private final String url =  dotenv.get("url");
    private final String user = dotenv.get("user");
    private final String password = dotenv.get("password");

    // Let callers handle connection failures.
    public Connection getConnection() throws SQLException {

        System.out.println("user.dir = " + System.getProperty("user.dir"));
        System.out.println("url = " + (url != null) + ", user = " + (user != null) + ", password = " + (password != null));
        System.out.println("env url = " + (System.getenv("url") != null));

        try {
            Class.forName("org.postgresql.Driver");
        } catch (ClassNotFoundException e) {
            throw new SQLException("Driver PostgreSQL não encontrado no classpath", e);
        }

        if (url == null || user == null || password == null) {
            throw new SQLException("URL and User/Password are mandatory");
        }

        return DriverManager.getConnection(url, user, password);

    }

}