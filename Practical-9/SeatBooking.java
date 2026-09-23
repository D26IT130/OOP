class Booking {
    int seatsLeft = 5;

    synchronized void book(String name) {
        if (seatsLeft > 0) {
            System.out.println(name + " booked a seat.");
            seatsLeft--;
        } else {
            System.out.println(name + " failed. No seats left.");
        }
    }
}

class Customer extends Thread {
    Booking booking;
    String name;

    Customer(Booking booking, String name) {
        this.booking = booking;
        this.name = name;
    }

    public void run() {
        booking.book(name);
    }
}

public class SeatBooking {
    public static void main(String[] args) throws Exception {

        Booking booking = new Booking();

        Customer[] customers = new Customer[10];

        for (int i = 0; i < 10; i++) {
            customers[i] = new Customer(booking, "Customer " + (i + 1));
            customers[i].start();
        }

        for (int i = 0; i < 10; i++) {
            customers[i].join();
        }

        System.out.println("Seats left = " + booking.seatsLeft);
    }
}
