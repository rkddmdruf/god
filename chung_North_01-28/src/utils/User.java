package utils;

public class User {
	private static Data user  = Connections.select("select * from user").get(0);
	public static boolean admin;
	
	public static void setUser(Data d) {
		user = d;
	}
	public static Data getUser() {
		if(user == null) return null;
		Data data = new Data();
		for(Object d : user)
			data.add(d);
		return data;
	}
}
