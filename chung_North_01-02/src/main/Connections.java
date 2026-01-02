package main;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

public class Connections {
	
	private String[] Strings = {
			"SELECT * FROM moviedb.fb;",
	};
	private PreparedStatement c(String query, Object...objects) {
		try {
			Connection c = DriverManager.getConnection("jdbc:mysql://localhost/moviedb?serverTimezone=Asia/Seoul&allowLoadLocalInfile=true", "root", "1234");
			PreparedStatement ps = c.prepareStatement(query);
			for(int i = 0; i < objects.length; i++) {
				ps.setObject(i+1, objects[i]);
			}
			return ps;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	
	public List<data> getData(String string, Object...objects){
		List<data> list = new ArrayList<>();
		try {
			PreparedStatement ps = c(string, objects);
			if(!string.toUpperCase().contains("SELECT")) {
				ps.executeUpdate();
				return list;
			}
			ResultSet re = ps.executeQuery();
			while(re.next()) {
				data d = new data();
				for(int i = 0; i < re.getMetaData().getColumnCount(); i++) {
					d.add(re.getObject(i+1));
				}
				list.add(d);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return list;
	}
	public List<data> getData(int index, Object...objects){
		List<data> list = getData(Strings[index], objects);
		return list;
	}
	public class data extends ArrayList<Object>{  String getString(int index) { return this.get(index).toString(); }   }
}
