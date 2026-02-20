package orm;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Optional;

public class SaveList {
	protected static Map<Class<?>, Map<Object, Entity>> map = new HashMap<>();
	protected static <E extends Entity> void saveList(Class<?> c, List<E> list) {
		if(map.get(c) == null) map.put(c, new HashMap<>());
		try {
			for(E e : list) {
				Field fs = c.getDeclaredFields()[0];
				fs.setAccessible(true);
				map.get(c).put(fs.get(e), e);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	protected static List<Entity> getList(Class<?> c){
		List<Entity> list = new ArrayList<>();
		if(map.get(c) == null) return list;
		return new ArrayList<>(map.get(c).values());
	}
}
