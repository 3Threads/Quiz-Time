package FunctionalClasses;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.sql.*;

public class SQLConnect {
    private String server = "jdbc:mysql:// localhost:3306/";
    private final String username = "root";
    private final String password = "passwordD1!";
    protected Connection connect;
    private final boolean isTesting;

    public SQLConnect(boolean isTesting) {
        server += "?allowMultiQueries=true";
        this.isTesting = isTesting;
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            connect = DriverManager.getConnection
                    (server, username, password);
            createAgain();
        } catch (ClassNotFoundException | SQLException | IOException ex) {
            ex.printStackTrace();
        }
    }

    public void createAgain() throws IOException, SQLException {
        String s;
        if (isTesting)
            s = System.getProperty("user.dir") + "/src/main/java/SqlScripts/SqlScriptTEST.sql";
        else
            s = System.getProperty("user.dir") + "/src/main/java/SqlScripts/SqlScript.sql";


        String query = Files.readString(Path.of(s));
        Statement stmt = connect.createStatement();
        stmt.execute(query);
    }
}