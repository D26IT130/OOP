package service;

import exception.BankException;
import model.Account;
import model.MenuOption;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Scanner;

import static model.MenuOption.EXIT;

public class MiniBank {
    private static final Path DATA_DIRECTORY = Path.of("data");
    private static final Path STATE_FILE = DATA_DIRECTORY.resolve("accounts.dat");
    private static final Path LOG_DIRECTORY = DATA_DIRECTORY.resolve("logs");
    private static final Path TRANSACTION_LOG = LOG_DIRECTORY.resolve("transactions.log");
    private static final Path REPORT_FILE = DATA_DIRECTORY.resolve("end-of-day-report.txt");

    private MiniBank() {
    }

    public static void main(String[] args) throws IOException, ClassNotFoundException {
        Repository<Account> repository = new Repository<>();
        if (Files.exists(STATE_FILE)) {
            repository.saveAll(StatePersister.load(STATE_FILE));
        }
        BankService bank = new BankService(repository, TRANSACTION_LOG);

        System.out.println("Welcome to MiniBank");
        try (Scanner scanner = new Scanner(System.in)) {
            runMenu(scanner, bank);
        } finally {
            StatePersister.save(repository.findAll().toArray(Account[]::new), STATE_FILE);
            System.out.println("Account data saved.");
        }
    }

    private static void runMenu(Scanner scanner, BankService bank) throws IOException {
        boolean running = true;
        while (running && scanner.hasNextLine()) {
            printMenu();
            System.out.print("Choose an option: ");
            String input = scanner.nextLine().trim();
            int choice;
            try {
                choice = Integer.parseInt(input);
            } catch (NumberFormatException e) {
                System.out.println("Please enter a number from 1 to 9.");
                continue;
            }

            try {
                switch (choice) {
                    case 1 -> openAccount(scanner, bank);
                    case 2 -> deposit(scanner, bank);
                    case 3 -> withdraw(scanner, bank);
                    case 4 -> transfer(scanner, bank);
                    case 5 -> printStatement(scanner, bank);
                    case 6 -> listAccounts(bank);
                    case 7 -> processBatch(scanner, bank);
                    case 8 -> printReport();
                    case 9 -> {
                        System.out.println(EXIT.placeholder());
                        running = false;
                    }
                    default -> System.out.println("Invalid choice. Please enter a number from 1 to 9.");
                }
            } catch (BankException | IllegalArgumentException e) {
                System.out.println("Operation failed: " + e.getMessage());
            } catch (IOException e) {
                System.out.println("I/O operation failed: " + e.getMessage());
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                System.out.println("Operation interrupted.");
            }
        }
    }

    private static void printMenu() {
        System.out.println("\n1. Open Account");
        System.out.println("2. Deposit");
        System.out.println("3. Withdraw");
        System.out.println("4. Transfer");
        System.out.println("5. Account Statement");
        System.out.println("6. List Accounts");
        System.out.println("7. Process Transaction Batch");
        System.out.println("8. End-of-day Report");
        System.out.println("9. Exit");
    }

    private static void openAccount(Scanner scanner, BankService bank)
            throws IOException, BankException {
        String owner = readValue(scanner, "Account holder name: ");
        BankService.AccountType type = BankService.AccountType.valueOf(
                readValue(scanner, "Account type (SAVINGS, CURRENT, FIXED_DEPOSIT): ")
                        .toUpperCase(Locale.ROOT));
        long openingBalance = readAmount(scanner, "Opening balance: ");
        long limit = 0;
        if (type != BankService.AccountType.FIXED_DEPOSIT) {
            limit = readAmount(scanner, type == BankService.AccountType.SAVINGS
                    ? "Minimum balance: " : "Overdraft limit: ");
        }
        Account account = bank.openAccount(owner, type, openingBalance, limit);
        System.out.println("Account opened: " + account.getAccountNumber());
    }

    private static void deposit(Scanner scanner, BankService bank)
            throws IOException, BankException {
        String accountNumber = readValue(scanner, "Account number: ");
        bank.deposit(accountNumber, readAmount(scanner, "Deposit amount: "));
        System.out.println("Deposit completed.");
    }

    private static void withdraw(Scanner scanner, BankService bank)
            throws IOException, BankException {
        String accountNumber = readValue(scanner, "Account number: ");
        bank.withdraw(accountNumber, readAmount(scanner, "Withdrawal amount: "));
        System.out.println("Withdrawal completed.");
    }

    private static void transfer(Scanner scanner, BankService bank)
            throws IOException, BankException {
        String from = readValue(scanner, "From account number: ");
        String to = readValue(scanner, "To account number: ");
        bank.transfer(from, to, readAmount(scanner, "Transfer amount: "));
        System.out.println("Transfer completed.");
    }

    private static void printStatement(Scanner scanner, BankService bank)
            throws BankException {
        System.out.println(bank.statement(readValue(scanner, "Account number: ")));
    }

    private static void listAccounts(BankService bank) {
        ArrayList<Account> accounts = bank.listAccounts();
        if (accounts.isEmpty()) {
            System.out.println("No accounts are open.");
            return;
        }
        for (Account account : accounts) {
            System.out.println(account);
        }
    }

    private static void processBatch(Scanner scanner, BankService bank)
            throws IOException, BankException, InterruptedException {
        int count = Integer.parseInt(readValue(scanner, "Number of transactions: "));
        if (count < 0) {
            throw new IllegalArgumentException("Transaction count cannot be negative.");
        }
        List<TransactionRequest> transactions = new ArrayList<>();
        System.out.println("Enter each transaction as: ACCOUNT_NUMBER DEPOSIT|WITHDRAW AMOUNT");
        for (int i = 0; i < count; i++) {
            String[] fields = readValue(scanner, "Transaction " + (i + 1) + ": ")
                    .split("\\s+");
            if (fields.length != 3) {
                throw new IllegalArgumentException("Each transaction must have three fields.");
            }
            TransactionTask.Type type = TransactionTask.Type.valueOf(
                    fields[1].toUpperCase(Locale.ROOT));
            transactions.add(new TransactionRequest(fields[0], type,
                    Long.parseLong(fields[2])));
        }
        bank.processBatch(transactions);
        System.out.println("Batch processing completed.");
    }

    private static void printReport() throws IOException {
        Files.createDirectories(LOG_DIRECTORY);
        System.out.println(ReportGenerator.generate(LOG_DIRECTORY, REPORT_FILE));
    }

    private static String readValue(Scanner scanner, String prompt) {
        System.out.print(prompt);
        if (!scanner.hasNextLine()) {
            throw new IllegalStateException("Input ended before the operation completed.");
        }
        return scanner.nextLine().trim();
    }

    private static long readAmount(Scanner scanner, String prompt) {
        return Long.parseLong(readValue(scanner, prompt));
    }
}
