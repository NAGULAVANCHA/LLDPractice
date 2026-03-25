package snakeladder;

public class SnakeLadderPlayer {
    private final String name;
    private int position;

    public SnakeLadderPlayer(String name) {
        this.name = name;
        this.position = 0; // start off the board
    }

    public String getName() { return name; }
    public int getPosition() { return position; }
    public void setPosition(int position) { this.position = position; }

    @Override
    public String toString() { return name + " (pos=" + position + ")"; }
}

