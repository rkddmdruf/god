package orm;

public class Nev {
	String name;
	String eq;
	Object value;
	Class<?> c;
	
	public Nev(String name, String eq, Object value, Class<?> c) {
		this.name = name;
		this.eq = eq;
		this.value = value;
		this.c = c;
	}
	
	public Nev(String name, Class<?> c) {
		this.name = name;
		this.c = c;
	}
	
	public static String count(Nev n) {
		return "count(" + n.c.getSimpleName().toLowerCase() + "." + n.name + ")";
	}
	public static String countN(Nev n, String name) {
		return count(n) + " as " + name;
	}
	public static String sum(Nev n) {
		return "sum(" + n.c.getSimpleName().toLowerCase() + "." + n.name+ ")";
	}
	public static String sumN(Nev n, String name) {
		return sum(n) + " as " + name;
	}
	public static String avg(Nev n) {
		return "avg(" + n.c.getSimpleName().toLowerCase() + "." + n.name+ ")";
	}
	public static String avgN(Nev n, String name) {
		return avg(n) + " as " + name;
	}
	
	/**
	 * @param bool
	 *  if true = asc ,else if false = desc
	 */
	public static Nev orderGet(String s, boolean bool) {
		return new Nev(s, "", bool ? "asc" : "desc", null);
	}
	
	public static Nev orderGet(Nev n, boolean bool) {
		return new Nev(n.name, "", bool ? "asc" : "desc", n.c);
	}
}
