package main;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;


public class Connections {
	private static Connection c = null;
	private static PreparedStatement ps = null;
	private static ResultSet re = null;
	
	private static void getConnectionAndps(String s, Object...objects) throws SQLException{
		c = DriverManager.getConnection("jdbc:mysql://Localhost/moviedb?serverTimezone=Asia/Seoul&allowLoadLocalInfile=true", "root", "1234");
		ps = c.prepareStatement(s);
		for(int i = 0; i < objects.length; i++) {
			ps.setObject(i + 1, objects[i]);
		}
	}
	
	static List<Data> select(String s, Object...objects){
		List<Data> list = new ArrayList<>();
		try {
			getConnectionAndps(s, objects);
			re = ps.executeQuery();
			while(re.next()) {
				Data d = new Data();
				for(int i = 0; i < re.getMetaData().getColumnCount(); i++) 
					d.add(re.getObject(i + 1));
				list.add(d);
			}
		} catch (Exception e) {
			System.out.println(e.getMessage());
		} finally {
			closeds();
		}
		return list;
	}
	
	static void update(String s, Object...objects) {
		try {
			getConnectionAndps(s, objects);
			ps.executeUpdate();
		} catch (Exception e) {
			System.out.println(e.getMessage());
		}finally {
			closeds();
		}
	}
	private static void closeds() {
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
		return Integer.parseInt(get(index).toString());
	}
}
