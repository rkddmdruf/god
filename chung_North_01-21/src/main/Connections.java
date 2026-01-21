package main;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class Connections {
	private static Connection c;
	private static PreparedStatement ps;
	private static ResultSet re;
	
	private static void setting(String query, Object...objects) throws SQLException{
		c = DriverManager.getConnection("jdbc:mysql://localhost/moviedb?serverTimezone=Asia/Seoul", "root", "1234");
		ps = c.prepareStatement(query);
		for(int i = 0; i < objects.length; i++) {
			ps.setObject(i + 1, objects[i]);
		}
	}
	
	public static List<Data> select(String query, Object...objects){
		List<Data> list = new ArrayList<>();
		try {
			setting(query, objects);
			re = ps.executeQuery();
			while (re.next()) {
				Data d = new Data();
				for(int i = 0; i < re.getMetaData().getColumnCount(); i++)
					d.add(re.getObject(i + 1));
				list.add(d);
			}
		} catch (Exception e) {
			System.out.println(e.getMessage());
		}finally {
			clozed();
		}
		return list;
	}
	
	public static void update(String query, Object...objects) {
		try {
			setting(query, objects);
			ps.executeUpdate();
		} catch (Exception e) {
			System.out.println(e.getMessage());
		}
	}
	private static void clozed() {
		try {
			if (c != null && !c.isClosed())
				c.close();
			if (ps != null && !ps.isClosed())
				ps.close();
			if (re != null && !re.isClosed())
				re.close();
		} catch (Exception e) {
			System.out.println(e.getMessage());
		}
	}
}
class Data extends ArrayList<Object> {
	public int getInt(int i) {
		return Integer.parseInt(get(i).toString());
	}
}