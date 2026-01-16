package main;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;


public class Connections {
	private static Connection c = null;
	private static PreparedStatement ps = null;
	private static ResultSet re = null;
	
	private static void setConnections(String query, Object...objects) throws SQLException{
		c = DriverManager.getConnection("jdbc:mysql://localhost/moviedb?serverTimezone=Asia/Seoul&allowLoadLocalInfile=true", "root", "1234");
		ps = c.prepareStatement(query);
		for(int i = 0; i < objects.length; i++)
			ps.setObject(i+1, objects[i]);
	}
	public static List<Data> select(String query, Object...objects){
		List<Data> list = new ArrayList<>();
		try {
			setConnections(query, objects);
			re = ps.executeQuery();
			while(re.next()) {
				Data d = new Data();
				for(int i = 0; i < re.getMetaData().getColumnCount(); i++)
					d.add(re.getObject(i+1));
				list.add(d);
			}
		} catch (Exception e) {
			System.out.println(e.getMessage());
		} finally {
			closedConnection();
		}
		return list;
	}
	
	public static void update(String query, Object...objects) {
		try {
			setConnections(query, objects);
			ps.executeUpdate();
		} catch (Exception e) {
			System.out.println(e.getMessage());
		} finally {
			closedConnection();
		}
	}
	
	private static void closedConnection() {
		try {
			if(c != null && !c.isClosed()) c.close();
			if(ps != null && !ps.isClosed()) ps.close();
			if(re != null && !re.isClosed()) re.close();
		} catch (Exception e) {
			System.out.println(e.getMessage());
		}
	}
}
class Data extends ArrayList<Object> {
	public int getInt(int index) {
		return Integer.parseInt(this.get(index).toString());
	}
};