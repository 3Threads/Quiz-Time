package FunctionalClasses;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

public class SQLConnect {
    protected final String database = "QUIZ_WEBSITE";
    protected final String databaseTest = "QUIZ_WEBSITE_TEST";
    private String server = "jdbc:mysql:// localhost:3306/";
    private final String username = "root";
    private final String password = "password";
    protected Connection connect;

    public SQLConnect(boolean isTesting) {
        if (isTesting) {
            server += databaseTest;
        } else {
            server += database;
        }
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            connect = DriverManager.getConnection
                    (server, username, password);
        } catch (ClassNotFoundException | SQLException ex) {
            ex.printStackTrace();
        }
    }

    public void createAgain() throws IOException, SQLException {
        String s = System.getProperty("user.dir") + "/src/main/java/SqlScripts/SqlScriptTEST.sql";
        String query = Files.readString(Path.of(s));
        System.out.println(query);
        Statement stmt = connect.createStatement();
        stmt.execute(query);
    }
}
