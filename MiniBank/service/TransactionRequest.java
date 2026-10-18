package service;

public record TransactionRequest(String accountNumber, TransactionTask.Type type, long amount) {
}
