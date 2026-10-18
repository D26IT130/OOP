package model;

import java.io.Serial;

public class CurrentAccount extends Account {
    @Serial
    private static final long serialVersionUID = 1L;

    private final long overdraftLimit;

    public CurrentAccount(String ownerName, long openingBalance, long overdraftLimit) {
        super(ownerName, openingBalance);
        if (overdraftLimit < 0) {
            throw new IllegalArgumentException("Overdraft limit cannot be negative.");
        }
        this.overdraftLimit = overdraftLimit;
    }

    @Override
    public double interestRate() {
        return 0.0;
    }

    @Override
    public boolean canWithdraw(long amount) {
        return amount > 0 && amount <= availableWithdrawalAmount();
    }

    @Override
    protected long availableWithdrawalAmount() {
        try {
            return Math.addExact(getBalance(), overdraftLimit);
        } catch (ArithmeticException e) {
            return Long.MAX_VALUE;
        }
    }

    public long getOverdraftLimit() {
        return overdraftLimit;
    }
}
