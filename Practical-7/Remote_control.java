interface Switchable{ 

    public void on(); 

    public void off(); 

 

    default void toggle(){ 

        System.out.println("Toggling device.."); 

    } 

    // public void toggle(); 

} 

 

class Fan implements Switchable{ 

    public void on(){ 

        System.out.println("Fan is on"); 

    } 

 

    public void off(){ 

        System.out.println("Fan is off"); 

    } 

}; 

 

class Light implements Switchable{ 

    public void on(){ 

        System.out.println("Switch is on"); 

    } 

 

    public void off(){ 

        System.out.println("Switch is off"); 

    } 

} 

 

@FunctionalInterface 

interface SwitchPolicy { 

    boolean maySwitchOn(Switchable device, int hour); 

} 

 

public class Remote_control { 

    public static void main(String[] args) { 

 

        Switchable[] devices = { 

            new Fan(), 

            new Light() 

        }; 

 

        for (Switchable d : devices) { 

            d.toggle(); 

        } 

 

        SwitchPolicy p1 = new SwitchPolicy() { 

            public boolean maySwitchOn(Switchable d, int hour) { 

                return hour >= 6 && hour <= 22; 

            } 

        }; 

 

        // Lambda 

        SwitchPolicy p2 = (d, hour) -> hour >= 6 && hour <= 22; 

 

        System.out.println(p1.maySwitchOn(devices[0], 10)); 

        System.out.println(p2.maySwitchOn(devices[1], 23)); 

    } 

} 
