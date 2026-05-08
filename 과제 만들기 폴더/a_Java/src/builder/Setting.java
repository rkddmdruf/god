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
        	stmt.executeUpdate("DROP DATABASE IF EXISTS game");

            // 2. 스키마 생성
            stmt.executeUpdate("CREATE DATABASE game");

            // 3. 스키마 선택
            stmt.executeUpdate("USE game");

            // 4. 테이블 생성
            stmt.executeUpdate(
                "CREATE TABLE category (" +
                "cno INT NOT NULL, " +
                "cname VARCHAR(45) NOT NULL, " +
                "PRIMARY KEY (cno))"
            );
            stmt.executeUpdate(
                    "CREATE TABLE game (" +
                    "gno INT NOT NULL, " +
                    "gname VARCHAR(45) NOT NULL, " +
                    "cno VARCHAR(45) NOT NULL," + 
                    "online INT NOT NULL," + 
                    "price INT NOT NULL," + 
                    "PRIMARY KEY (gno))"
                );
            stmt.executeUpdate(
                    "CREATE TABLE user (" +
                    "uno INT NOT NULL, " +
                    "uname VARCHAR(45) NOT NULL, " +
                    "id VARCHAR(45) NOT NULL," + 
                    "pw VARCHAR(45) NOT NULL," + 
                    "iconNumber INT NOT NULL," + 
                    "price INT NOT NULL," +
                    "otp VARCHAR(5) NOT NULL," + 
                    "PRIMARY KEY (uno))"
                );
            stmt.executeUpdate(
            		"CREATE TABLE loginUser ("
            		+ "uno INT NOT NULL,"
            		+ "FOREIGN KEY (uno) REFERENCES user (uno)"
            		+ ")");
            stmt.executeUpdate(
            		"CREATE TABLE buyGame ("
            		+ "bgno INT NOT NULL,"
            		+ "gno INT NOT NULL,"
            		+ "uno INT NOT NULL,"
            		+ "date DATE NOT NULL,"
            		+ "FOREIGN KEY (gno) REFERENCES game (gno),"
            		+ "FOREIGN KEY (uno) REFERENCES user (uno),"
            		+ "PRIMARY KEY (bgno)"
            		+ ")");
            
            stmt.executeUpdate(
            	    "LOAD DATA LOCAL INFILE 'datafiles/category.txt' " +
            	    "INTO TABLE category " +
            	    "FIELDS TERMINATED BY '\\t' " +
            	    "LINES TERMINATED BY '\\r\\n' " +
            	    "IGNORE 1 LINES " +
            	    "(cno, cname)"
            	);
            stmt.executeUpdate(
            	    "LOAD DATA LOCAL INFILE 'datafiles/game.txt' " +
            	    "INTO TABLE game " +
            	    "FIELDS TERMINATED BY '\\t' " +
            	    "LINES TERMINATED BY '\\r\\n' " +
            	    "IGNORE 1 LINES " +
            	    "(gno, gname, cno, online, price)"
            	);
            stmt.executeUpdate(
            	    "LOAD DATA LOCAL INFILE 'datafiles/user.txt' " +
            	    "INTO TABLE user " +
            	    "FIELDS TERMINATED BY '\\t' " +
            	    "LINES TERMINATED BY '\\r\\n' " +
            	    "IGNORE 1 LINES " +
            	    "(uno, uname, id, pw, iconNumber, price, otp)"
            	);
            stmt.executeUpdate(
            	    "LOAD DATA LOCAL INFILE 'datafiles/buyGame.txt' " +
            	    "INTO TABLE buyGame " +
            	    "FIELDS TERMINATED BY '\\t' " +
            	    "LINES TERMINATED BY '\\r\\n' " +
            	    "IGNORE 1 LINES " +
            	    "(bgno, gno, uno, date)"
            	);
            System.out.println("game 스키마 재생성 완료");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}