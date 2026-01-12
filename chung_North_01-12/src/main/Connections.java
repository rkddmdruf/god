package main;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class Connections {
	private static Connection c = null;
	private static PreparedStatement ps = null;
	private static ResultSet re = null;
	
	private static void ConnectAndSetPs(String s, Object...objects) throws SQLException{
		c = DriverManager.getConnection("jdbc:mysql://localhost/moviedb?serverTimezone=UTC&allowLoadLocalInfile=true", "root", "1234");
		ps = c.prepareStatement(s);
		for(int i = 0; i < objects.length; i++) {
			ps.setObject(i+1, objects[i]);
		}
	}
	
	public static List<Data> select(String s, Object...objects) {
		List<Data> list = new ArrayList<>();
		try {
			ConnectAndSetPs(s, objects);
			re = ps.executeQuery();
			while(re.next()) {
				Data data = new Data();
				for(int i = 0; i < re.getMetaData().getColumnCount(); i++) {
					data.add(re.getObject(i+1));
				}
				list.add(data);
			}
		} catch (Exception e) {
			System.out.println(e.getMessage());
		} finally {
			Closzd();
		}
		return list;
	}
	
	public static void update(String s, Object...objects) {
		try {
			ConnectAndSetPs(s, objects);
			ps.executeUpdate();
		} catch (SQLException e) {
			System.out.println(e.getMessage());
		}finally {
			Closzd();
		}
	}
	
	private static void Closzd() {
		try {
			if(c != null && !c.isClosed()) c.close();
			if(ps != null && !ps.isClosed()) ps.close();
			if(re != null && !re.isClosed()) re.close();
		} catch (Exception e) {
			System.out.println(e.getMessage());
		}
	}
}
class Data extends ArrayList<Object> { }
