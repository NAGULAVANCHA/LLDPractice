package chess;

public class Position {
    private final int row; // 0-7
    private final int col; // 0-7

    public Position(int row, int col) {
        this.row = row;
        this.col = col;
    }

    public int getRow() { return row; }
    public int getCol() { return col; }

    public boolean isValid() {
        return row >= 0 && row < 8 && col >= 0 && col < 8;
    }

    @Override
    public String toString() {
        return "" + (char)('a' + col) + (row + 1);
    }

    @Override
    public boolean equals(Object o) {
        if (!(o instanceof Position p)) return false;
        return row == p.row && col == p.col;
    }

    @Override
    public int hashCode() { return 31 * row + col; }
}

