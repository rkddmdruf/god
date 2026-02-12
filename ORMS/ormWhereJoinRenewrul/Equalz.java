package orm;

import java.lang.reflect.Field;

public class Equalz {
	private String fieldName;
	private Class<?> c;
	
	public Nev getNev() {
		return new Nev(fieldName, c);
	}
	public String getName() {
		return fieldName;
	}
	
	public Equalz(String fieldName, Class<?> c) {
		this.fieldName = fieldName;
		this.c = c;
	}
	
	public Nev contains(Object value) {
		return new Nev(fieldName, "like", value, c);
	}
	public Nev big(Object value) {
		return new Nev(fieldName, ">", value, c);
	}
	public Nev eq(Object value) {
		return new Nev(fieldName, "=", value, c);
	}
	public Nev small(Object value) {
		return new Nev(fieldName, "<", value, c);
	}
	public Nev bigEq(Object value) {
		return new Nev(fieldName, ">=", value, c);
	}
	public Nev smallEq(Object value) {
		return new Nev(fieldName, "<=", value, c);
	}
}
