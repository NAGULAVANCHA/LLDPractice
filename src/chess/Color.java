package chess;

/**
 * CHESS LLD
 * ==========
 * Key Concepts:
 *  - Inheritance/Polymorphism: All pieces extend Piece, each overrides getValidMoves()
 *  - Encapsulation:  Board manages state, Piece knows its own movement rules
 *  - Template Method: Piece base class with abstract movement logic
 *  - OCP:            Add new piece types without modifying Board
 *
 * Interview Points:
 *  - Each piece type has unique movement rules (single responsibility)
 *  - Board validates moves (bounds, occupied, check)
 *  - Turn management and game state (check, checkmate, stalemate)
 *  - This is a SIMPLIFIED version focusing on OOP structure
 */
public enum Color {
    WHITE, BLACK;

    public Color opposite() {
        return this == WHITE ? BLACK : WHITE;
    }
}

