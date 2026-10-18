package service;

import exception.InsufficientFundsException;
import exception.InvalidAmountException;
import model.Account;
import model.CurrentAccount;
import model.SavingsAccount;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import java.nio.file.Path;
import java.util.ArrayList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class BankServiceTest {
    @Test
    void accountNumbersRemainUniqueAfterReload(@TempDir Path directory) throws Exception {
        Account original = new CurrentAccount("Persisted User", 100, 0);
        Path stateFile = directory.resolve("accounts.dat");

        StatePersister.save(new Account[] {original}, stateFile);
        Account restored = StatePersister.load(stateFile)[0];
        Account newlyOpened = new CurrentAccount("New User", 100, 0);

        assertNotEquals(restored.getAccountNumber(), newlyOpened.getAccountNumber());
    }

    @Test
    void depositIncreasesBalance() throws Exception {
        Account account = new CurrentAccount("Test User", 100, 0);

        account.deposit(50);

        assertEquals(150, account.getBalance());
    }

    @Test
    void withdrawalOverLimitThrowsInsufficientFundsException() {
        Account account = new SavingsAccount("Test User", 100, 50);

        assertThrows(InsufficientFundsException.class, () -> account.withdraw(51));
        assertEquals(100, account.getBalance());
    }

    @Test
    void concurrentDepositsKeepCorrectBalance() throws InterruptedException {
        Account account = new CurrentAccount("Thread Test", 0, 0);
        int workerCount = 10;
        int depositsPerWorker = 10_000;
        ExecutorService executor = Executors.newFixedThreadPool(workerCount);
        try {
            for (int i = 0; i < workerCount; i++) {
                executor.execute(() -> {
                    for (int j = 0; j < depositsPerWorker; j++) {
                        try {
                            account.deposit(1);
                        } catch (InvalidAmountException e) {
                            throw new AssertionError("Valid deposit unexpectedly failed.", e);
                        }
                    }
                });
            }
        } finally {
            executor.shutdown();
            if (!executor.awaitTermination(30, TimeUnit.SECONDS)) {
                executor.shutdownNow();
                throw new AssertionError("Deposit workers did not finish.");
            }
        }

        assertEquals((long) workerCount * depositsPerWorker, account.getBalance());
    }

    @Test
    void batchProcessorCompletesTransactions() throws Exception {
        Repository<Account> repository = new Repository<>();
        Account account = new CurrentAccount("Batch User", 0, 0);
        repository.save(account);
        Path logFile = Path.of("target", "test-data", "batch-transactions.log");
        BankService bank = new BankService(repository, logFile);

        ArrayList<TransactionRequest> requests = new ArrayList<>();
        for (int i = 0; i < 100; i++) {
            requests.add(new TransactionRequest(account.getAccountNumber(),
                    TransactionTask.Type.DEPOSIT, 1));
        }
        bank.processBatch(requests);

        assertEquals(100, account.getBalance());
    }
}
