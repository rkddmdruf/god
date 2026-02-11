package orm;

import java.util.ArrayList;

public class JoinTable extends ArrayList<Object>{
	public String getString(int i) {
		return get(i).toString();
	}
	public int getInt(int i) {
		return Integer.parseInt(getString(i));
	}
	
	public Object getter(int i) {
		return this.get(0);
	}
}
