package figma;

public class Rectangle extends Shape {
    public Rectangle(double x, double y, double w, double h, String color) {
        super(x, y, w, h, color);
    }

    @Override
    public String getType() { return "Rectangle"; }
}

