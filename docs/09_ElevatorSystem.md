# Problem 9: Elevator System — Complete Guide

---

## Part 1: Understanding the Problem

### What are we building?
A system that manages multiple elevators in a building:
- Users press a button on a floor (external request)
- Users press a floor button inside the elevator (internal request)
- System dispatches the best elevator
- Elevator services stops efficiently using the **SCAN algorithm**

### Requirements
- ✓ Multiple elevators managed by a central controller
- ✓ Dispatch requests to the best elevator (nearest, correct direction)
- ✓ SCAN algorithm: service all stops in one direction, then reverse
- ✓ Thread-safe — multiple requests can come simultaneously

---

## Part 2: The Key Insight — SCAN Algorithm (Disk Scheduling)

### Real World Analogy
Think of a real elevator: it doesn't jump back and forth. If it's going UP and people on floors 3, 7, 5 are waiting, it stops at 3, then 5, then 7 (in order going up). It only reverses when there are no more stops upward.

This is the same as the **SCAN disk scheduling algorithm** — the read head sweeps in one direction, then reverses.

### How SCAN Works
```
Elevator at Floor 2, going UP
Requests: Floor 5, Floor 8, Floor 3 (down), Floor 1 (down)

Step 1: upStops = {5, 8}     downStops = {3, 1}
Step 2: Go UP → Floor 5 ✓   upStops = {8}
Step 3: Go UP → Floor 8 ✓   upStops = {}
Step 4: No more up → REVERSE to DOWN
Step 5: Go DOWN → Floor 3 ✓ downStops = {1}
Step 6: Go DOWN → Floor 1 ✓ downStops = {}
Step 7: No more down → IDLE
```

### Data Structure: Two TreeSets
```java
TreeSet<Integer> upStops;    // sorted ascending — take first() for nearest up
TreeSet<Integer> downStops;  // sorted ascending — take last() for nearest down
```

**Why TreeSet?**
- Auto-sorted → always know the nearest stop in O(log n)
- `upStops.first()` → nearest floor above
- `downStops.last()` → nearest floor below
- No duplicates (multiple people on same floor = one stop)

---

## Part 3: The Code — Explained

### Direction Enum
```java
public enum Direction { UP, DOWN, IDLE }
```

### Request — Two Types
```java
public class Request {
    private final int fromFloor;
    private final int toFloor;
    private final Direction direction;

    // Internal request: user inside elevator presses "Floor 7"
    public Request(int fromFloor, int toFloor) { ... }

    // External request: user on Floor 3 presses UP button
    public Request(int floor, Direction direction) { this.toFloor = -1; }
}
```

### Elevator — The SCAN Engine
```java
public class Elevator {
    private int currentFloor;
    private Direction direction;
    private final TreeSet<Integer> upStops;
    private final TreeSet<Integer> downStops;

    public synchronized void addRequest(Request request) {
        int destination = request.getToFloor() != -1
                ? request.getToFloor() : request.getFromFloor();

        // Classify stop by direction relative to current position
        if (destination > currentFloor) upStops.add(destination);
        else if (destination < currentFloor) downStops.add(destination);

        // Also add pickup floor for external requests
        if (request.getFromFloor() != currentFloor) {
            if (request.getFromFloor() > currentFloor)
                upStops.add(request.getFromFloor());
            else
                downStops.add(request.getFromFloor());
        }

        if (direction == Direction.IDLE)
            direction = destination >= currentFloor ? Direction.UP : Direction.DOWN;
    }

    public synchronized void step() {
        if (direction == Direction.UP) {
            if (!upStops.isEmpty()) {
                moveTo(upStops.first());   // nearest floor above
                upStops.remove(upStops.first());
            } else if (!downStops.isEmpty()) {
                direction = Direction.DOWN; // reverse!
                step();
            } else {
                direction = Direction.IDLE;
            }
        } else if (direction == Direction.DOWN) {
            if (!downStops.isEmpty()) {
                moveTo(downStops.last());  // nearest floor below
                downStops.remove(downStops.last());
            } else if (!upStops.isEmpty()) {
                direction = Direction.UP;  // reverse!
                step();
            } else {
                direction = Direction.IDLE;
            }
        }
    }
}
```

### ElevatorController — Dispatcher
```java
public class ElevatorController {
    private final List<Elevator> elevators;

    public void handleRequest(Request request) {
        Elevator best = findBestElevator(request);
        best.addRequest(request);
    }

    private int calculateScore(Elevator e, Request request) {
        int distance = Math.abs(e.getCurrentFloor() - request.getFromFloor());

        if (e.isIdle()) return distance;                // idle = just distance

        boolean movingTowards =
            (e.getDirection() == Direction.UP && request.getFromFloor() >= e.getCurrentFloor()) ||
            (e.getDirection() == Direction.DOWN && request.getFromFloor() <= e.getCurrentFloor());

        if (movingTowards) return distance;             // on the way = good
        return distance + 1000;                          // going away = big penalty
    }
}
```

**Scoring: lower = better**

| Scenario | Score | Why |
|---|---|---|
| Idle elevator, 3 floors away | 3 | Just needs to travel there |
| Going UP, request is above | distance | Will pass by anyway |
| Going UP, request is below | distance + 1000 | Has to finish up, reverse, then go |

---

## Part 4: Data Flow — Processing a Request

```
1. User at Floor 3 presses UP to go to Floor 7
   → Request(fromFloor=3, toFloor=7)

2. Controller scores all elevators:
   Elevator-1: Floor 0, IDLE → score = |0-3| = 3
   Elevator-2: Floor 5, DOWN → score = |5-3| + 1000 = 1002 (going away!)
   → Dispatch to Elevator-1 (score 3)

3. Elevator-1.addRequest():
   Floor 3 > currentFloor(0) → upStops.add(3)
   Floor 7 > currentFloor(0) → upStops.add(7)
   direction = UP

4. step() → moveTo(3)  [upStops.first()]
   step() → moveTo(7)  [upStops.first()]
   step() → no more stops → IDLE
```

---

## Part 5: Follow-Up Questions

| Question | Answer |
|---|---|
| What about door open/close timing? | Add `DOOR_OPEN` state. Elevator waits N seconds at each stop. |
| VIP/priority floors? | Modify scoring: VIP floors get score bonus. Or use a separate priority queue. |
| Weight limit? | Track current load in elevator. Reject if exceeding capacity. |
| Fire mode? | All elevators return to ground floor. Override all requests. Special state. |
| How to handle 100+ floors? | Zone-based: elevators 1-4 serve floors 1-25, elevators 5-8 serve 26-50, etc. |

---

## Part 6: Patterns Recap

| Pattern | Where & Why |
|---|---|
| **Strategy** | Dispatch algorithm — scoring heuristic is swappable |
| **SCAN Algorithm** | Elevator services stops in sweep order — efficient, fair |
| **Observer** | Floor displays could observe elevator position |
| **SRP** | Elevator handles movement, Controller handles dispatch |
| **Thread Safety** | `synchronized` on addRequest and step |

---

📁 **Source code:** `src/elevator/`
