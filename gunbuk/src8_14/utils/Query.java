package utils;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.util.ArrayList;
import java.util.List;
public enum Query{

	login("SELECT * FROM medinow.user where id = ? and pw = ?;"),
	category("select * from category"),
	symptom("SELECT * FROM medinow.symptom;");
	String string;
	String url = "jdbc:mysql://localhost/medinow?serverTimezone=UTC&allowLoadLocalInfile=true";
	String user = "root";
	String pass = "1234";
	Query(String string){
		this.string = string;
	}
	
	public List<Row> select(Object...val){
		List<Row> list = new ArrayList<>();
		try {
			Connection c = DriverManager.getConnection(url, user, pass);
			PreparedStatement ps = c.prepareStatement(string);
			for(int i =0; i < val.length; i++) {
				ps.setObject(i+1, val[i]);
			}
			ResultSet rs = ps.executeQuery();
			ResultSetMetaData rsm = rs.getMetaData();
			while (rs.next()) {
				Row row = new Row();
				for(int i = 0; i < rs.getMetaData().getColumnCount(); i++) 
					row.add(rs.getObject(i+1));
				list.add(row);
			}
		} catch (Exception e) {
			System.out.println(e.getMessage());
		}
		return list;
	}
}
