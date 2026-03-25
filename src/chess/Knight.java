package chess;

import java.util.ArrayList;
import java.util.List;

public class Knight extends Piece {
    public Knight(Color c, Position p) { super(c, p); }

    @Override
    public List<Position> getValidMoves(ChessBoard board) {
        List<Position> moves = new ArrayList<>();
        int r = getPosition().getRow(), c = getPosition().getCol();
        int[][] jumps = {{-2,-1},{-2,1},{-1,-2},{-1,2},{1,-2},{1,2},{2,-1},{2,1}};
        for (int[] j : jumps) {
            Position p = new Position(r + j[0], c + j[1]);
            if (p.isValid() && !board.hasFriendlyPiece(p, getColor())) moves.add(p);
        }
        return moves;
    }

    @Override public String getSymbol() { return "N"; }
}

