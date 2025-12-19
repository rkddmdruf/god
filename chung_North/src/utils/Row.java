package utils;

import java.util.ArrayList;

public class Row extends ArrayList<Object>{
	public String getString(int i) { return this.get(i).toString(); }
	public int getInt(int i) { return Integer.parseInt(getString(i)); }
}
