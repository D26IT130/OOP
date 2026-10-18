package model;

import java.io.Serial;

public class SavingsAccount extends Account {
    @Serial
    private static final long serialVersionUID = 1L;

    private final long minBalance;

    public SavingsAccount(String ownerName, long openingBalance, long minBalance) {
        super(ownerName, openingBalance);
        if (minBalance < 0) {
            throw new IllegalArgumentException("Minimum balance cannot be negative.");
        }
        this.minBalance = minBalance;
    }

    @Override
    public double interestRate() {
        return 4.0;
    }

    @Override
    public boolean canWithdraw(long amount) {
        return amount > 0 && amount <= availableWithdrawalAmount();
    }

    @Override
    protected long availableWithdrawalAmount() {
        return Math.max(0, getBalance() - minBalance);
    }

    public long getMinBalance() {
        return minBalance;
    }
}
