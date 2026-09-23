class OutOfStockException extends Exception {
    int shortfall;

    OutOfStockException(int shortfall) {
        this.shortfall = shortfall;
    }
}

class InvalidQuantityException extends Exception {
    InvalidQuantityException() {
        super("Quantity must be greater than 0");
    }
}

class Warehouse {
    int stock = 10;

    void issue(String item, int qty)
            throws OutOfStockException, InvalidQuantityException {

        if (qty <= 0)
            throw new InvalidQuantityException();

        if (qty > stock)
            throw new OutOfStockException(qty - stock);

        stock = stock - qty;
        System.out.println(qty + " " + item + " issued");
    }
}

public class Stock {
    public static void main(String[] args) {

        Warehouse w = new Warehouse();

        int[] requests = {3, 5, 10, -2, 2};

        for (int qty : requests) {
            try {
                w.issue("Pen", qty);
            }
            catch (OutOfStockException e) {
                System.out.println("Out of stock. Shortfall: " + e.shortfall);
            }
            catch (InvalidQuantityException e) {
                System.out.println(e.getMessage());
            }
        }

        System.out.println("Remaining stock: " + w.stock);
    }
}
