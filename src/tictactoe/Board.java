package tictactoe;

/**
 * Board with O(1) win detection.
 */
public class Board {
    private final int size;
    private final char[][] grid;
    private final int[] rowSums;
    private final int[] colSums;
    private int diagSum;
    private int antiDiagSum;
    private int moveCount;

    public Board(int size) {
        this.size = size;
        this.grid = new char[size][size];
        this.rowSums = new int[size];
        this.colSums = new int[size];
        this.diagSum = 0;
        this.antiDiagSum = 0;
        this.moveCount = 0;

        for (int i = 0; i < size; i++)
            for (int j = 0; j < size; j++)
                grid[i][j] = '.';
    }

    /**
     * Place a move. Returns true if this move wins the game.
     */
    public boolean makeMove(int row, int col, Player player) {
        if (row < 0 || row >= size || col < 0 || col >= size) {
            throw new IllegalArgumentException("Invalid position: (" + row + "," + col + ")");
        }
        if (grid[row][col] != '.') {
            throw new IllegalArgumentException("Cell (" + row + "," + col + ") is already occupied!");
        }

        grid[row][col] = player.getSymbol();
        int val = player.getValue();
        moveCount++;

        // Update sums
        rowSums[row] += val;
        colSums[col] += val;
        if (row == col) diagSum += val;
        if (row + col == size - 1) antiDiagSum += val;

        // O(1) win check: if any sum reaches ±size, that player wins
        return Math.abs(rowSums[row]) == size ||
               Math.abs(colSums[col]) == size ||
               Math.abs(diagSum) == size ||
               Math.abs(antiDiagSum) == size;
    }

    public boolean isFull() {
        return moveCount == size * size;
    }

    public int getSize() { return size; }

    public void display() {
        System.out.println();
        for (int i = 0; i < size; i++) {
            System.out.print("  ");
            for (int j = 0; j < size; j++) {
                System.out.print(" " + grid[i][j]);
                if (j < size - 1) System.out.print(" |");
            }
            System.out.println();
            if (i < size - 1) {
                System.out.print("  ");
                for (int j = 0; j < size; j++) {
                    System.out.print("---");
                    if (j < size - 1) System.out.print("+");
                }
                System.out.println();
            }
        }
        System.out.println();
    }
}

