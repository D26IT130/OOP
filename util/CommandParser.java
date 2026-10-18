package util;

import model.Command;
import model.TransactionType;

public class CommandParser {
    private CommandParser() {
    }

    public static Command parse(String line) {
        if (line == null) {
            throw new IllegalArgumentException("Command cannot be null.");
        }

        String[] parts = line.trim().split("\\s+");
        if (parts.length != 3) {
            throw new IllegalArgumentException(
                    "Command must contain a type, account number, and amount.");
        }

        TransactionType type;
        try {
            type = TransactionType.valueOf(parts[0]);
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("Unknown transaction type: " + parts[0], e);
        }

        long amount;
        try {
            amount = Long.parseLong(parts[2]);
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException("Amount must be a whole number.", e);
        }
        if (amount <= 0) {
            throw new IllegalArgumentException("Amount must be positive.");
        }

        return new Command(type, parts[1], amount);
    }
}
