package utils;

public class User {
	public static Data user = Connections.select("select * from user").get(0);
	public static boolean admin;
	
	public static void setUser(Data data) {
		user = data;
	}
	
	public static Data getUser() {
		if(user == null) return null;
		Data data = new Data();
		for(Object obj : user) {
			data.add(obj);
		}
		return data;
	}
}
