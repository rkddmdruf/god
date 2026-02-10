package orm2;

import java.sql.*;

public class DBManager {

    private static String url, user, password;

    @FunctionalInterface
    public interface SqlAction {
        void run(PreparedStatement stmt) throws Exception;
    }

    public static void init(String url, String user, String password) {
        DBManager.url = url;
        DBManager.user = user;
        DBManager.password = password;
    }

    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(url, user, password);
    }

    public static void execute(String sql, SqlAction action) {
        try (var conn = getConnection();
             var stmt = conn.prepareStatement(sql)
        ) {
            action.run(stmt);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public static void executeInsert(String sql, SqlAction action) {
        try (var conn = getConnection();
             var stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)
        ) {
            action.run(stmt);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public static void executeUpdate(String sql, Object... params) {
        execute(sql, stmt -> {
            for (int i = 0; i < params.length; i++) {
                stmt.setObject(i + 1, params[i]);
            }
            stmt.executeUpdate();
        });
    }
}
