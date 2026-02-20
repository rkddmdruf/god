package orm;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

public class Tuple extends LinkedHashMap<String, Object>{ 
	public int getInt(String string) {
		return Integer.parseInt(getString(string));
	}
	
	public String getString(String string) {
		return get(string).toString();
	}
}
