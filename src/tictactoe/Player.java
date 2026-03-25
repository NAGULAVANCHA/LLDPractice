package tictactoe;

/**
 * TIC-TAC-TOE LLD
 * =================
 * Key Concepts:
 *  - OOP Modeling:   Board, Player, Move, Game as separate concerns
 *  - SRP:            Board handles state, Game handles flow
 *  - O(1) Win Check: Track row/col/diagonal sums instead of scanning
 *
 * O(1) Win Detection Trick (Interview Gold):
 *  - Assign Player X = +1, Player O = -1
 *  - Maintain sums for each row, column, and both diagonals
 *  - On each move, add the player's value to the relevant row/col/diag
 *  - If any sum reaches +n or -n, that player wins (n = board size)
 *  - This avoids O(n) scanning after every move!
 */
public class Player {
    private final String name;
    private final char symbol; // 'X' or 'O'
    private final int value;   // +1 or -1 (for O(1) win detection)

    public Player(String name, char symbol, int value) {
        this.name = name;
        this.symbol = symbol;
        this.value = value;
    }

    public String getName() { return name; }
    public char getSymbol() { return symbol; }
    public int getValue() { return value; }

    @Override
    public String toString() { return name + " (" + symbol + ")"; }
}

