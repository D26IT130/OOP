import java.util.Scanner;

public class Driver {
    public static void main(String[] args) {

        String[] logs = {
            "18:05 Hello Piyush",
            "18:10 Virat are you there?",
            "18:15 Yes piyush, i am here",
        };

        Scanner sc = new Scanner(System.in);

        System.out.print("Enter keyword: ");
        String keyword = sc.nextLine();

        String result = ChatFilter.filterLogs(logs, keyword);

        System.out.println(result);

        sc.close();
    }
}
