package com.worldskills.orm_template.entity;

import java.lang.reflect.Field;
import java.sql.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

public class EntityMapper {

    public static BaseEntity mapResultSet(final ResultSet resultSet, final Class<?> entityClass) throws Exception {
        final BaseEntity entity = (BaseEntity) entityClass.getDeclaredConstructor().newInstance();
        final List<Field> fields = EntityReflector.resolveFields(entityClass);
        for (final Field field : fields) {
            field.set(entity, extractValue(resultSet, field.getName(), field.getType()));
        }
        return entity;
    }

    private static Object extractValue(final ResultSet resultSet, final String columnName, final Class<?> type) throws SQLException {
        final Object rawValue = resultSet.getObject(columnName);
        if (rawValue == null) return null;
        if (type == Long.class || type == long.class) return resultSet.getLong(columnName);
        if (type == Integer.class || type == int.class) return resultSet.getInt(columnName);
        if (type == LocalDateTime.class) return resultSet.getTimestamp(columnName).toLocalDateTime();
        if (type == LocalDate.class) return resultSet.getDate(columnName).toLocalDate();
        return rawValue;
    }
}
