class Thermostat {

    private String location;
    private int temperature;
    private static final int min = 16;
    private static final int max = 30;
    private static int activeCount = 0;

    public Thermostat(String location, int StartTemp) {
        this.location = location;

        if (StartTemp >= min && StartTemp <= max) {
            this.temperature = StartTemp;
        } else {
            this.temperature = 22;
        }

        activeCount++;
    }

    public void raise() {
        if (temperature < max) {
            temperature++;
        } else {
            System.out.println("Already at maximum (30)");
        }
    }

    public void lower() {
        if (temperature > min) {
            temperature--;
        } else {
            System.out.println("Already at minimum (16)");
        }
    }

    public int getTemperature() {
        return temperature;
    }

    public int getActiveCount() {
        return activeCount;
    }

    public static void main(String[] args) {

        Thermostat t1 = new Thermostat("Lab", 22);

        System.out.println("Temperature: " + t1.getTemperature());

        System.out.println("Rising temperature");

        for (int i = 0; i < 10; i++) {
            t1.raise();
            System.out.println("Temperature: " + t1.getTemperature());
        }

        System.out.println("Lowering temperature");

        for (int i = 0; i < 20; i++) {
            t1.lower();
            System.out.println("Temperature: " + t1.getTemperature());
        }
    }
}
