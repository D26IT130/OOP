package model;

import exception.AccountNotFoundException;
import exception.BankException;
import exception.InsufficientFundsException;
import exception.InvalidAmountException;
import model.annotation.Id;
import model.annotation.MaxLength;
import model.annotation.Positive;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.Serializable;
import java.util.Objects;

public abstract class Account implements Transactable, 
InterestBearing, Serializable {
    private static final long serialVersionUID = 1L;

    private static long accountCounter;

    @Id
    @MaxLength(6)
    private final String accountNumber;
    @MaxLength(50)
    private String ownerName;
    @Positive
    private long balance;
    private boolean active;

    public Account(String ownerName, long openingBalance) {
        if (openingBalance < 0) {
            throw new IllegalArgumentException("Opening balance cannot be negative.");
        }
        this.accountNumber = nextAccountNumber();
        this.ownerName = ownerName;
        this.balance = openingBalance;
        this.active = true;
    }

    public Account(String ownerName) {
        this(ownerName, 0);
    }

    private static synchronized String nextAccountNumber() {
        return String.format("AC%04d", ++accountCounter);
    }

    private void readObject(ObjectInputStream input) throws IOException, ClassNotFoundException {
        input.defaultReadObject();
        if (accountNumber == null || !accountNumber.matches("AC\\d+")) {
            throw new IOException("Invalid serialized account number: " + accountNumber);
        }
        String numericId = accountNumber.substring(2);
        try {
            long restoredCounter = Long.parseLong(numericId);
            synchronized (Account.class) {
                accountCounter = Math.max(accountCounter, restoredCounter);
            }
        } catch (NumberFormatException e) {
            throw new IOException("Invalid serialized account number: " + accountNumber, e);
        }
    }

    @Override
    public synchronized void deposit(long amount) throws InvalidAmountException {
        if (amount <= 0) {
            throw new InvalidAmountException("Deposit amount must be positive.");
        }
        balance = Math.addExact(balance, amount);
    }

    @Override
    public synchronized void withdraw(long amount)
            throws InsufficientFundsException, InvalidAmountException {
        if (amount <= 0) {
            throw new InvalidAmountException("Withdrawal amount must be positive.");
        }
        if (!canWithdraw(amount)) {
            throw new InsufficientFundsException(
                    Math.max(0, amount - availableWithdrawalAmount()));
        }
        balance -= amount;
    }

    public void transfer(Account to, long amount) throws BankException {
        if (to == null) {
            throw new AccountNotFoundException("Destination account was not found.");
        }
        if (amount <= 0) {
            throw new InvalidAmountException("Transfer amount must be positive.");
        }
        if (to == this) {
            return;
        }

        Account firstLock = accountNumber.compareTo(to.accountNumber) < 0 ? this : to;
        Account secondLock = firstLock == this ? to : this;
        synchronized (firstLock) {
            synchronized (secondLock) {
                if (!canWithdraw(amount)) {
                    throw new InsufficientFundsException(
                            Math.max(0, amount - availableWithdrawalAmount()));
                }
                long destinationBalance = Math.addExact(to.balance, amount);
                balance -= amount;
                to.balance = destinationBalance;
            }
        }
    }

    

    public abstract double interestRate();

    public abstract boolean canWithdraw(long amount);

    protected abstract long availableWithdrawalAmount();

    public String getAccountNumber() {
        return accountNumber;
    }

    public String getOwnerName() {
        return ownerName;
    }

    public synchronized long getBalance() {
        return balance;
    }

    public boolean isActive() {
        return active;
    }

    @Override
    public String toString() {
        return "Account{accountNumber='" + accountNumber + "', ownerName='" + ownerName
                + "', balance=" + balance + "}";
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Account account)) {
            return false;
        }
        return accountNumber.equals(account.accountNumber);
    }

    @Override
    public int hashCode() {
        return Objects.hash(accountNumber);
    }
}
