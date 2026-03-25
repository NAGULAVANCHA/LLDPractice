# LLD Interview Practice — Complete Guide

> A comprehensive collection of Low-Level Design problems with detailed explanations, design patterns, and Java implementations.

---

## 📚 Problems & Guides

| # | Problem | Key Patterns | Guide | Source |
|---|---|---|---|---|
| 1 | **Parking Lot** | Singleton, Strategy | [Guide](docs/01_ParkingLot.md) | `src/parkinglot/` |
| 2 | **Vending Machine** | State | [Guide](docs/02_VendingMachine.md) | `src/vendingmachine/` |
| 3 | **LRU Cache** | HashMap + DLL | [Guide](docs/03_LRUCache.md) | `src/lrucache/` |
| 4 | **LFU Cache** | 3 HashMaps + minFreq | [Guide](docs/04_LFUCache.md) | `src/lfucache/` |
| 5 | **Notification System** | Observer, Strategy | [Guide](docs/05_NotificationSystem.md) | `src/notification/` |
| 6 | **Splitwise** | Strategy, Enum | [Guide](docs/06_Splitwise.md) | `src/splitwise/` |
| 7 | **Figma (Design Tool)** | Command, Composite, Observer, Prototype | [Guide](docs/07_FigmaDesignTool.md) | `src/figma/` |
| 8 | **Rate Limiter** | Strategy | [Guide](docs/08_RateLimiter.md) | `src/ratelimiter/` |

---

## 🏗️ Also Implemented (code in `src/`)

| Problem | Source |
|---|---|
| Elevator System | `src/elevator/` |
| Chess Game | `src/chess/` |
| Tic Tac Toe | `src/tictactoe/` |
| Snake & Ladder | `src/snakeladder/` |
| Hotel Reservation | `src/hotel/` |
| BookMyShow | `src/bookmyshow/` |
| Library Management | `src/library/` |
| Shopping Cart | `src/shoppingcart/` |
| ATM Machine | `src/atm/` |
| File System | `src/filesystem/` |
| Pub-Sub System | `src/pubsub/` |
| Logger | `src/logger/` |

---

## 🎯 The 5-Step Framework for ANY LLD Problem

1. **Clarify Requirements** (2-3 min) — Ask questions, narrow scope
2. **Identify Entities (Nouns)** (2-3 min) — These become classes
3. **Identify Behaviors (Verbs)** (2 min) — These become methods
4. **Identify Relationships** (2 min) — HAS-A, IS-A, uses
5. **Code Bottom-Up** (15-20 min) — Smallest classes first

---

## 🧩 Design Patterns Cheat Sheet

| Pattern | When to Use | LLD Problems That Use It |
|---|---|---|
| **Singleton** | Only ONE instance needed | Parking Lot, Logger |
| **Strategy** | Multiple interchangeable algorithms | Parking Lot (spot-finding), Rate Limiter, Splitwise (split types), Notification (channels) |
| **State** | Object behavior changes with state | Vending Machine, ATM, Elevator |
| **Observer** | Notify multiple objects of changes | Notification System, Figma (collaboration), Pub-Sub |
| **Command** | Undo/redo, operation history | Figma (undo/redo) |
| **Composite** | Tree structures, part-whole hierarchies | Figma (shape groups), File System |
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

