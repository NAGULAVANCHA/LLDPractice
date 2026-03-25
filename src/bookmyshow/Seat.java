package bookmyshow;

public class Seat {
    private final int row;
    private final int col;
    private final SeatType type;

    public Seat(int row, int col, SeatType type) {
        this.row = row;
        this.col = col;
        this.type = type;
    }

    public int getRow() { return row; }
    public int getCol() { return col; }
    public SeatType getType() { return type; }
    public double getPrice() { return type.getPrice(); }

    public String getSeatId() { return (char) ('A' + row) + "" + (col + 1); }

    @Override
    public String toString() { return getSeatId() + "(" + type + ")"; }

    @Override
    public int hashCode() { return 31 * row + col; }

    @Override
    public boolean equals(Object o) {
        if (!(o instanceof Seat s)) return false;
        return row == s.row && col == s.col;
    }
}

