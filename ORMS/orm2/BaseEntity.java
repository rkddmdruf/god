package orm2;

import java.lang.reflect.Field;
import java.sql.ResultSet;
import java.time.*;
import java.util.*;
import java.util.stream.Collector;
import java.util.stream.Collectors;

@SuppressWarnings("unchecked")
public abstract class BaseEntity {

    private static final Map<Class<?>, Map<Object, BaseEntity>> cache = new HashMap<>();

    private static List<Field> fields(Class<?> cls) {
        return Arrays.stream(cls.getDeclaredFields()).peek(f -> f.setAccessible(true)).collect(Collectors.toList());
    }

    private static List<Field> nonIdFields(Class<?> cls) {
        return fields(cls).stream().filter(f -> !f.isAnnotationPresent(Id.class)).collect(Collectors.toList());
    }

    private static Field idField(Class<?> cls) {
        return fields(cls).stream().filter(f -> f.isAnnotationPresent(Id.class)).findFirst().orElseThrow();
    }

    private static String table(Class<?> cls) {
        return cls.getAnnotation(Table.class).value();
    }

    private static String where(Class<?> cls) {
        return " WHERE " + idField(cls).getName() + " = ?";
    }

    private Object idValue() {
        try {
            return idField(getClass()).get(this);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private static Object extractValue(ResultSet rs, Field field) throws Exception {
        var column = field.getName();
        if (rs.getObject(column) == null) {
            return null;
        }
        var type = field.getType();
        if (type == Long.class) {
            return rs.getLong(column);
        }
        if (type == Integer.class) {
            return rs.getInt(column);
        }
        if (type == LocalDateTime.class) {
            return rs.getTimestamp(column).toLocalDateTime();
        }
        if (type == LocalDate.class) {
            return rs.getDate(column).toLocalDate();
        }
        return rs.getObject(column);
    }

    // 앱 실행할 때 초기화
    public static <T extends BaseEntity> void load(Class<T> cls) {
        cache.put(cls, new HashMap<>());
        DBManager.execute("SELECT * FROM " + table(cls), stmt -> {
            var rs = stmt.executeQuery();
            while (rs.next()) {
                var entity = (BaseEntity) cls.getDeclaredConstructor().newInstance();
                for (var field : fields(cls)) {
                    field.set(entity, extractValue(rs, field));
                }
                putCache(entity);
            }
        });
    }

    public static <T extends BaseEntity> List<T> findAll(Class<T> cls) {
        return (List<T>) new ArrayList<>(cache.getOrDefault(cls, Map.of()).values());
    }

    public static <T extends BaseEntity> Optional<T> findById(Class<T> cls, Object id) {
        return Optional.ofNullable((T) cache.getOrDefault(cls, Map.of()).get(id));
    }

    public void save() {
        var fields = nonIdFields(getClass());
        var columns = String.join(", ", fields.stream().map(Field::getName).collect(Collectors.toList()));
        var placeholders = String.join(", ", Collections.nCopies(fields.size(), "?"));
        DBManager.executeInsert("INSERT INTO " + table(getClass()) + " (" + columns + ") VALUES (" + placeholders + ")",
                stmt -> {
                    for (int i = 0; i < fields.size(); i++) {
                        stmt.setObject(i + 1, fields.get(i).get(this));
                    }
                    stmt.executeUpdate();
                    var keys = stmt.getGeneratedKeys();
                    if (keys.next()) {
                        var id = idField(getClass());
                        id.set(this, keys.getInt(1));
                    }
                });
        putCache(this);
    }

    public void update() {
        var fields = nonIdFields(getClass());
        var setClause = String.join(", ", fields.stream().map(f -> f.getName() + " = ?").toList());
        var params = new ArrayList<>();
        for (var field : fields) {
            try {
                params.add(field.get(this));
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }
        params.add(idValue());
        DBManager.executeUpdate("UPDATE " + table(getClass()) + " SET " + setClause + where(getClass()),
                params.toArray());
        putCache(this);
    }

    public void delete() {
        DBManager.executeUpdate("DELETE FROM " + table(getClass()) + where(getClass()), idValue());
        cache.getOrDefault(getClass(), Map.of()).remove(idValue());
    }

    private static void putCache(BaseEntity entity) {
        cache.computeIfAbsent(entity.getClass(), k -> new HashMap<>()).put(entity.idValue(), entity);
    }

    public static void clearCache() {
        cache.clear();
    }

    public static void clearCache(Class<?> cls) {
        cache.remove(cls);
    }
}
