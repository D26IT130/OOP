import java.util.ArrayList;

public class AccountService {
    private final ArrayList<Account> accounts = new ArrayList<>();

    public void addAccount(Account account) {
        if (account == null) {
            throw new IllegalArgumentException("Invalid account");
        }
        accounts.add(account);
    }

    public int getCount() {
        return accounts.size();
    }

    public Account getAccount(int index) {
        if (index < 0 || index >= accounts.size()) {
            throw new IllegalArgumentException("Invalid account index");
        }
        return accounts.get(index);
    }
}
