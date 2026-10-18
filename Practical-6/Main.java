
@FunctionalInterface
interface Notifier {
    void send(String message);
}

interface Urgent {
}

public class Main {
    public static void main(String[] args) {
        Notifier email = msg -> System.out.println("Email: " + msg);

        Notifier sms = msg -> System.out.println("SMS: " + msg);

        class UrgentNotifier implements Notifier, Urgent {
            public void send(String message) {
                System.out.println("Urgent: " + message);
            }
        }

        Notifier urgent = new UrgentNotifier();

        Notifier[] senders = {email, sms, urgent};

        String message = "Meeting at 10 AM";

        for (Notifier sender : senders) {
            sender.send(message);

            if (sender instanceof Urgent) {
                sender.send(message);
            }
        }
    }
}
