package orm;

import java.util.HashMap;
import java.util.Map;

public class Tuple {

    private Map<Class<?>, Object> map = new HashMap<>();

    public <T> void put(Class<T> clazz, Object value) {
        map.put(clazz, value);
    }

    public <T> T get(Class<T> clazz) {
        return clazz.cast(map.get(clazz));
    }
}
