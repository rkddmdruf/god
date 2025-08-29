package utils;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
public enum Query{

	login("SELECT * FROM medinow.user where id = ? and pw = ?;"),
	category("select * from category"),
	hospital("SELECT * FROM medinow.hospital;"),
	hospitalUserPoint("SELECT hospital.*, reservation.*, doctor.name FROM medinow.hospital "
			+ "	LEFT JOIN doctor ON hospital.hno = doctor.hno LEFT JOIN reservation ON reservation.dno = doctor.dno "
			+ "	where reservation.uno = ?;"),
	myHomeHospital("SELECT hospital.hno, hospital.name, reservation.date, doctor.name, reservation.uno"
			+ " FROM reservation LEFT JOIN doctor ON reservation.dno = doctor.dno LEFT JOIN hospital ON doctor.hno = hospital.hno where uno = ? group by reservation.reno"),
	symptom("SELECT * FROM medinow.symptom;"),
	hospitalTOdoctor("SELECT doctor.*, hospital.name "
			+ "	FROM medinow.doctor LEFT JOIN hospital ON hospital.hno = doctor.hno where doctor.hno = ?;"),
	hospitalTOdoctorALL("SELECT doctor.*, hospital.name "
			+ "	FROM medinow.doctor LEFT JOIN hospital ON hospital.hno = doctor.hno;"),
	hospitalTOdoctorDNO("SELECT doctor.*, hospital.name "
			+ "	FROM medinow.doctor LEFT JOIN hospital ON hospital.hno = doctor.hno where doctor.dno = ?;"),
	reservation("insert into reservation set date = ?, uno = ?, dno = ?"),
	question("SELECT * FROM medinow.question;"),
	medisn("SELECT * FROM medinow.pharmacy;");
	String string;
	
	
	Query(String string){
		this.string = string;
	}
	
	
	
	public static Connection getCon() {
		try {
			return DriverManager.getConnection("jdbc:mysql://localhost/medinow?serverTimezone=UTC&allowLoadLocalInfile=true","root","1234");
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	public List<Row> select(Object...val){// VAL 0
		List<Row> list = new ArrayList<>();
		try {
			PreparedStatement ps = getCon().prepareStatement(string);
			for(int i =0; i < val.length; i++) {
				ps.setObject(i+1, val[i]);
			}
			ResultSet rs = ps.executeQuery();
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
	
	
	public void update(Object...val) {
		try {
			PreparedStatement s = getCon().prepareStatement(string);
			for (int i = 0; i < val.length; i++) {
				s.setObject(i + 1, val[i]);
			}
			s.executeUpdate();
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		
	}
	
	
	
}
