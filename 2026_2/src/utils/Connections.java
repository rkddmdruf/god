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
		return DriverManager.getConnection("jdbc:mysql://localhost/lecture?serverTimezone=Asia/Seoul", "root", "1234");
	}

	private static PreparedStatement setPs(Connection c, String string, Object... val) throws SQLException {
		PreparedStatement ps = c.prepareStatement(string);
		for (int i = 0; i < val.length; i++) {
			ps.setObject(i + 1, val[i]);
		}
		return ps;
	}

	public static List<Data> select(String string, Object... val) {
		List<Data> list = new ArrayList<>();
		try (
				Connection c = getCon();
				PreparedStatement ps = setPs(c, string, val); 
				ResultSet re = ps.executeQuery()
			) {

			while (re.next()) {
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
	
	public static void update(String string, Object...val) {
		try (
				Connection c = getCon();
				PreparedStatement ps = setPs(c, string, val);
			) {
			ps.executeUpdate();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
