package chess;

import java.util.ArrayList;
import java.util.List;

public class King extends Piece {
    public King(Color c, Position p) { super(c, p); }

    @Override
    public List<Position> getValidMoves(ChessBoard board) {
        List<Position> moves = new ArrayList<>();
        int r = getPosition().getRow(), c = getPosition().getCol();
        int[][] dirs = {{-1,-1},{-1,0},{-1,1},{0,-1},{0,1},{1,-1},{1,0},{1,1}};
        for (int[] d : dirs) {
            Position p = new Position(r + d[0], c + d[1]);
            if (p.isValid() && !board.hasFriendlyPiece(p, getColor())) moves.add(p);
        }
        return moves;
    }

    @Override public String getSymbol() { return "K"; }
}

