# Live Sports Scoreboard — Complete Guide

---

## Part 1: Understanding the Problem

A real-time scoreboard where thousands of fans subscribe to match updates. The system is **read-heavy** — millions read the score, only a few update it.

### Requirements
- ✓ Create/manage live matches with scores and events
- ✓ Observer Pattern: fans subscribe and get push notifications
- ✓ Thread-safe: concurrent reads (fans) + writes (score updates)
- ✓ Read-heavy optimization using CopyOnWriteArrayList and ConcurrentHashMap

---

## Part 2: Key Design — Read-Heavy Optimization

### The Problem
- **Writers:** 1 admin updating scores (rare — a goal every 10 min)
- **Readers:** 1 million fans checking scores (constant)

### Data Structures Chosen

| Structure | Why |
|---|---|
| `CopyOnWriteArrayList` for observers | Reads (iterate to notify) are frequent. Writes (subscribe/unsubscribe) are rare. COW copies array on write, so reads never block. |
| `ConcurrentHashMap` for match lookup | O(1) thread-safe reads without locking. |
| `synchronized` on score updates | Only writes need mutual exclusion. Reads are lock-free. |

---

## Part 3: The Code

### Observer Interface
```java
interface ScoreObserver {
    void onScoreUpdate(Match match, String event);
}

class Fan implements ScoreObserver {
    void onScoreUpdate(Match match, String event) {
        // push notification to fan's device
    }
}
```

### Match — Observable Subject
```java
class Match {
    private final List<ScoreObserver> observers = new CopyOnWriteArrayList<>();

    public synchronized void goal(String team, String scorer, int minute) {
        if (team.equals(teamA)) scoreA++;
        else scoreB++;
        String event = "⚽ GOAL! " + scorer + " " + minute + "'";
        notifyObservers(event);
    }

    private void notifyObservers(String event) {
        for (ScoreObserver obs : observers) obs.onScoreUpdate(this, event);
    }
}
```

---

## Part 4: Follow-Up Questions

| Question | Answer |
|---|---|
| Scale to millions of fans? | Fan-out via message queues (Kafka). Each notification worker processes a partition of fans. |
| Multiple sports? | Abstract `Match` → `CricketMatch`, `FootballMatch`, `BasketballMatch` with sport-specific events. |
| Live commentary? | Add `Commentary` observer that builds text stream of events. |
| WebSocket push? | Each `Fan` holds a WebSocket connection. `onScoreUpdate` pushes JSON through it. |

---

📁 **Source code:** `src/scoreboard/`

