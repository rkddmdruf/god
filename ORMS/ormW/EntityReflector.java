package com.worldskills.orm_template.entity;

import com.worldskills.orm_template.annotation.Id;
import com.worldskills.orm_template.annotation.Table;
import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.List;

public class EntityReflector {

    public static List<Field> resolveFields(final Class<?> entityClass) {
        return Arrays.stream(entityClass.getDeclaredFields()).peek(field -> field.setAccessible(true)).toList();
    }

    public static List<Field> resolveNonIdFields(final Class<?> entityClass) {
        return resolveFields(entityClass).stream().filter(field -> !field.isAnnotationPresent(Id.class)).toList();
    }

    public static String resolveTableName(final Class<?> entityClass) {
        return entityClass.getAnnotation(Table.class).value();
    }

    public static Field resolveIdField(final Class<?> entityClass) {
        return resolveFields(entityClass).stream()
                .filter(field -> field.isAnnotationPresent(Id.class))
                .findFirst()
                .orElseThrow();
    }

    public static Object resolveIdValue(final BaseEntity entity) {
        try {
            return resolveIdField(entity.getClass()).get(entity);
        } catch (final IllegalAccessException exception) {
            throw new RuntimeException(exception);
        }
    }

    public static Object[] resolveFieldValues(final BaseEntity entity, final List<Field> fields) {
        return fields.stream().map(field -> {
            try { return field.get(entity); }
            catch (final IllegalAccessException exception) { throw new RuntimeException(exception); }
        }).toArray();
    }
}
