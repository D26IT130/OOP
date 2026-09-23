import java.util.Scanner;

// Custom exception
class DivideByZeroException extends Exception {
    public DivideByZeroException(String message) {
        super(message);
    }
}

public class GuardedCalculator {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        boolean success = false;

        while (!success) {
            try {
                System.out.print("Enter first number: ");
                double num1 = Double.parseDouble(scanner.nextLine());

                System.out.print("Enter operator (+, -, *, /): ");
                String operator = scanner.nextLine();

                System.out.print("Enter second number: ");
                double num2 = Double.parseDouble(scanner.nextLine());

                double result;

                switch (operator) {
                    case "+":
                        result = num1 + num2;
                        break;

                    case "-":
                        result = num1 - num2;
                        break;

                    case "*":
                        result = num1 * num2;
                        break;

                    case "/":
                        if (num2 == 0) {
                            throw new DivideByZeroException(
                                "Cannot divide by zero."
                            );
                        }
                        result = num1 / num2;
                        break;

                    default:
                        System.out.println("Invalid operator. Please try again.");
                        continue;
                }

                System.out.println("Result: " + result);
                success = true;

            } catch (NumberFormatException e) {
                System.out.println(
                    "Invalid number input, Please enter valid numbers"
                );

            } catch (DivideByZeroException e) {
                System.out.println("Division error: " + e.getMessage());

            } finally {
                System.out.println("Attempt logged.");
                System.out.println();
            }
        }

        System.out.println("Valid calculation completed.");
        scanner.close();
    }
}
