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
}
