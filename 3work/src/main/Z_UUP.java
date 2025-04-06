package main;

public class Z_UUP {
	private static String url = "jdbc:mysql://localhost/question?serverTimezone=UTC&allowLoadLocalInfile=true";
	
	private static String username = "root";
    private static String password = "1234";
    
	public static String url() {
		return url;
	}
	
	public static String username() {
		return username;
		
	}
	public static String password() {
		return password;
		//"jdbc:mysql://localhost?serverTimezone=UTC&allowLoadLocalInfile=true";
	}
    
}
