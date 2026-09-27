import java.util.Scanner;

public class TollBooth {

    record Vehicle(String number, String type) {}

    public static void main(String[] args) {

        Scanner sc = new Scanner(System.in);

        int totalToll = 0;
        int bikes = 0, cars = 0, trucks = 0;

        System.out.println("Enter vehicles. Type 'done' for the number to stop.");

        while (true) {

            System.out.print("Vehicle number (or 'done'): ");
            String number = sc.nextLine();

            if ("done".equals(number)) {
                break;
            }

            System.out.print("Vehicle type (bike/car/truck): ");
            String type = sc.nextLine();

            Vehicle vehicle = new Vehicle(number, type);

            int toll = switch (type) {
                case "bike" -> 20;
                case "car" -> 50;
                case "truck" -> 150;
                default -> 0;
            };

            switch (type) {
                case "bike" -> bikes++;
                case "car" -> cars++;
                case "truck" -> trucks++;
            }

            System.out.println(
                " " + number + " (" + type + ") pays " + toll
            );

            totalToll += toll;
        }

        String mostFrequent;

        System.out.println("Total toll collected: " + totalToll);

        if (bikes >= cars && bikes >= trucks)
            mostFrequent = "bike";
        else if (cars >= bikes && cars >= trucks)
            mostFrequent = "car";
        else
            mostFrequent = "truck";

        System.out.println("Most frequent: " + mostFrequent);

        sc.close();
    }
}
