
class Box<T> {
    private T value;

    public void set(T value) {
        this.value = value;
    }

    public T get() {
        return value;
    }
}

class Pair<K, V> {
    K first;
    V second;

    Pair(K first, V second) {
        this.first = first;
        this.second = second;
    }

    void display() {
        System.out.println(first + " : " + second);
    }
}

class GenericContainers {
    public static <T> T firstElement(T[] array) {
        return array[0];
    }

    public static void main(String[] args) {
        Box<Integer> b1 = new Box<>();
        b1.set(100);
        System.out.println("Integer Box: " + b1.get());

        Box<String> b2 = new Box<>();
        b2.set("Hello Java");
        System.out.println("String Box: " + b2.get());

        Pair<String, Integer> p1 = new Pair<>("Age", 20);
        p1.display();

        String[] names = {"Rahul", "Priya", "Amit"};
        Integer[] numbers = {10, 20, 30};

        System.out.println("First name: " + firstElement(names));
        System.out.println("First number: " + firstElement(numbers));
    }
}
