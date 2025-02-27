package modul;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.Statement;
import java.util.Scanner;

public class DropTable {
	UUP o = new UUP();
	Scanner sc = new Scanner(System.in);
	String str;
	public DropTable() {
		try {
			Connection connection = DriverManager.getConnection(o.url(), o.username(), o.password());
			Statement s = connection.createStatement();
			System.out.print("객체 생성완료 삭제할 Table : ");
			str = "drop Table " + sc.nextLine();
			s.executeUpdate(str);
			System.out.println("테이블 삭제완료");
		}catch (Exception e) {
			System.out.println(e);
		}
	}
}
