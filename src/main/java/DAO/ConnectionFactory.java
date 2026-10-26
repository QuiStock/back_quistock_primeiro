package DAO;

import java.sql.Connection;
import io.github.cdimascio.dotenv.Dotenv;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.io.File;

//Classe que cria as conexões
public class ConnectionFactory {

    //Ler informações do .env
    private final Dotenv dotenv = loadDotenv();

    //procura o .env na pasta atual, no tomcat ou no WEB-INF
    private Dotenv loadDotenv() {
        File current = new File(System.getProperty("user.dir"));
        File env = new File(current, ".env");

        if (!env.isFile() && System.getProperty("catalina.base") != null) {
            env = new File(System.getProperty("catalina.base"), ".env");
        }

        if (!env.isFile()) {
            try {
                File classes = new File(ConnectionFactory.class.getProtectionDomain()
                        .getCodeSource().getLocation().toURI());
                File webInf = classes.getParentFile();
                env = new File(webInf, ".env");
            } catch (Exception e) {
                env = new File(".env");
            }
        }

        File directory = env.getParentFile();
        return Dotenv.configure()
                .directory(directory == null ? "." : directory.getAbsolutePath())
                .ignoreIfMissing()
                .load();
    }

    private final String url =  dotenv.get("url");
    private final String user = dotenv.get("user");
    private final String password = dotenv.get("password");

    //retornar Connection, lança exceção para ser tratada por outra classe
    public Connection getConnection() throws SQLException {

        try {
            Class.forName("org.postgresql.Driver");
        } catch (ClassNotFoundException e) {
            throw new SQLException("Driver PostgreSQL não encontrado no classpath", e);
        }

        //confere as infos antes de tentar conectar no db
        if (url == null || url.trim().isEmpty() || user == null || user.trim().isEmpty()
                || password == null || password.isEmpty()) {
            throw new SQLException("Configure url, user e password no .env da pasta atual, do catalina.base ou do WEB-INF");
        }

        return DriverManager.getConnection(url, user, password);

    }

}
