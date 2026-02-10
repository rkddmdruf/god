package com.worldskills.orm_template.entity;

import java.lang.reflect.Field;
import java.util.List;
import java.util.StringJoiner;

public class SqlBuilder {

    public static String selectAll(final Class<?> entityClass) {
        return "SELECT * FROM " + EntityReflector.resolveTableName(entityClass);
    }

    public static String insert(final Class<?> entityClass, final List<Field> fields) {
        final StringJoiner columns = new StringJoiner(", ");
        final StringJoiner placeholders = new StringJoiner(", ");
        fields.forEach(field -> {
            columns.add(field.getName());
            placeholders.add("?");
        });
        return "INSERT INTO " + EntityReflector.resolveTableName(entityClass) + " (" + columns + ") VALUES (" + placeholders + ")";
    }

    public static String update(final Class<?> entityClass, final List<Field> fields) {
        final StringJoiner setClause = new StringJoiner(", ");
        fields.forEach(field -> setClause.add(field.getName() + " = ?"));
        return "UPDATE " + EntityReflector.resolveTableName(entityClass) + " SET " + setClause + whereIdClause(entityClass);
    }

    public static String delete(final Class<?> entityClass) {
        return "DELETE FROM " + EntityReflector.resolveTableName(entityClass) + whereIdClause(entityClass);
    }

    private static String whereIdClause(final Class<?> entityClass) {
        return " WHERE " + EntityReflector.resolveIdField(entityClass).getName() + " = ?";
    }
}
