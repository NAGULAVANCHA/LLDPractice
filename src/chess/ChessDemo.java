package chess;

public class ChessDemo {
    public static void main(String[] args) {
        System.out.println("=== Chess Demo ===\n");

        ChessBoard board = new ChessBoard();
        board.setupStandard();
        board.display();

        // Opening moves
        System.out.println("--- Opening Moves ---");
        board.movePiece(new Position(1, 4), new Position(3, 4)); // e2-e4
        board.movePiece(new Position(6, 4), new Position(4, 4)); // e7-e5
        board.movePiece(new Position(0, 6), new Position(2, 5)); // Ng1-f3
        board.movePiece(new Position(7, 1), new Position(5, 2)); // Nb8-c6
        board.display();

        // Show valid moves for white knight at f3
        Piece knight = board.getPieceAt(new Position(2, 5));
        System.out.println("Valid moves for " + knight + " at f3: " + knight.getValidMoves(board));

        // Try invalid move
        System.out.println("\n--- Invalid move attempt ---");
        board.movePiece(new Position(0, 0), new Position(5, 0)); // Rook blocked by pawn

        // Capture
        System.out.println("\n--- Capture ---");
        board.movePiece(new Position(2, 5), new Position(4, 4)); // Nf3 captures e5 pawn
        board.display();
    }
}

