package DAO;

import org.apache.commons.dbcp2.BasicDataSource;
import java.sql.*;

import io.github.cdimascio.dotenv.Dotenv;

public class DataSource {
    private static final String DATABASE = "QUIZ_WEBSITE";
    private static final String TEST_DATABASE = "QUIZ_WEBSITE_TEST";
    private static final Dotenv dotenv = Dotenv.load();
    private static final String username = dotenv.get("SQL_USERNAME");
    private static final String password = dotenv.get("SQL_PASSWORD");

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
        dataSource.setUsername(username);
        dataSource.setPassword(password);

        return dataSource;
    }

    public static void clearTables(BasicDataSource dataSource, String[] tableNames){
        Connection connection = null;
        try {
            StringBuilder query = new StringBuilder();
            connection = dataSource.getConnection();
            query.append("DELETE FROM ?; ".repeat(tableNames.length));
            PreparedStatement statement = connection.prepareStatement(String.valueOf(query));
            for (int i = 0; i < tableNames.length; i++) {
                statement.setString(i + 1, tableNames[i]);
            }
            statement.executeQuery();

        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            if (connection != null) {
                try {
                    connection.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
