package orm;

import java.lang.reflect.Field;

public class Equalz {
	private String fieldName;
	
	public Equalz(String fieldName) {
		this.fieldName = fieldName;
	}
	
	public Nev contains(Object value) {
		return new Nev(fieldName, "contains", value);
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
		return new Nev(fieldName, ">=", value);
	}
	public Nev smallEq(Object value) {
		return new Nev(fieldName, "<=", value);
	}
}
