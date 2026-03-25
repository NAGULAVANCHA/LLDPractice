package snakeladder;

import java.util.Random;

public class Dice {
    private final int numDice;
    private final int faces;
    private final Random random;

    public Dice(int numDice, int faces) {
        this.numDice = numDice;
        this.faces = faces;
        this.random = new Random();
    }

    public Dice() { this(1, 6); }

    public int roll() {
        int total = 0;
        for (int i = 0; i < numDice; i++) {
            total += random.nextInt(faces) + 1;
        }
        return total;
    }
}

