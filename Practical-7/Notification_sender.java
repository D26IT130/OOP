
interface Notifier {
    void send(String message);
}

interface Urgent {
}

class UrgentNotifier implements Notifier, Urgent {
    private Notifier notifier;

    UrgentNotifier(Notifier notifier) {
        this.notifier = notifier;
    }

    public void send(String message) {
        notifier.send(message);
    }
}

public class Notification_sender {
    public static void main(String[] args) {

        Notifier email = (msg) ->
            System.out.println("Email: " + msg);

        Notifier sms = (msg) ->
            System.out.println("SMS: " + msg);

        Notifier urgentEmail = new UrgentNotifier(email);

        Notifier[] senders = {urgentEmail, sms};

        String message = "Important college notice";

        for (Notifier sender : senders) {
            sender.send(message);

            if (sender instanceof Urgent) {
                sender.send(message);
            }
        }
    }
}
