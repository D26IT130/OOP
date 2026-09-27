import java.util.Scanner;

class VendingMachine {

    enum Coin { ONE, TWO, FIVE, TEN }

    public static void main(String[] args) {

        final int price = 15;
        int total = 0;
        Scanner sc = new Scanner(System.in);

        while (total < price) {

            System.out.println("Coin: ");
            String n = sc.next().toUpperCase();

            Coin coin;

            try {
                coin = Coin.valueOf(n);
            } catch (IllegalArgumentException e) {
                System.out.println("Not a valid coin, try again");
                continue;
            }

            int value = switch (coin) {
                case ONE -> 1;
                case TWO -> 2;
                case FIVE -> 5;
                case TEN -> 10;
            };

            total += value;
            System.out.println("Inserted so far: " + total);
        }

        // Loop ended, so total >= PRICE. Change is the surplus.
        System.out.println("Paid. Change: " + (total - price));

        sc.close();
    }
}
