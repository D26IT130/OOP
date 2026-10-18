abstract class Shape {
    abstract double Area();
}

class Circle extends Shape {
    private double radius;

    public Circle(double radius) {
        this.radius = radius;
    }

    @Override
    double Area() {
        return Math.PI * radius * radius;
    }
}
class Rectangle extends Shape {
    private double width;
    private double height;

    public Rectangle(double width, double height) {
        this.width = width;
        this.height = height;
    }

    @Override
    double Area() {
        return width * height;
    }
}
class Triangle extends Shape {
    private double base;
    private double height;

    public Triangle(double base, double height) {
        this.base = base;
        this.height = height;
    }

    @Override
    double Area() {
        return 0.5 * base * height;
    }
}

public class ShapeAreaCalculator{
    public static void main(String[] args) {
        Shape circle = new Circle(7);
        Shape rectangle = new Rectangle(3, 6);
        Shape triangle = new Triangle(4, 8);

        System.out.println("Circle Area: " + circle.Area());
        System.out.println("Rectangle Area: " + rectangle.Area());
        System.out.println("Triangle Area: " + triangle.Area());

        Double totalArea = circle.Area() + rectangle.Area() + triangle.Area();
        System.out.println("Total Area: " + totalArea);

        Shape largestShape = circle;
        double largestArea = circle.Area();

        if (rectangle.Area() > largestArea) {
            largestShape = rectangle;
            largestArea = rectangle.Area();
        }
        if (triangle.Area() > largestArea) {
            largestShape = triangle;
            largestArea = triangle.Area();
        }

        System.out.println("Largest Area: " + largestArea);
        System.out.println("Largest Shape: " + largestShape.getClass().getSimpleName());
        
    }
}
