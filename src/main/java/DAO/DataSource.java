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


    /**
     * Creates and configures a BasicDataSource instance for database connectivity.
     * Depending on the value of the isForTesting parameter, the appropriate database
     * name and credentials are used to configure the data source. The data source is configured
     * with settings for maximum connections, abandoned connection handling, and the JDBC URL.
     *
     * @param isForTesting A flag indicating whether the data source is for testing or production.
     * @return A configured BasicDataSource instance.
     */
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


    /**
     * Clears specified tables by executing DELETE statements on each of them.
     * This method takes a BasicDataSource instance and an array of table names as parameters.
     * For each provided table name, a DELETE statement is executed to clear all records from the table.
     * The method establishes a database connection, creates a Statement, and executes the DELETE queries.
     * If any SQLException occurs during the database operations, the exception is caught,
     * and the stack trace is printed. The database connection is properly closed in the finally block
     * to ensure resource release, even if an exception occurs.
     *
     * @param dataSource The BasicDataSource instance for database connectivity.
     * @param tableNames An array of table names to be cleared.
     */
    public static void clearTables(BasicDataSource dataSource, String[] tableNames) {
        Connection connection = null;
        try {
            StringBuilder query = new StringBuilder();
            connection = dataSource.getConnection();
            Statement statement = connection.createStatement();
            for (String tableName : tableNames) {
                query.append("DELETE FROM ").append(tableName).append("; ");
            }
            statement.execute(String.valueOf(query));
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
