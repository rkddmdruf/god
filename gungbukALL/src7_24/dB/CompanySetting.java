package dB;

import java.sql.*;

import javax.swing.JOptionPane;

public class CompanySetting {


	public static void main(String[] args) {
		try {
			
			Connection c=  DriverManager.getConnection("jdbc:mysql://localhost/?serverTimezone=UTC&allowLoadLocalInfile=true","root","1234");
			Statement s =c.createStatement();
			
			s.executeUpdate("drop database if exists company_선수번호");
			s.executeUpdate("create database company_선수번호");
			s.executeUpdate("use company_선수번호");
			s.executeUpdate("create table admin(name varchar(20) not null, passwd varchar(20) not null, position varchar(20), jumin char(14), inputDate date, PRIMARY key (name, passwd))");
			s.executeUpdate("create table contract(customerCode char(7) not null,contractName varchar(20) not null, regPrice int, regDate date not null, monthPrice int, adminName varchar(20) not null)");
			s.executeUpdate("create table customer(code char(7) not null, name varchar(20) not null, birth date, tel varchar(20), address varchar(100), company varchar(20), PRIMARY key (code, name))");
			s.executeUpdate("set global local_infile = 1");
			s.executeUpdate("load data local infile 'src/dB/admin.txt' into table admin ignore 1 lines");
			s.executeUpdate("load data local infile 'src/dB/contract.txt' into table contract ignore 1 lines");
			s.executeUpdate("load data local infile 'src/dB/customer.txt' into table customer ignore 1 lines");
			s.executeUpdate("drop user if exists 'user'@localhost");
			s.executeUpdate("create user 'user'@localhost identified by '1234'");
			s.executeUpdate("grant select, delete, insert, update on company_선수번호.* to 'user'@localhost");
			
			JOptionPane.showMessageDialog(null, "Success");
			
		} catch (Exception e) {
			System.out.println(e);
		}
	}
}
