# LLD Interview Practice — Complete Guide

> 20 Low-Level Design problems with Java implementations, detailed guides, and design pattern explanations.

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
java -cp . vendingmachine.VendingMachineDemo
java -cp . ratelimiter.RateLimiterDemo
```

---

## 📚 All 20 Problems with Guides

| # | Problem | Key Patterns | Guide | Source |
|---|---|---|---|---|
| 1 | **Parking Lot** | Singleton, Strategy | [Guide](docs/01_ParkingLot.md) | `src/parkinglot/` |
| 2 | **Vending Machine** | State | [Guide](docs/02_VendingMachine.md) | `src/vendingmachine/` |
| 3 | **LRU Cache** | HashMap + DLL | [Guide](docs/03_LRUCache.md) | `src/lrucache/` |
| 4 | **LFU Cache** | 3 HashMaps + minFreq | [Guide](docs/04_LFUCache.md) | `src/lfucache/` |
| 5 | **Notification System** | Observer, Strategy | [Guide](docs/05_NotificationSystem.md) | `src/notification/` |
| 6 | **Splitwise** | Strategy, Enum | [Guide](docs/06_Splitwise.md) | `src/splitwise/` |
| 7 | **Figma (Design Tool)** | Command, Composite, Observer, Prototype | [Guide](docs/07_FigmaDesignTool.md) | `src/figma/` |
| 8 | **Rate Limiter** | Strategy (4 algorithms) | [Guide](docs/08_RateLimiter.md) | `src/ratelimiter/` |
| 9 | **Elevator System** | Strategy, SCAN Algorithm | [Guide](docs/09_ElevatorSystem.md) | `src/elevator/` |
| 10 | **Tic-Tac-Toe** | O(1) Win Detection | [Guide](docs/10_TicTacToe.md) | `src/tictactoe/` |
| 11 | **BookMyShow** | Concurrency, Atomic Booking | [Guide](docs/11_BookMyShow.md) | `src/bookmyshow/` |
| 12 | **Snake & Ladder** | OOP, Game Loop, Queue | [Guide](docs/12_SnakeLadder.md) | `src/snakeladder/` |
| 13 | **Library Management** | CRUD, Search, Fines | [Guide](docs/13_LibraryManagement.md) | `src/library/` |
| 14 | **Shopping Cart** | Strategy (Pricing/Discounts) | [Guide](docs/14_ShoppingCart.md) | `src/shoppingcart/` |
| 15 | **Chess** | Polymorphism, Piece Hierarchy | [Guide](docs/15_Chess.md) | `src/chess/` |
| 16 | **Logging Framework** | Singleton, Chain of Responsibility | [Guide](docs/16_LoggingFramework.md) | `src/logger/` |
| 17 | **Pub-Sub Queue** | Observer, Producer-Consumer | [Guide](docs/17_PubSubQueue.md) | `src/pubsub/` |
| 18 | **ATM Machine** | State, Chain of Responsibility | [Guide](docs/18_ATMMachine.md) | `src/atm/` |
| 19 | **Hotel Booking** | Concurrency, Date Ranges | [Guide](docs/19_HotelBooking.md) | `src/hotel/` |
| 20 | **File System** | Composite, Recursion | [Guide](docs/20_FileSystem.md) | `src/filesystem/` |

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
| **Strategy** | Multiple interchangeable algorithms | Parking Lot, Rate Limiter, Splitwise, Shopping Cart, Notification |
| **State** | Object behavior changes with state | Vending Machine, ATM, Elevator |
| **Observer** | Notify multiple objects of changes | Notification System, Figma, Pub-Sub |
| **Command** | Undo/redo, operation history | Figma |
| **Composite** | Tree structures, part-whole hierarchies | Figma (groups), File System, Chess (board) |
| **Chain of Responsibility** | Pass request through handler chain | Logger, ATM (cash dispenser) |
| **Factory** | Object creation without specifying exact class | Vehicle creation, Shape creation |
| **Prototype** | Clone existing objects | Figma (duplicate shapes) |

---

## 📖 SOLID Principles Quick Reference

| Principle | Meaning | Example in This Repo |
|---|---|---|
| **S** — Single Responsibility | One class, one job | ParkingSpot only manages parking, not billing |
| **O** — Open-Closed | Open for extension, closed for modification | Add new ParkingStrategy without changing ParkingLot |
| **L** — Liskov Substitution | Subtypes must be substitutable | Any Shape (Rectangle, Circle) works wherever Shape is expected |
| **I** — Interface Segregation | Don't force unused methods | NotificationChannel has only send() — not a bloated interface |
| **D** — Dependency Inversion | Depend on abstractions, not concretions | ParkingLot depends on ParkingStrategy interface, not NearestSpotStrategy |
