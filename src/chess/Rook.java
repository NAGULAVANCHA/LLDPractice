package chess;

import java.util.ArrayList;
import java.util.List;

public class Rook extends Piece {
    public Rook(Color c, Position p) { super(c, p); }

    @Override
    public List<Position> getValidMoves(ChessBoard board) {
        List<Position> moves = new ArrayList<>();
        int[][] dirs = {{0,1},{0,-1},{1,0},{-1,0}};
        for (int[] d : dirs) {
            addLinearMoves(moves, d[0], d[1], board);
        }
        return moves;
    }

    protected void addLinearMoves(List<Position> moves, int dr, int dc, ChessBoard board) {
        int r = getPosition().getRow() + dr;
        int c = getPosition().getCol() + dc;
        while (new Position(r, c).isValid()) {
            Position p = new Position(r, c);
            if (board.hasFriendlyPiece(p, getColor())) break;
            moves.add(p);
            if (board.hasEnemyPiece(p, getColor())) break; // can capture but stop
            r += dr;
            c += dc;
        }
    }

    @Override public String getSymbol() { return "R"; }
}

