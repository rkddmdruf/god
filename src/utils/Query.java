package utils;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JOptionPane;

public class Query {

	public static List<Row> select(String str, String...val){
		List<Row> list = new ArrayList<Row>();
		try {
			Connection c = DriverManager.getConnection("jdbc:mysql://localhost/company_선수번호?serverTimezone=UTC&allowLoadLocalInfile=true","root","1234");
			PreparedStatement ps = c.prepareStatement(str);
			for(int i = 0; i < val.length; i++) {
				ps.setObject(i+1, val[i]);
			}
			ResultSet rs = ps.executeQuery();
			ResultSetMetaData rsm = rs.getMetaData();
			Row rowName = new Row();
			for(int i = 1; i <= rsm.getColumnCount(); i++) {
				rowName.add(rsm.getColumnName(i));
			}
			list.add(rowName);
			while(rs.next()) {
				Row row = new Row();
				
				for(int i = 1; i <= rsm.getColumnCount(); i++) {
					row.add(rs.getObject(i));
				}
				list.add(row);
			}
			rs.close();
			ps.close();
			c.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return list;
	}
	
	public static void upDate(String str, String...val){
		try {
			Connection c = DriverManager.getConnection("jdbc:mysql://localhost/company_선수번호?serverTimezone=UTC&allowLoadLocalInfile=true","root","1234");
			PreparedStatement ps = c.prepareStatement(str);
			for(int i = 0; i < val.length; i++) {
				ps.setObject(i+1, val[i]);
			}
			ps.executeUpdate();
			ps.close();
			c.close();
		}catch (Exception e) {
			
		}
	}
}
