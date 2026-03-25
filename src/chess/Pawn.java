package chess;

import java.util.ArrayList;
import java.util.List;

public class Pawn extends Piece {
    public Pawn(Color c, Position p) { super(c, p); }

    @Override
    public List<Position> getValidMoves(ChessBoard board) {
        List<Position> moves = new ArrayList<>();
        int dir = getColor() == Color.WHITE ? 1 : -1;
        int r = getPosition().getRow(), c = getPosition().getCol();

        // Forward one
        Position fwd = new Position(r + dir, c);
        if (fwd.isValid() && board.getPieceAt(fwd) == null) {
            moves.add(fwd);
            // Forward two from start
            boolean atStart = (getColor() == Color.WHITE && r == 1) || (getColor() == Color.BLACK && r == 6);
            Position fwd2 = new Position(r + 2 * dir, c);
            if (atStart && fwd2.isValid() && board.getPieceAt(fwd2) == null) moves.add(fwd2);
        }
        // Diagonal capture
        for (int dc : new int[]{-1, 1}) {
            Position diag = new Position(r + dir, c + dc);
            if (diag.isValid() && board.hasEnemyPiece(diag, getColor())) moves.add(diag);
        }
        return moves;
    }

    @Override public String getSymbol() { return "P"; }
}

