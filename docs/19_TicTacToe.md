# Problem 10: Tic-Tac-Toe — Complete Guide

---

## Part 1: Understanding the Problem

A 2-player game on an N×N grid. Players alternate placing X and O. First to fill a complete row, column, or diagonal wins.

### Requirements
- ✓ Configurable board size (3×3, 4×4, etc.)
- ✓ Turn-based gameplay
- ✓ **O(1) win detection** — the key interview insight!
- ✓ Draw detection

---

## Part 2: ⭐ The Interview Gold — O(1) Win Detection

### The Naive Way (O(n) per move)
After every move, scan the entire row, column, and diagonals to check if all cells match. For an N×N board, that's O(n) work per move.

### The Smart Way (O(1) per move)

**Trick:** Assign numerical values to players and track sums.

```
Player X = +1
Player O = -1

Maintain:
  rowSums[n]      — sum of values in each row
  colSums[n]      — sum of values in each column
  diagSum          — sum of main diagonal (row == col)
  antiDiagSum      — sum of anti-diagonal (row + col == n-1)
```

### On each move(row, col, player):
```java
rowSums[row] += player.value;      // +1 or -1
colSums[col] += player.value;
if (row == col) diagSum += player.value;
if (row + col == size - 1) antiDiagSum += player.value;

// Win check: did ANY sum reach ±n?
boolean won = Math.abs(rowSums[row]) == size ||
              Math.abs(colSums[col]) == size ||
              Math.abs(diagSum) == size ||
              Math.abs(antiDiagSum) == size;
```

### Why This Works
```
Board size = 3

X plays 3 in row 0: rowSums[0] = +1 +1 +1 = +3 = +n → X wins! ✓
O plays 3 in col 2: colSums[2] = -1 -1 -1 = -3 = -n → O wins! ✓
Mixed row (X,O,X):  rowSums[0] = +1 -1 +1 = +1 ≠ ±3  → no win  ✗
```

**Time: O(1) per move.** No scanning. Interviewers LOVE this.

### Draw Detection
```java
public boolean isFull() {
    return moveCount == size * size;  // O(1)!
}
```

---

## Part 3: The Code — Explained

### Player
```java
public class Player {
    private final String name;
    private final char symbol;   // 'X' or 'O'
    private final int value;     // +1 or -1 — for O(1) win detection
}
```

### Board
```java
public class Board {
    private final char[][] grid;
    private final int[] rowSums, colSums;
    private int diagSum, antiDiagSum, moveCount;

    public boolean makeMove(int row, int col, Player player) {
        // Validate bounds and empty cell
        grid[row][col] = player.getSymbol();
        int val = player.getValue();
        moveCount++;

        rowSums[row] += val;
        colSums[col] += val;
        if (row == col) diagSum += val;
        if (row + col == size - 1) antiDiagSum += val;

        // O(1) win check
        return Math.abs(rowSums[row]) == size ||
               Math.abs(colSums[col]) == size ||
               Math.abs(diagSum) == size ||
               Math.abs(antiDiagSum) == size;
    }
}
```

### Game — Turn Controller
```java
public class Game {
    private final Board board;
    private final Player player1, player2;
    private Player currentPlayer;

    public boolean play(int row, int col) {
        boolean won = board.makeMove(row, col, currentPlayer);
        if (won) { currentPlayer + " WINS!"; return true; }
        if (board.isFull()) { "DRAW!"; return true; }
        currentPlayer = (currentPlayer == player1) ? player2 : player1;
        return false;
    }
}
```

---

## Part 4: Walkthrough

```
Board (3×3), X=+1, O=-1

X plays (0,0):  rowSums=[1,0,0]  colSums=[1,0,0]  diag=1  anti=0
O plays (0,1):  rowSums=[0,0,0]  colSums=[1,-1,0] diag=1  anti=0
X plays (1,1):  rowSums=[0,1,0]  colSums=[1,0,0]  diag=2  anti=0
O plays (0,2):  rowSums=[-1,1,0] colSums=[1,-1,-1] diag=2 anti=-1
X plays (2,2):  rowSums=[-1,1,1] colSums=[1,-1,0] diag=3  anti=-1
                                                    ↑
                                              diag == 3 == size → X WINS! 🏆
```

---

## Part 5: Follow-Up Questions

| Question | Answer |
|---|---|
| What about NxN boards? | This algorithm scales — works for ANY N with O(1) per move. |
| How to support 3+ players? | Can't use ±1 trick with 3 players. Fall back to O(n) scanning, or use per-player sum arrays. |
| AI opponent? | Minimax algorithm with alpha-beta pruning. Separate concern from the board. |
| How to undo a move? | Store previous sums, decrement. Or use Command Pattern. |

---

## Part 6: Patterns Recap

| Pattern | Where & Why |
|---|---|
| **O(1) Sum Tracking** | Core algorithm — avoids scanning |
| **SRP** | Board manages state, Game manages turns |
| **Encapsulation** | Grid and sums are private, accessed via makeMove() |

---

📁 **Source code:** `src/tictactoe/`
