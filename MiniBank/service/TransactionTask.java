package service;

import exception.BankException;
import exception.InvalidAmountException;
import model.Account;

import java.io.IOException;
import java.nio.file.Path;

public class TransactionTask implements Runnable {
    public enum Type {
        DEPOSIT,
        WITHDRAW
    }

    private final Account account;
    private final Type type;
    private final long amount;
    private final Path logFile;
    private volatile BankException failure;

    public TransactionTask(Account account, Type type, long amount) {
        this(account, type, amount, null);
    }

    public TransactionTask(Account account, Type type, long amount, Path logFile) {
        if (account == null || type == null) {
            throw new IllegalArgumentException("Account and transaction type are required.");
        }
        this.account = account;
        this.type = type;
        this.amount = amount;
        this.logFile = logFile;
    }

    @Override
    public void run() {
        try {
            if (type == Type.DEPOSIT) {
                account.deposit(amount);
            } else {
                account.withdraw(amount);
            }
            if (logFile != null) {
                TransactionLog.append(logFile, type.name() + "," + amount);
            }
        } catch (BankException e) {
            failure = e;
            System.out.println(Thread.currentThread().getName()
                    + " transaction failed: " + e.getMessage());
        } catch (IOException e) {
            throw new IllegalStateException("Could not write transaction log.", e);
        }
    }

    public BankException getFailure() {
        return failure;
    }
}
