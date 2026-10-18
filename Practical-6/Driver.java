
interface Switchable {
    void on();
    void off();

    default void toggle() {
        off();
        on();
    }
}

class Fan implements Switchable {
    public void on() {
        System.out.println("Fan is ON");
    }

    public void off() {
        System.out.println("Fan is OFF");
    }
}

class Light implements Switchable {
    public void on() {
        System.out.println("Light is ON");
    }

    public void off() {
        System.out.println("Light is OFF");
    }
}

@FunctionalInterface
interface SwitchPolicy {
    boolean maySwitchOn(Switchable device, int hour);
}

public class Driver {
    public static void main(String[] args) {
        Switchable[] devices = {new Fan(), new Light()};

        for (Switchable device : devices) {
            device.toggle();
        }

        SwitchPolicy policy1 = new SwitchPolicy() {
            public boolean maySwitchOn(Switchable device, int hour) {
                return hour >= 18 && hour <= 22;
            }
        };

        SwitchPolicy policy2 = (device, hour) -> hour >= 6 && hour <= 10;

        System.out.println("Anonymous class: " +
                policy1.maySwitchOn(devices[0], 20));

        System.out.println("Lambda expression: " +
                policy2.maySwitchOn(devices[1], 8));
    }
}
