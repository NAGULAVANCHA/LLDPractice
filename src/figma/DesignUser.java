package figma;

public class DesignUser {
    private final String name;
    private double cursorX, cursorY;

    public DesignUser(String name) {
        this.name = name;
    }

    public String getName() { return name; }
    public double getCursorX() { return cursorX; }
    public double getCursorY() { return cursorY; }

    public void moveCursor(double x, double y) {
        this.cursorX = x;
        this.cursorY = y;
    }
}

