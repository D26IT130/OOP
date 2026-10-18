
import java.util.*;

class UniqueVisitors {
    public static void main(String[] args) {
        String[] logins = {
            "Piyush", "Priya", "Amit", "Rahul", "Neha", "Priya", "Raj"
        };

        HashSet<String> uniqueUsers = new HashSet<>();
        LinkedHashSet<String> orderedUsers = new LinkedHashSet<>();

        for (String name : logins) {
            uniqueUsers.add(name);
            orderedUsers.add(name);
        }

        System.out.println("Unique visitors count: " + uniqueUsers.size());

        System.out.println("First three distinct names:");
        int count = 0;

        for (String name : orderedUsers) {
            if (count == 3) {
                break;
            }
            System.out.println(name);
            count++;
        }
    }
}
