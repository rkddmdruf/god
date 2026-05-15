package utils;

public class User {
	private static Data user = Connections.select("select * from user").get(7);
	public static boolean admin;
	
	public static void setUser(Data a) {
		user = a;
	}
	
	public static Data getUser() {
		if(user == null) return null;
		Data data = new Data();
		for(Object o : user) {
			data.add(o);
		}
		return data;
	}
}
