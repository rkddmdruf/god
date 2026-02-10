package com.worldskills.orm_template.entity;

import com.worldskills.orm_template.database.DBManager;
import java.lang.reflect.Field;
import java.sql.ResultSet;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

public abstract class BaseEntity {

    private static final Map<Class<?>, Map<Object, BaseEntity>> cache = new ConcurrentHashMap<>();

    public static <T extends BaseEntity> void load(final Class<T> entityClass) {
        cache.put(entityClass, new ConcurrentHashMap<>());
        DBManager.execute(SqlBuilder.selectAll(entityClass), statement -> {
            final ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()) putCache(EntityMapper.mapResultSet(resultSet, entityClass));
        });
    }

    @SuppressWarnings("unchecked")
    public static <T extends BaseEntity> List<T> findAll(final Class<T> entityClass) {
        return new ArrayList<>((Collection<T>) cache.getOrDefault(entityClass, Map.of()).values());
    }

    @SuppressWarnings("unchecked")
    public static <T extends BaseEntity> Optional<T> findById(final Class<T> entityClass, final Object id) {
        return Optional.ofNullable((T) cache.getOrDefault(entityClass, Map.of()).get(id));
    }

    public void save() {
        final List<Field> fields = EntityReflector.resolveNonIdFields(getClass());
        DBManager.executeInsert(SqlBuilder.insert(getClass(), fields), statement -> {
            for (int i = 0; i < fields.size(); i++) statement.setObject(i + 1, fields.get(i).get(this));
            statement.executeUpdate();
            final var keys = statement.getGeneratedKeys();
            if (keys.next()) {
                final Field idField = EntityReflector.resolveIdField(getClass());
                idField.set(this, keys.getInt(1));
            }
        });
        putCache(this);
    }

    public void update() {
        final List<Field> fields = EntityReflector.resolveNonIdFields(getClass());
        final Object[] values = EntityReflector.resolveFieldValues(this, fields);
        final Object[] params = Arrays.copyOf(values, values.length + 1);
        params[values.length] = EntityReflector.resolveIdValue(this);
        DBManager.executeUpdate(SqlBuilder.update(getClass(), fields), params);
        putCache(this);
    }

    public void delete() {
        DBManager.executeUpdate(SqlBuilder.delete(getClass()), EntityReflector.resolveIdValue(this));
        removeCache(this);
    }

    private static void putCache(final BaseEntity entity) {
        cache.computeIfAbsent(entity.getClass(), k -> new ConcurrentHashMap<>())
                .put(EntityReflector.resolveIdValue(entity), entity);
    }

    private static void removeCache(final BaseEntity entity) {
        Optional.ofNullable(cache.get(entity.getClass()))
                .ifPresent(classCache -> classCache.remove(EntityReflector.resolveIdValue(entity)));
    }

    public static void clearCache() { cache.clear(); }
    public static void clearCache(final Class<?> entityClass) { cache.remove(entityClass); }
}
