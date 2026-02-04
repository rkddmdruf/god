package utils;

import java.util.ArrayList;

public class Data extends ArrayList<Object>{
	public int getInt(int i) {
		return Integer.parseInt(get(i).toString());
	}
}
