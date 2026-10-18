package model;

public enum MenuOption {
    OPEN_ACCOUNT("Open Account"),
    DEPOSIT("Deposit"),
    WITHDRAW("Withdraw"),
    TRANSFER("Transfer"),
    EXIT("Goodbye!");

    private final String placeholder;

    MenuOption(String placeholder) {
        this.placeholder = placeholder;
    }

    public String placeholder() {
        return placeholder;
    }
}
