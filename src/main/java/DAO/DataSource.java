package DAO;

import org.apache.commons.dbcp2.BasicDataSource;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.sql.*;

public class DataSource {
    private static final String DATABASE = "QUIZ_WEBSITE";
    private static final String TEST_DATABASE = "QUIZ_WEBSITE_TEST";

    private static final String userName = "root";
    private static final String password = "passwordD1!";

    public static BasicDataSource getDataSource(boolean isForTesting) {
        String dbName = DATABASE;
        if (isForTesting) {
            dbName = TEST_DATABASE;
        }
        BasicDataSource dataSource = new BasicDataSource();
        dataSource.setMaxTotal(-1);
        dataSource.setRemoveAbandonedOnBorrow(true);
        dataSource.setRemoveAbandonedTimeout(5);

        dataSource.setUrl("jdbc:mysql://localhost:3306/" + dbName + "?allowMultiQueries=true");
        dataSource.setUsername(userName);
        dataSource.setPassword(password);

        if (isForTesting) {
            createAgain(dataSource);
        }
        return dataSource;
    }

    private static void createAgain(BasicDataSource dataSource) {
        try {
            String s = System.getProperty("user.dir") + "/src/main/java/SqlScripts/SqlScriptTEST.sql";
            String query = Files.readString(Path.of(s));
            Connection connect = dataSource.getConnection();
            Statement stmt = connect.createStatement();
            stmt.execute(query);
        } catch (SQLException | IOException e) {
            throw new RuntimeException(e);
        }
    }
}
