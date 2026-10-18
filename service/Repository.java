package service;

import model.annotation.Id;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Objects;

public class Repository<T> {
    private final HashMap<String, T> entities = new HashMap<>();

    public synchronized T save(T entity) {
        Objects.requireNonNull(entity, "Entity cannot be null.");
        String id = getId(entity);
        entities.put(id, entity);
        return entity;
    }

    @SafeVarargs
    public final void saveAll(T... items) {
        Objects.requireNonNull(items, "Items cannot be null.");
        for (T item : items) {
            save(item);
        }
    }

    public synchronized T findById(String id) {
        return entities.get(id);
    }

    public synchronized ArrayList<T> findAll() {
        return new ArrayList<>(entities.values());
    }

    public synchronized boolean delete(String id) {
        return entities.remove(id) != null;
    }

    public static <N extends Number> double average(N[] values) {
        Objects.requireNonNull(values, "Values cannot be null.");
        if (values.length == 0) {
            throw new IllegalArgumentException("Cannot calculate the average of an empty array.");
        }
        double sum = 0;
        for (N value : values) {
            sum += Objects.requireNonNull(value, "Values cannot contain null.").doubleValue();
        }
        return sum / values.length;
    }

    private String getId(T entity) {
        Field idField = null;
        for (Class<?> type = entity.getClass(); type != null; type = type.getSuperclass()) {
            for (Field field : type.getDeclaredFields()) {
                if (field.isAnnotationPresent(Id.class)) {
                    if (idField != null) {
                        throw new IllegalArgumentException(
                                "Entity type has multiple fields annotated with @Id.");
                    }
                    idField = field;
                }
            }
        }
        if (idField == null) {
            throw new IllegalArgumentException("Entity type must have a field annotated with @Id.");
        }
        try {
            idField.setAccessible(true);
            Object value = idField.get(entity);
            if (value == null) {
                throw new IllegalArgumentException("@Id field cannot be null.");
            }
            return value.toString();
        } catch (IllegalAccessException e) {
            throw new IllegalStateException("Could not read the entity's @Id field.", e);
        }
    }
}
