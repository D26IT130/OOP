import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        AccountService service = new AccountService();

        try (Scanner sc = new Scanner(System.in)) {
            while (true) {
                System.out.println("\n1. Add Account");
                System.out.println("2. Show Count");
                System.out.println("3. Exit");
                System.out.print("Enter choice: ");

                if (!sc.hasNextInt()) {
                    System.out.println("Please enter a number.");
                    sc.nextLine();
                    continue;
                }

                int choice = sc.nextInt();
                sc.nextLine();

                switch (choice) {
                    case 1:
                        System.out.print("Enter name: ");
                        String name = sc.nextLine();

                        System.out.print("Enter balance: ");
                        if (!sc.hasNextDouble()) {
                            System.out.println("Please enter a valid balance.");
                            sc.nextLine();
                            break;
                        }
                        double balance = sc.nextDouble();
                        sc.nextLine();

                        service.addAccount(new Account(name, balance));
                        System.out.println("Account added.");
                        break;
                    case 2:
                        System.out.println("Total accounts: " + service.getCount());
                        break;
                    case 3:
                        return;
                    default:
                        System.out.println("Invalid choice.");
                }
            }
        }
    }
}
