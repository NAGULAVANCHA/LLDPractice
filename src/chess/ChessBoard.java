package chess;

import java.util.ArrayList;
import java.util.List;

public class ChessBoard {
    private final Piece[][] board = new Piece[8][8];
    private final List<Piece> pieces = new ArrayList<>();

    public void placePiece(Piece piece) {
        Position p = piece.getPosition();
        board[p.getRow()][p.getCol()] = piece;
        pieces.add(piece);
    }

    public Piece getPieceAt(Position p) {
        if (!p.isValid()) return null;
        return board[p.getRow()][p.getCol()];
    }

    public boolean hasFriendlyPiece(Position p, Color color) {
        Piece piece = getPieceAt(p);
        return piece != null && piece.getColor() == color;
    }

    public boolean hasEnemyPiece(Position p, Color color) {
        Piece piece = getPieceAt(p);
        return piece != null && piece.getColor() != color;
    }

    public boolean movePiece(Position from, Position to) {
        Piece piece = getPieceAt(from);
        if (piece == null) { System.out.println("  No piece at " + from); return false; }
        if (!piece.canMoveTo(to, this)) {
            System.out.println("  Invalid move: " + piece + " " + from + " -> " + to);
            return false;
        }

        // Capture
        Piece captured = getPieceAt(to);
        if (captured != null) {
            captured.setCaptured(true);
            System.out.println("  ⚔️ " + piece + " captures " + captured + " at " + to);
        }

        board[from.getRow()][from.getCol()] = null;
        board[to.getRow()][to.getCol()] = piece;
        piece.setPosition(to);

        System.out.println("  " + piece + " moves " + from + " -> " + to);
        return true;
    }

    public void display() {
        System.out.println("\n    a   b   c   d   e   f   g   h");
        System.out.println("  +---+---+---+---+---+---+---+---+");
        for (int r = 7; r >= 0; r--) {
            System.out.print((r + 1) + " |");
            for (int c = 0; c < 8; c++) {
                Piece p = board[r][c];
                System.out.print(p == null ? "   " : " " + p + "");
                System.out.print("|");
            }
            System.out.println(" " + (r + 1));
            System.out.println("  +---+---+---+---+---+---+---+---+");
        }
        System.out.println("    a   b   c   d   e   f   g   h\n");
    }

    /**
     * Setup standard initial pieces (simplified — King, Rooks, Knights, Pawns).
     */
    public void setupStandard() {
        // White
        placePiece(new King(Color.WHITE, new Position(0, 4)));
        placePiece(new Rook(Color.WHITE, new Position(0, 0)));
        placePiece(new Rook(Color.WHITE, new Position(0, 7)));
        placePiece(new Knight(Color.WHITE, new Position(0, 1)));
        placePiece(new Knight(Color.WHITE, new Position(0, 6)));
        for (int c = 0; c < 8; c++) placePiece(new Pawn(Color.WHITE, new Position(1, c)));

        // Black
        placePiece(new King(Color.BLACK, new Position(7, 4)));
        placePiece(new Rook(Color.BLACK, new Position(7, 0)));
        placePiece(new Rook(Color.BLACK, new Position(7, 7)));
        placePiece(new Knight(Color.BLACK, new Position(7, 1)));
        placePiece(new Knight(Color.BLACK, new Position(7, 6)));
        for (int c = 0; c < 8; c++) placePiece(new Pawn(Color.BLACK, new Position(6, c)));
    }
}

