package main;

public class User {

	private static Data user = Connections.select("select * from user").get(1);
	public static boolean admin = true;
	
	public static  void setUser(Data d) {
		user = d;
	}
	
	public static Data getUser() {
		Data d = new Data();
		for(Object obj : user) {
			d.add(obj);
		}
		return d;
	}
}
