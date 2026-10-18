import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class AccountServiceTest {
    @Test
    void addingAccountIncreasesCount() {
        AccountService service = new AccountService();
        service.addAccount(new Account("Rahul", 1000));
        assertEquals(1, service.getCount());
    }

    @Test
    void addingTwoAccountsIncreasesCountToTwo() {
        AccountService service = new AccountService();
        service.addAccount(new Account("Rahul", 1000));
        service.addAccount(new Account("Priya", 2000));
        assertEquals(2, service.getCount());
    }

    @Test
    void addingNullAccountThrowsException() {
        AccountService service = new AccountService();
        assertThrows(IllegalArgumentException.class,
                     () -> service.addAccount(null));
    }
}
