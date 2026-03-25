# Problem 15: Chess

| Pattern | Why |
|---|---|
| **Polymorphism** | Each Piece subclass defines its own `getValidMoves()` |
| **Template Method** | Base `Piece` provides `canMoveTo()`, subclasses define moves |
| **Enum** | Color (WHITE, BLACK) |

## Key Design — Piece Hierarchy
```
Piece (abstract)
├── King   → moves 1 square in any direction
├── Rook   → moves horizontally/vertically (slides)
├── Knight → L-shaped (2+1), can jump over pieces
└── Pawn   → moves forward 1, captures diagonally
```

Each piece implements:
```java
public abstract List<Position> getValidMoves(ChessBoard board);
```

## Board
- `Piece[8][8]` grid
- `movePiece(from, to)` — validates move, handles capture, updates position
- `hasFriendlyPiece()` / `hasEnemyPiece()` — collision detection

## Interview Points
- Polymorphism: call `piece.getValidMoves()` without knowing if it's a King or Pawn
- Position validation: bounds checking (`0 ≤ row,col ≤ 7`)
- Capture logic: enemy piece at target → remove it
- Extensible: add Bishop, Queen by creating new subclass

📁 `src/chess/`

