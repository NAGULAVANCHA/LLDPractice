package snakeladder;

/**
 * SNAKE & LADDER LLD
 * ====================
 * Key Concepts:
 *  - OOP Modeling:     Board, Snake, Ladder, Player, Dice, Game
 *  - Composition:      Board HAS snakes and ladders
 *  - SRP:              Dice rolls, Board manages position transforms
 *  - Game loop:        Turn-based with win condition
 *
 * Interview Points:
 *  - Extensible for custom board sizes
 *  - Multiple dice support
 *  - Configurable snakes/ladders
 *  - Player queue (support N players)
 */

import java.util.*;

public class SnakeLadderBoard {
    private final int size;
    private final Map<Integer, Integer> snakes;  // head -> tail (goes DOWN)
    private final Map<Integer, Integer> ladders; // bottom -> top (goes UP)

    public SnakeLadderBoard(int size) {
        this.size = size;
        this.snakes = new HashMap<>();
        this.ladders = new HashMap<>();
    }

    public void addSnake(int head, int tail) {
        if (head <= tail) throw new IllegalArgumentException("Snake head must be above tail");
        snakes.put(head, tail);
        System.out.println("  🐍 Snake: " + head + " -> " + tail);
    }

    public void addLadder(int bottom, int top) {
        if (bottom >= top) throw new IllegalArgumentException("Ladder bottom must be below top");
        ladders.put(bottom, top);
        System.out.println("  🪜 Ladder: " + bottom + " -> " + top);
    }

    /**
     * Given a position, apply any snake/ladder transformation.
     */
    public int getFinalPosition(int position) {
        if (snakes.containsKey(position)) {
            System.out.println("    🐍 Snake! " + position + " -> " + snakes.get(position));
            return snakes.get(position);
        }
        if (ladders.containsKey(position)) {
            System.out.println("    🪜 Ladder! " + position + " -> " + ladders.get(position));
            return ladders.get(position);
        }
        return position;
    }

    public int getSize() { return size; }
    public int getWinPosition() { return size; }
}

