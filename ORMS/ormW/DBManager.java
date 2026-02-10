package com.worldskills.orm_template.database;

import java.sql.*;

public class DBManager {

    private static String url = "jdbc:mysql://localhost:3306/DB_NAME";
    private static String user = "root";
    private static String password = "1234";

    @FunctionalInterface
    public interface SqlAction {
        void execute(PreparedStatement statement) throws Exception;
    }

    public static void init(final String url, final String user, final String password) {
        DBManager.url = url;
        DBManager.user = user;
        DBManager.password = password;
    }

    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(url, user, password);
    }

    public static void execute(final String sql, final SqlAction action) {
        try (final Connection connection = getConnection();
             final PreparedStatement statement = connection.prepareStatement(sql)) {
            action.execute(statement);
        } catch (final Exception exception) { throw new RuntimeException(exception); }
    }

    public static void executeInsert(final String sql, final SqlAction action) {
        try (final Connection connection = getConnection();
             final PreparedStatement statement = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            action.execute(statement);
        } catch (final Exception exception) { throw new RuntimeException(exception); }
    }

    public static void executeUpdate(final String sql, final Object... params) {
        execute(sql, statement -> {
            for (int i = 0; i < params.length; i++) statement.setObject(i + 1, params[i]);
            statement.executeUpdate();
        });
    }
}
