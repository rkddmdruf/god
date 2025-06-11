package utils;

import java.util.ArrayList;
import java.util.List;

public class Row extends ArrayList<Object>{
	public int getInt(int i) {return Integer.parseInt(this.get(i).toString());}
	public String getString(int i) {return this.get(i).toString();}
}
