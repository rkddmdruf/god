package utils;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class Connections {

	private static Connection getCon() throws SQLException {
		return DriverManager.getConnection("jdbc:mysql://localhost/moviedb?serverTimezone=Asia/Seoul", "root", "1234");
	}

	private static PreparedStatement setPreparedStatement(Connection c, String query, Object... objs)
			throws SQLException {
		PreparedStatement ps = c.prepareStatement(query);
		for (int i = 0; i < objs.length; i++) {
			ps.setObject(i + 1, objs[i]);
		}
		return ps;
	}

	public static List<Data> select(String queryStr, Object... objs) {
		List<Data> list = new ArrayList<>();
		try (Connection c = getCon(); PreparedStatement ps = setPreparedStatement(c, queryStr, objs);) {
			ResultSet re = ps.executeQuery();
			while(re.next()) {
				Data data = new Data();
				for(int i = 0; i < re.getMetaData().getColumnCount(); i++) {
					data.add(re.getObject(i + 1));
				}
				list.add(data);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return list;
	}
	
	public static void update(String queryStr, Object...objects) {
		try (Connection c = getCon(); PreparedStatement ps = setPreparedStatement(c, queryStr, objects);){
			ps.executeUpdate();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
