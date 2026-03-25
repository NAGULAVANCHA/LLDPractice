# LLD Interview Practice — Complete Guide

> 20 Low-Level Design problems **ranked by 2026 interview frequency**, with Java implementations, detailed guides, and design pattern explanations.
>
> ⭐ = Most frequently asked &nbsp; | &nbsp; 🆕 = Newly added

---

## 🚀 How to Run

```bash
# Compile everything
cd src
javac Main.java

# Run the interactive menu (pick any of the 20 demos)
java Main

# Or run a specific problem directly
java -cp . parkinglot.ParkingLotDemo
java -cp . ridesharing.RideSharingDemo
java -cp . taskscheduler.TaskSchedulerDemo
```

---

## 📚 All 20 Problems — Ranked by 2026 Interview Importance

| Rank | Problem | Key Patterns | Guide | Source |
|---|---|---|---|---|
| ⭐1 | **LRU Cache** | HashMap + Doubly Linked List | [Guide](docs/01_LRUCache.md) | `src/lrucache/` |
| ⭐2 | **Rate Limiter** | Strategy (4 algorithms) | [Guide](docs/02_RateLimiter.md) | `src/ratelimiter/` |
| ⭐3 | **Parking Lot** | Singleton, Strategy | [Guide](docs/03_ParkingLot.md) | `src/parkinglot/` |
| 🆕4 | **Ride-Sharing (Uber/Ola)** | Strategy, State, Observer | [Guide](docs/04_RideSharing.md) | `src/ridesharing/` |
| 🆕5 | **Task Scheduler / Thread Pool** | Producer-Consumer, Concurrency | [Guide](docs/05_TaskScheduler.md) | `src/taskscheduler/` |
| ⭐6 | **Pub-Sub Message Queue** | Observer, Producer-Consumer | [Guide](docs/06_PubSubQueue.md) | `src/pubsub/` |
| 7 | **Vending Machine** | State Pattern | [Guide](docs/07_VendingMachine.md) | `src/vendingmachine/` |
| 8 | **BookMyShow** | Concurrency, Atomic Booking | [Guide](docs/08_BookMyShow.md) | `src/bookmyshow/` |
| 9 | **Notification System** | Observer, Strategy | [Guide](docs/09_NotificationSystem.md) | `src/notification/` |
| 10 | **Splitwise** | Strategy, Enum | [Guide](docs/10_Splitwise.md) | `src/splitwise/` |
| 11 | **LFU Cache** | 3 HashMaps + minFreq | [Guide](docs/11_LFUCache.md) | `src/lfucache/` |
| 12 | **Elevator System** | Strategy, SCAN Algorithm | [Guide](docs/12_ElevatorSystem.md) | `src/elevator/` |
| 13 | **Logging Framework** | Singleton, Chain of Responsibility | [Guide](docs/13_LoggingFramework.md) | `src/logger/` |
| 14 | **ATM Machine** | State, Chain of Responsibility | [Guide](docs/14_ATMMachine.md) | `src/atm/` |
| 15 | **Hotel Booking** | Concurrency, Date Ranges | [Guide](docs/15_HotelBooking.md) | `src/hotel/` |
| 16 | **Shopping Cart** | Strategy (Pricing/Discounts) | [Guide](docs/16_ShoppingCart.md) | `src/shoppingcart/` |
| 17 | **Figma (Design Tool)** | Command, Composite, Observer, Prototype | [Guide](docs/17_FigmaDesignTool.md) | `src/figma/` |
| 18 | **File System** | Composite, Recursion | [Guide](docs/18_FileSystem.md) | `src/filesystem/` |
| 19 | **Tic-Tac-Toe** | O(1) Win Detection | [Guide](docs/19_TicTacToe.md) | `src/tictactoe/` |
| 20 | **Snake & Ladder** | OOP, Game Loop, Queue | [Guide](docs/20_SnakeLadder.md) | `src/snakeladder/` |

### Why This Ranking?

| Tier | Problems | Why |
|---|---|---|
| **Tier 1 — Must Know** (1-6) | LRU Cache, Rate Limiter, Parking Lot, Uber, Thread Pool, Pub-Sub | Asked at Google, Amazon, Meta, startups. System design crossover. Concurrency is king in 2026. |
| **Tier 2 — Frequently Asked** (7-12) | Vending Machine, BookMyShow, Notifications, Splitwise, LFU, Elevator | Classic LLD staples. State Pattern and Observer are tested heavily. |
| **Tier 3 — Good to Know** (13-18) | Logger, ATM, Hotel, Shopping Cart, Figma, File System | Less frequent but demonstrate specific patterns (CoR, Command, Composite). |
| **Tier 4 — Basics** (19-20) | Tic-Tac-Toe, Snake & Ladder | OOP warm-up. Rarely asked at senior level, but great for learning. |

---

## 🎯 The 5-Step Framework for ANY LLD Problem

1. **Clarify Requirements** (2-3 min) — Ask questions, narrow scope
2. **Identify Entities (Nouns)** (2-3 min) — These become classes
3. **Identify Behaviors (Verbs)** (2 min) — These become methods
4. **Identify Relationships** (2 min) — HAS-A, IS-A, uses
5. **Code Bottom-Up** (15-20 min) — Smallest classes first

---

## 🧩 Design Patterns Cheat Sheet

| Pattern | When to Use | Problems That Use It |
|---|---|---|
| **Singleton** | Only ONE instance needed | Parking Lot, Logger |
| **Strategy** | Multiple interchangeable algorithms | Parking Lot, Rate Limiter, Uber, Splitwise, Shopping Cart, Notification |
| **State** | Object behavior changes with state | Vending Machine, ATM, Uber (Driver states) |
| **Observer** | Notify multiple objects of changes | Notification System, Uber, Figma, Pub-Sub |
| **Command** | Undo/redo, operation history | Figma |
| **Composite** | Tree structures, part-whole hierarchies | Figma (groups), File System |
| **Chain of Responsibility** | Pass request through handler chain | Logger, ATM (cash dispenser) |
| **Producer-Consumer** | Decouple task submission from execution | Thread Pool, Pub-Sub |
| **Prototype** | Clone existing objects | Figma (duplicate shapes) |

---

## 📖 SOLID Principles Quick Reference

| Principle | Meaning | Example in This Repo |
|---|---|---|
| **S** — Single Responsibility | One class, one job | ParkingSpot only manages parking, not billing |
| **O** — Open-Closed | Open for extension, closed for modification | Add new MatchingStrategy without changing RideService |
| **L** — Liskov Substitution | Subtypes must be substitutable | Any Shape (Rectangle, Circle) works wherever Shape is expected |
| **I** — Interface Segregation | Don't force unused methods | NotificationChannel has only send() — not a bloated interface |
| **D** — Dependency Inversion | Depend on abstractions, not concretions | ParkingLot depends on ParkingStrategy interface, not NearestSpotStrategy |
