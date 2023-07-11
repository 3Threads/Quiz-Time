package FunctionalClasses;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.sql.*;

public class SQLConnect {
    protected final String database = "QUIZ_WEBSITE";
    protected final String databaseTest = "QUIZ_WEBSITE_TEST";
    private String server = "jdbc:mysql:// localhost:3306/";
    private final String username = "root";
    private final String password = "passwordD1!";
    protected Connection connect;

    public SQLConnect(boolean isTesting) {
        server += "?allowMultiQueries=true";
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            connect = DriverManager.getConnection
                    (server, username, password);
            String query;
            if (isTesting) {
                query = "CREATE DATABASE IF NOT EXISTS " + databaseTest + "; USE " + databaseTest + ";";
            } else {
                query = "CREATE DATABASE IF NOT EXISTS " + database + "; USE " + database + ";";
            }
            Statement stmt = connect.createStatement();
            stmt.execute(query);
            if (isTesting) createAgain();
        } catch (ClassNotFoundException | SQLException | IOException ex) {
            ex.printStackTrace();
        }
    }

    private void createAgain() throws IOException, SQLException {
        String s = System.getProperty("user.dir") + "/src/main/java/SqlScripts/SqlScriptTEST.sql";
        String query = Files.readString(Path.of(s));
        Statement stmt = connect.createStatement();
        stmt.execute(query);
    }
}