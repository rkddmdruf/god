package main;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class Connections {
	
	String[] stringQ = {
			
	};
	private Connection c = null;
	private PreparedStatement ps = null;
	private ResultSet re = null;
	
	private void getConAndStatement(String string, Object...objects) throws SQLException {
		c = DriverManager.getConnection("jdbc:mysql://localhost/moviedb?serverTimezone=UTC&allowLoadLocalInfile=true", "root", "1234");
		ps = c.prepareStatement(string);
		for(int i = 0; i < objects.length; i++)
			ps.setObject(i+1, objects[i]);
	}
	
	public List<Data> select(String string, Object...objects){
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
			closed();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return list;
	}
	
	public List<Data> select(int index){
		return select(stringQ[index]);
	}
	
	public void update(String string, Object...objects) {
		try { 
			getConAndStatement(string, objects);
			ps.executeUpdate(); 
			closed();
		}
		catch (SQLException e) { 
			System.out.println(e.getMessage()); 
		}
	}
	
	private void closed() throws SQLException{
		if(!re.isClosed()) re.close();
		if(!ps.isClosed()) ps.close();
		if(!c.isClosed()) c.close();
	}
}
class Data extends ArrayList<Object> { }