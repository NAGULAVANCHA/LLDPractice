package chess;

import java.util.List;

public abstract class Piece {
    private final Color color;
    private Position position;
    private boolean captured;

    public Piece(Color color, Position position) {
        this.color = color;
        this.position = position;
        this.captured = false;
    }

    /**
     * Each piece defines its own valid move positions.
     * Returns list of positions this piece can move to.
     */
    public abstract List<Position> getValidMoves(ChessBoard board);

    public abstract String getSymbol();

    public boolean canMoveTo(Position target, ChessBoard board) {
        return getValidMoves(board).contains(target);
    }

    public Color getColor() { return color; }
    public Position getPosition() { return position; }
    public void setPosition(Position position) { this.position = position; }
    public boolean isCaptured() { return captured; }
    public void setCaptured(boolean captured) { this.captured = captured; }

    @Override
    public String toString() {
        return getSymbol() + "(" + color.name().charAt(0) + ")";
    }
}

