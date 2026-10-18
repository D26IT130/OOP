public class PasswordChecker {

    public static boolean checkLength(String pw) {
        return pw.length() >= 8;
    }

    public static boolean checkUppercase(String pw) {
        return pw.matches(".*[A-Z].*");
    }

    public static boolean checkDigit(String pw) {
        return pw.matches(".*[0-9].*");
    }

    public static boolean checkSpecial(String pw) {
        return pw.matches(".*[^a-zA-Z0-9].*");
    }

    public static String strength(String pw) {

        int count = 0;

        if (checkLength(pw))
            count++;

        if (checkUppercase(pw))
            count++;

        if (checkDigit(pw))
            count++;

        if (checkSpecial(pw))
            count++;

        if (count <= 1)
            return "Weak";
        else if (count <= 3)
            return "Medium";
        else
            return "Strong";
    }
}
