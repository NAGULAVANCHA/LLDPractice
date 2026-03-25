# Problem 10: Tic-Tac-Toe

| Pattern | Why |
|---|---|
| **OOP Modeling** | Board, Player, Game as separate concerns |
| **SRP** | Board handles state, Game handles flow/turns |

## ⭐ The Interview Gold — O(1) Win Detection

Instead of scanning rows/columns/diagonals after every move (O(n)), use this trick:

```
Assign: Player X = +1, Player O = -1
Maintain: rowSums[n], colSums[n], diagSum, antiDiagSum

On each move(row, col, player):
  rowSums[row] += player.value
  colSums[col] += player.value
  if (row == col) diagSum += player.value
  if (row + col == n-1) antiDiagSum += player.value

Win condition: any sum reaches +n or -n
```

### Why This Works
- X places 3 in a row → sum = +1 +1 +1 = +3 = +n ✓
- O places 3 in a row → sum = -1 -1 -1 = -3 = -n ✓
- Mixed row (X, O, X) → sum = +1 -1 +1 = +1 ≠ ±n ✗

**Time: O(1) per move** instead of O(n). Interviewers LOVE this.

## Board is Full Check
```java
moveCount == size * size  // O(1) draw detection
```

📁 `src/tictactoe/`

