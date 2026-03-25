# Problem 9: Elevator System

| Pattern | Why |
|---|---|
| **Strategy** | Dispatch algorithm (nearest idle, directional preference) |
| **SCAN Algorithm** | Like disk scheduling — service floors in one direction, then reverse |
| **Observer** | Controller dispatches to elevators |

## Key Design
- **Elevator**: Maintains two `TreeSet<Integer>` — `upStops` and `downStops` (sorted)
- **ElevatorController**: Manages N elevators, dispatches requests via scoring heuristic
- **SCAN**: Go UP servicing all stops, then reverse DOWN — minimizes direction changes

## Scoring Heuristic (Dispatch)
```
Idle elevator      → score = distance to request floor (best)
Moving TOWARDS     → score = distance (good — on the way)
Moving AWAY        → score = distance + 1000 (penalty)
Lowest score wins.
```

## Core Trick: TreeSet for Sorted Stops
- `upStops.first()` → nearest stop above (O(log n))
- `downStops.last()` → nearest stop below (O(log n))
- Auto-sorted, no manual ordering needed

📁 `src/elevator/`

