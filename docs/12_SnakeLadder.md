# Problem 12: Snake & Ladder

| Pattern | Why |
|---|---|
| **OOP Modeling** | Board, Snake, Ladder, Player, Dice, Game |
| **Composition** | Board HAS snakes (Map) and ladders (Map) |
| **Queue** | Players take turns in round-robin order |

## Key Design

### Board — Position Transforms
```java
Map<Integer, Integer> snakes;   // head → tail (goes DOWN)
Map<Integer, Integer> ladders;  // bottom → top (goes UP)

int getFinalPosition(int pos) {
    if (snakes.containsKey(pos))  return snakes.get(pos);
    if (ladders.containsKey(pos)) return ladders.get(pos);
    return pos;
}
```

### Game Loop
```
1. Dequeue next player
2. Roll dice
3. Calculate new position
4. If > 100: stay (can't exceed board)
5. Apply snake/ladder transform
6. If == 100: WIN!
7. Enqueue player back
```

### Dice — Configurable
Single die (1-6), or multiple dice, or loaded dice — just change `Dice.roll()`.

📁 `src/snakeladder/`

