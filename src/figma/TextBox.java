package figma;

public class TextBox extends Shape {
    private String text;

    public TextBox(double x, double y, double w, double h, String color, String text) {
        super(x, y, w, h, color);
        this.text = text;
    }

    public String getText() { return text; }
    public void setText(String text) { this.text = text; }

    @Override
    public String getType() { return "Text"; }

    @Override
    public String toString() {
        return super.toString() + " \"" + text + "\"";
    }
}

