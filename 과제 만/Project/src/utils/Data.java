package utils;

import java.util.ArrayList;

public class Data extends ArrayList<Object>{

	public String getString(int i) {
		return get(i).toString();
	}
	
	public int getInt(int i) {
		return Integer.parseInt(getString(i));
	}
}
