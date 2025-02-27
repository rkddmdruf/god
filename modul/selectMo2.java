package modul;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class selectMo2 {

	public List<Object> selectModul() {
		List<Object> list = new ArrayList<>();
		Scanner sc = new Scanner(System.in);
        UUP o = new UUP();
        
        try {
        	
        	Connection connection = DriverManager.getConnection(o.url(), o.username(), o.password());
        	Statement statement = connection.createStatement();
        	System.out.print("객체 생성 완료 조회할 DBName.table 입력: ");
        	String dbName = sc.nextLine();
        	System.out.println("");
        	//statement.executeUpdate("use "+ dbName);
        	//resultSet = statement.executeQuery("select * from students");
        	String rs = "select * from " + dbName;
        	ResultSet resultSet = statement.executeQuery(rs);
        	while(resultSet.next()) {
        		
        		List<Object> r = new ArrayList<>();
				for (int i = 1; i <= resultSet.getMetaData().getColumnCount(); i++) {
					r.add(resultSet.getObject(i));
				}
				list.add(r);
        	}
        	
        	statement.close();
        	connection.close();
        	resultSet.close();
        	
        }catch (SQLException e) {
			System.out.println("연결 실패");
			System.out.println("사유: "+ e.getMessage());
		}
		return list;
	}
	
	public void t() {
		List<Object>list = selectModul();
		int i = 0;
		for(Object l : list) {
			System.out.println(list.get(i));
			i++;
		}
	}
}
