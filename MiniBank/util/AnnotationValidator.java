package util;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;
import model.annotation.MaxLength;
import model.annotation.Positive;

public class AnnotationValidator {
    private AnnotationValidator() {
    }

    public static String[] validate(Object obj) {
        if (obj == null) {
            throw new IllegalArgumentException("Object to validate cannot be null.");
        }

        List<String> errors = new ArrayList<>();
        for (Class<?> type = obj.getClass(); type != null; type = type.getSuperclass()) {
            for (Field field : type.getDeclaredFields()) {
                if (!field.isAnnotationPresent(Positive.class)
                        && !field.isAnnotationPresent(MaxLength.class)) {
                    continue;
                }

                field.setAccessible(true);
                Object value;
                try {
                    value = field.get(obj);
                } catch (IllegalAccessException e) {
                    throw new IllegalStateException("Cannot read field " + field.getName(), e);
                }

                Positive positive = field.getAnnotation(Positive.class);
                if (positive != null) {
                    if (!(value instanceof Number number)) {
                        throw new IllegalStateException(
                                "@Positive requires a numeric field: " + field.getName());
                    }
                    if (number.doubleValue() <= 0) {
                        errors.add(field.getName() + " " + positive.message());
                    }
                }

                MaxLength maxLength = field.getAnnotation(MaxLength.class);
                if (maxLength != null) {
                    if (!(value instanceof String string)) {
                        throw new IllegalStateException(
                                "@MaxLength requires a String field: " + field.getName());
                    }
                    if (string.length() > maxLength.value()) {
                        errors.add(field.getName() + " length must be <= " + maxLength.value());
                    }
                }
            }
        }
        return errors.toArray(String[]::new);
    }
}
