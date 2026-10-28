
import java.lang.annotation.*;
import java.lang.reflect.*;

// Define the Run annotation
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
@interface Run {
}

// Class containing test methods
class MyTests {

    @Run
    public void testAddition() {
        System.out.println("Addition test executed");
    }

    @Run
    public void testSubtraction() {
        System.out.println("Subtraction test executed");
    }

    public void normalMethod() {
        System.out.println("This method should not run");
    }

    @Run
    public void testMultiplication() {
        System.out.println("Multiplication test executed");
    }
}

// Mini test runner
public class MiniRunner {

    public static void main(String[] args) throws Exception {

        MyTests testObject = new MyTests();

        Class<?> cls = testObject.getClass();

        int count = 0;

        for (Method method : cls.getDeclaredMethods()) {

            // Run only methods annotated with @Run
            if (method.isAnnotationPresent(Run.class)) {

                method.invoke(testObject);
                count++;
            }
        }

        System.out.println("Total tests run: " + count);
    }
}
