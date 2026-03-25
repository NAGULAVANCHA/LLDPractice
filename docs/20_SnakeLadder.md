# Problem 12: Snake & Ladder — Complete Guide

---

## Part 1: Understanding the Problem

A board game where players roll dice and move forward. Snakes send you down, ladders take you up. First to reach the final square wins.

### Requirements
- ✓ Configurable board size, snakes, and ladders
- ✓ Multiple players, turn-based (round-robin queue)
- ✓ Configurable dice (number of dice, faces)
- ✓ Can't exceed the final square (stay if roll is too high)

---

## Part 2: The Code — Explained

### Board — Position Transforms via HashMap
```java
public class SnakeLadderBoard {
    private final int size;
    private final Map<Integer, Integer> snakes;   // head → tail (DOWN)
    private final Map<Integer, Integer> ladders;  // bottom → top (UP)

    public int getFinalPosition(int position) {
        if (snakes.containsKey(position)) {
            System.out.println("🐍 Snake! " + position + " → " + snakes.get(position));
            return snakes.get(position);
        }
        if (ladders.containsKey(position)) {
            System.out.println("🪜 Ladder! " + position + " → " + ladders.get(position));
            return ladders.get(position);
        }
        return position;  // no snake or ladder
    }
}
```

**Why HashMap?** O(1) lookup. On every move, just check: "Is there a snake/ladder at this position?"

### Dice — Configurable
```java
public class Dice {
    private final int numDice;
    private final int faces;

    public int roll() {
        int total = 0;
        for (int i = 0; i < numDice; i++)
            total += random.nextInt(faces) + 1;
        return total;
    }
}
```

### Game — Turn-Based via Queue
```java
public class SnakeLadderGame {
    private final Queue<SnakeLadderPlayer> players;  // round-robin turns

    public boolean playTurn() {
        SnakeLadderPlayer player = players.poll();  // dequeue current player
        int roll = dice.roll();
        int newPos = player.getPosition() + roll;

        if (newPos > board.getWinPosition()) {
            // Can't exceed board — stay put
            players.offer(player);
            return false;
        }

        newPos = board.getFinalPosition(newPos);  // apply snake/ladder
        player.setPosition(newPos);

        if (newPos == board.getWinPosition()) {
            winner = player;
            return true;  // game over!
        }

        players.offer(player);  // enqueue back for next turn
        return false;
    }
}
```

**Why Queue?** `poll()` takes the front player, `offer()` puts them back at the end. Perfect round-robin.

---

## Part 3: Data Flow — A Turn

```
Players queue: [Alice(pos=5), Bob(pos=12)]

Alice's turn:
  1. poll() → Alice
  2. roll() → 6
  3. newPos = 5 + 6 = 11
  4. getFinalPosition(11) → ladder at 11 → 26!
  5. Alice.position = 26
  6. 26 ≠ 30 (win position) → game continues
  7. offer(Alice) → queue: [Bob(12), Alice(26)]

Bob's turn:
  1. poll() → Bob
  2. roll() → 3
  3. newPos = 12 + 3 = 15
  4. getFinalPosition(15) → no snake/ladder → 15
  5. Bob.position = 15
  6. offer(Bob) → queue: [Alice(26), Bob(15)]
```

---

## Part 4: Follow-Up Questions

| Question | Answer |
|---|---|
| What if snake head = ladder bottom? | Validate during setup: no position should have both. |
| Chained snakes/ladders? | Currently one transform per move. Could chain by calling `getFinalPosition` recursively until stable. |
| Special rules (extra turn on 6)? | After rolling, if `roll == 6`, don't `offer()` the player back — they go again. |
| Multiple dice? | Already supported: `Dice(2, 6)` rolls 2d6 (range 2-12). |

---

## Part 5: Patterns Recap

| Pattern | Where & Why |
|---|---|
| **Composition** | Board HAS snakes and ladders (Maps) |
| **Queue** | Round-robin turn management |
| **SRP** | Dice rolls, Board transforms, Game manages flow |
| **Configurable** | Board size, dice, snakes/ladders all parameterized |

---

📁 **Source code:** `src/snakeladder/`
