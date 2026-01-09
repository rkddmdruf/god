package main;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class Connections {
	
	private static String[] stringQ = {
			
	};
	private static Connection c = null;
	private static PreparedStatement ps = null;
	private static ResultSet re = null;
	
	private static void getConAndStatement(String string, Object...objects) throws SQLException {
		c = DriverManager.getConnection("jdbc:mysql://localhost/moviedb?serverTimezone=Asia/Seoul&allowLoadLocalInfile=true", "root", "1234");
		ps = c.prepareStatement(string);
		for(int i = 0; i < objects.length; i++)
			ps.setObject(i+1, objects[i]);
	}
	
	
	public static List<Data> select(String string, Object...objects){
		List<Data> list = new ArrayList<>();
		try {
			getConAndStatement(string, objects);
			re = ps.executeQuery();
			while(re.next()) {
				Data data = new Data();
				for(int i = 0; i < re.getMetaData().getColumnCount(); i++)
					data.add(re.getObject(i + 1));
				list.add(data);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}finally {
			closed();
		}
		return list;
	}
	
	public static List<Data> select(int index){
		return select(stringQ[index]);
	}
	
	public static void update(String string, Object...objects) {
		try { 
			getConAndStatement(string, objects);
			ps.executeUpdate(); 
		}
		catch (SQLException e) { 
			System.out.println(e.getMessage()); 
		}finally {
			closed();
		}
	}
	
	private static void closed(){
		try {
			if(!re.isClosed() && re != null) re.close();
			if(!ps.isClosed() && ps != null) ps.close();
			if(!c.isClosed() && c != null) c.close(); 
		} catch (Exception e) {
			System.out.println(e.getMessage());
		}
		
	}
}
class Data extends ArrayList<Object> { }