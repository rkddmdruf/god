package orm;

import java.lang.reflect.Field;

public class Equalz {
	private String fieldName;
	
	public Equalz(String fieldName) {
		this.fieldName = fieldName;
	}
	
	public Nev big(Object value) {
		return new Nev(fieldName, ">", value);
	}
	public Nev eq(Object value) {
		return new Nev(fieldName, "=", value);
	}
	public Nev small(Object value) {
		return new Nev(fieldName, "<", value);
	}
	public Nev bigEq(Object value) {
		Nev n = big(value);
		n.eq = ">=";
		return n;
	}
	public Nev smallEq(Object value) {
		Nev n = small(value);
		n.eq = "<=";
		return n;
	}
}
