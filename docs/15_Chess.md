# Problem 15: Chess — Complete Guide

---

## Part 1: Understanding the Problem

A simplified chess game with piece movement validation, capture logic, and board display.

### Requirements
- ✓ Different piece types with unique movement rules
- ✓ Validate moves (bounds, friendly fire, piece-specific rules)
- ✓ Capture enemy pieces
- ✓ Display board with standard notation

---

## Part 2: The Key Insight — Polymorphism for Piece Movement

### The Problem
Each piece moves differently. King moves 1 square. Rook slides horizontally/vertically. Knight jumps in L-shape. How to handle this WITHOUT a huge switch statement?

### The Solution — Abstract Method + Polymorphism
```java
public abstract class Piece {
    private final Color color;
    private Position position;

    // Each subclass defines its own movement rules
    public abstract List<Position> getValidMoves(ChessBoard board);

    public boolean canMoveTo(Position target, ChessBoard board) {
        return getValidMoves(board).contains(target);
    }
}
```

The board calls `piece.getValidMoves(board)` — it doesn't know or care if it's a King, Rook, or Pawn. **Polymorphism handles it.**

---

## Part 3: The Code — Each Piece

### Knight — L-Shaped Jumps
```java
public class Knight extends Piece {
    public List<Position> getValidMoves(ChessBoard board) {
        int[][] jumps = {{-2,-1},{-2,1},{-1,-2},{-1,2},{1,-2},{1,2},{2,-1},{2,1}};
        List<Position> moves = new ArrayList<>();
        for (int[] j : jumps) {
            Position p = new Position(row + j[0], col + j[1]);
            if (p.isValid() && !board.hasFriendlyPiece(p, getColor()))
                moves.add(p);
        }
        return moves;
    }
}
```
**Knight can jump over pieces** — only checks destination, not path.

### Rook — Linear Slides
```java
public class Rook extends Piece {
    public List<Position> getValidMoves(ChessBoard board) {
        int[][] dirs = {{0,1},{0,-1},{1,0},{-1,0}};
        for (int[] d : dirs)
            addLinearMoves(moves, d[0], d[1], board);
        return moves;
    }

    void addLinearMoves(List<Position> moves, int dr, int dc, ChessBoard board) {
        int r = row + dr, c = col + dc;
        while (new Position(r, c).isValid()) {
            Position p = new Position(r, c);
            if (board.hasFriendlyPiece(p, color)) break;  // blocked by own piece
            moves.add(p);
            if (board.hasEnemyPiece(p, color)) break;     // can capture, but stop
            r += dr; c += dc;
        }
    }
}
```
**Rook slides until blocked.** Can capture the first enemy in its path, then stops.

### Pawn — Forward + Diagonal Capture
```java
public class Pawn extends Piece {
    public List<Position> getValidMoves(ChessBoard board) {
        int dir = (color == WHITE) ? 1 : -1;  // white goes up, black goes down

        // Forward one (only if empty)
        Position fwd = new Position(row + dir, col);
        if (fwd.isValid() && board.getPieceAt(fwd) == null)
            moves.add(fwd);

        // Forward two from starting row
        if (atStartingRow && fwd2.isValid() && board.getPieceAt(fwd2) == null)
            moves.add(fwd2);

        // Diagonal capture (only if enemy present)
        for (int dc : new int[]{-1, 1}) {
            Position diag = new Position(row + dir, col + dc);
            if (diag.isValid() && board.hasEnemyPiece(diag, color))
                moves.add(diag);
        }
        return moves;
    }
}
```
**Pawn moves forward but captures diagonally** — most complex piece for its simplicity.

### King — One Square Any Direction
```java
public class King extends Piece {
    public List<Position> getValidMoves(ChessBoard board) {
        int[][] dirs = {{-1,-1},{-1,0},{-1,1},{0,-1},{0,1},{1,-1},{1,0},{1,1}};
        for (int[] d : dirs) {
            Position p = new Position(row + d[0], col + d[1]);
            if (p.isValid() && !board.hasFriendlyPiece(p, color))
                moves.add(p);
        }
        return moves;
    }
}
```

### Board — Move Execution
```java
public boolean movePiece(Position from, Position to) {
    Piece piece = getPieceAt(from);
    if (!piece.canMoveTo(to, this)) return false;  // validate via polymorphism

    Piece captured = getPieceAt(to);
    if (captured != null) captured.setCaptured(true);  // capture!

    board[from.row][from.col] = null;
    board[to.row][to.col] = piece;
    piece.setPosition(to);
    return true;
}
```

---

## Part 4: Follow-Up Questions

| Question | Answer |
|---|---|
| Add Bishop/Queen? | Bishop: `addLinearMoves` with diagonal directions. Queen: Rook + Bishop combined. Just add a new subclass. |
| Check/checkmate? | After each move, check if opponent's King is in any piece's valid moves. |
| Castling? | Special King move: King moves 2 squares, Rook jumps over. Add as special case in King's getValidMoves. |
| En passant? | Track last move. If last move was a pawn double-step, adjacent pawn can capture diagonally behind it. |

---

## Part 5: Patterns Recap

| Pattern | Where & Why |
|---|---|
| **Polymorphism** | `getValidMoves()` — each piece defines its own rules |
| **Template Method** | Base `canMoveTo()` uses abstract `getValidMoves()` |
| **SRP** | Piece defines moves, Board manages grid, Game manages turns |
| **OCP** | Add new piece types (Bishop, Queen) without changing Board |

---

📁 **Source code:** `src/chess/`
