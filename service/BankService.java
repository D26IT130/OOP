package service;

import exception.AccountNotFoundException;
import exception.BankException;
import exception.InvalidAmountException;
import model.Account;
import model.CurrentAccount;
import model.FixedDepositAccount;
import model.SavingsAccount;
import util.StatementFormatter;
import util.Validator;

import java.io.IOException;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

public class BankService {
    public enum AccountType {
        SAVINGS,
        CURRENT,
        FIXED_DEPOSIT
    }

    private final Repository<Account> accounts;
    private final Path logFile;

    public BankService(Repository<Account> accounts, Path logFile) {
        if (accounts == null || logFile == null) {
            throw new IllegalArgumentException("Repository and log file are required.");
        }
        this.accounts = accounts;
        this.logFile = logFile;
    }

    public Account openAccount(String ownerName, AccountType type, long openingBalance,
                               long accountLimit) throws InvalidAmountException {
        if (!Validator.isValidName(ownerName)) {
            throw new InvalidAmountException("Account holder name is invalid.");
        }
        if (type == null) {
            throw new IllegalArgumentException("Account type is required.");
        }
        if (openingBalance < 0 || accountLimit < 0) {
            throw new InvalidAmountException("Opening balance and account limit cannot be negative.");
        }

        Account account = switch (type) {
            case SAVINGS -> new SavingsAccount(ownerName.trim(), openingBalance, accountLimit);
            case CURRENT -> new CurrentAccount(ownerName.trim(), openingBalance, accountLimit);
            case FIXED_DEPOSIT -> new FixedDepositAccount(ownerName.trim(), openingBalance);
        };
        accounts.save(account);
        return account;
    }

    public void deposit(String accountNumber, long amount)
            throws BankException, IOException {
        Account account = requireAccount(accountNumber);
        account.deposit(amount);
        TransactionLog.append(logFile, "DEPOSIT," + amount);
    }

    public void withdraw(String accountNumber, long amount)
            throws BankException, IOException {
        Account account = requireAccount(accountNumber);
        account.withdraw(amount);
        TransactionLog.append(logFile, "WITHDRAW," + amount);
    }

    public void transfer(String fromAccountNumber, String toAccountNumber, long amount)
            throws BankException, IOException {
        Account from = requireAccount(fromAccountNumber);
        Account to = requireAccount(toAccountNumber);
        from.transfer(to, amount);
        if (from != to) {
            TransactionLog.append(logFile, "WITHDRAW," + amount);
            TransactionLog.append(logFile, "DEPOSIT," + amount);
        }
    }

    public String statement(String accountNumber) throws AccountNotFoundException {
        return StatementFormatter.buildStatement(requireAccount(accountNumber));
    }

    public ArrayList<Account> listAccounts() {
        ArrayList<Account> result = accounts.findAll();
        result.sort(Comparator.comparing(Account::getAccountNumber));
        return result;
    }

    public void processBatch(List<TransactionRequest> requests)
            throws BankException, InterruptedException, IOException {
        if (requests == null) {
            throw new IllegalArgumentException("Transaction batch cannot be null.");
        }
        for (TransactionRequest request : requests) {
            if (request == null || request.type() == null) {
                throw new IllegalArgumentException("Batch transactions must be specified.");
            }
            requireAccount(request.accountNumber());
            if (request.amount() <= 0) {
                throw new InvalidAmountException("Transaction amounts must be positive.");
            }
        }

        TransactionProcessor processor = new TransactionProcessor();
        List<TransactionTask> tasks = new ArrayList<>();
        List<Future<?>> results = new ArrayList<>();
        for (Account account : accounts.findAll()) {
            processor.addAccount(account);
        }
        try {
            for (TransactionRequest request : requests) {
                Account account = processor.getAccount(request.accountNumber());
                TransactionTask task = new TransactionTask(account, request.type(),
                        request.amount(), logFile);
                tasks.add(task);
                results.add(processor.submit(task));
            }
        } finally {
            processor.stop();
        }
        for (Future<?> result : results) {
            try {
                result.get();
            } catch (ExecutionException e) {
                Throwable cause = e.getCause();
                if (cause instanceof IOException ioException) {
                    throw ioException;
                }
                if (cause instanceof RuntimeException runtimeException) {
                    throw runtimeException;
                }
                throw new IllegalStateException("Batch transaction failed unexpectedly.", cause);
            }
        }
        for (TransactionTask task : tasks) {
            if (task.getFailure() != null) {
                throw task.getFailure();
            }
        }
    }

    private Account requireAccount(String accountNumber) throws AccountNotFoundException {
        Account account = accountNumber == null ? null : accounts.findById(accountNumber);
        if (account == null) {
            throw new AccountNotFoundException("Account not found: " + accountNumber);
        }
        return account;
    }
}
