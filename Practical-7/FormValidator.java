
import java.lang.annotation.*;
import java.lang.reflect.*;
import java.util.*;

// Annotation to check if a field is blank
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
@interface NotBlank {
}

// Annotation to set maximum length
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
@interface MaxLength {
    int value();
}

// Signup form class
class SignupForm {

    @NotBlank
    @MaxLength(20)
    String username;

    @NotBlank
    @MaxLength(50)
    String email;

    SignupForm(String username, String email) {
        this.username = username;
        this.email = email;
    }
}

// Validator class
public class FormValidator {

    public static List<String> validate(Object obj)
            throws IllegalAccessException {

        List<String> errors = new ArrayList<>();

        Class<?> cls = obj.getClass();

        for (Field field : cls.getDeclaredFields()) {
            field.setAccessible(true);

            Object value = field.get(obj);
            String name = field.getName();

            // Check NotBlank annotation
            if (field.isAnnotationPresent(NotBlank.class)) {
                if (value == null
                        || value.toString().trim().isEmpty()) {

                    errors.add(name + " must not be blank");
                }
            }

            // Check MaxLength annotation
            if (field.isAnnotationPresent(MaxLength.class)
                    && value != null) {

                MaxLength maxLength =
                        field.getAnnotation(MaxLength.class);

                int max = maxLength.value();

                if (value.toString().length() > max) {
                    errors.add(name + " must not exceed "
                            + max + " characters");
                }
            }
        }

        return errors;
    }

    // Main method
    public static void main(String[] args)
            throws IllegalAccessException {

        SignupForm form = new SignupForm(
                "",
                "verylongemailaddress@example.com"
        );

        List<String> errors = validate(form);

        if (errors.isEmpty()) {
            System.out.println("Signup form is valid!");
        } else {
            System.out.println("Validation errors:");

            for (String error : errors) {
                System.out.println(error);
            }
        }
    }
}
