package utils;

import java.util.ArrayList;

public class Row extends ArrayList<Object>{
	public String getString(int index) {
		return this.get(index).toString();
	}
	public int getInt(int index) {
		return Integer.parseInt(getString(index));
	}
}
