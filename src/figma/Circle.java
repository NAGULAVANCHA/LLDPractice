package figma;

public class Circle extends Shape {
    public Circle(double x, double y, double radius, String color) {
        super(x, y, radius * 2, radius * 2, color);
    }

    public double getRadius() { return getWidth() / 2; }

    @Override
    public String getType() { return "Circle"; }
}

