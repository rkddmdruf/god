package builder;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.Statement;

public class Setting {
    public static void main(String[] args) {
        String url = "jdbc:mysql://localhost/?serverTimezone=UTC&allowLoadLocalInfile=true";
        String user = "root";        // 본인 DB 계정
        String password = "1234";    // 본인 비번

        try (
            Connection conn = DriverManager.getConnection(url, user, password);
            Statement stmt = conn.createStatement();
        ) {
        	stmt.executeUpdate("SET GLOBAL local_infile = 1;");
        	stmt.executeUpdate("DROP DATABASE IF EXISTS emp");

            // 2. 스키마 생성
            stmt.executeUpdate("CREATE DATABASE emp");

            // 3. 스키마 선택
            stmt.executeUpdate("USE emp");

            // 4. 테이블 생성
            stmt.executeUpdate(
                    "CREATE TABLE emps (" +
                    "eno INT NOT NULL, " +
                    "code VARCHAR(20) NOT NULL," +
                    "name VARCHAR(20) NOT NULL," +
                    "team INT NOT NULL," +
                    "grade INT NOT NULL," +
                    "date DATE NOT NULL," +
                    "state INT NOT NULL," +
                    "id VARCHAR(20) NOT NULL," +
                    "pw VARCHAR(20) NOT NULL," +
                    "PRIMARY KEY (eno))"
                );
            stmt.executeUpdate(
            		"CREATE TABLE payList (" +
            				"pno INT, " +
            				"eno INT," +
            				"date DATE," +
            				"pay INT," +
            				"otPay INT," +
            				"taxPay INT," +
            				"FOREIGN KEY (eno) REFERENCES emps (eno)," +
            				"PRIMARY KEY (pno))"
            		);
            stmt.executeUpdate(
            	    "LOAD DATA LOCAL INFILE 'datafiles/empList.txt' " +
            	    "INTO TABLE emps " +
            	    "FIELDS TERMINATED BY '\\t' " +
            	    "LINES TERMINATED BY '\\r\\n' " +
            	    "IGNORE 1 LINES " +
            	    "(eno,code,name,team,grade,date,state, id, pw)"
            	);
            stmt.executeUpdate(
            	    "LOAD DATA LOCAL INFILE 'datafiles/payList.txt' " +
            	    "INTO TABLE paylist " +
            	    "FIELDS TERMINATED BY '\\t' " +
            	    "LINES TERMINATED BY '\\n' " +
            	    "IGNORE 1 LINES " +
            	    "(pno, eno, date, pay, otPay, taxPay)"
            	);
            
            System.out.println("game 스키마 재생성 완료");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}