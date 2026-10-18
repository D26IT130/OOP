// Source code is decompiled from a .class file using FernFlower decompiler (from Intellij IDEA).
public class PasswordChecker {
   public PasswordChecker() {
   }

   public static boolean checkLength(String var0) {
      return var0.length() >= 8;
   }

   public static boolean checkUppercase(String var0) {
      return var0.matches(".*[A-Z].*");
   }

   public static boolean checkDigit(String var0) {
      return var0.matches(".*[0-9].*");
   }

   public static boolean checkSpecial(String var0) {
      return var0.matches(".*[^a-zA-Z0-9].*");
   }

   public static String strength(String var0) {
      int var1 = 0;
      if (checkLength(var0)) {
         ++var1;
      }

      if (checkUppercase(var0)) {
         ++var1;
      }

      if (checkDigit(var0)) {
         ++var1;
      }

      if (checkSpecial(var0)) {
         ++var1;
      }

      if (var1 <= 1) {
         return "Weak";
      } else {
         return var1 <= 3 ? "Medium" : "Strong";
      }
   }
}
