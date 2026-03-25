# LLD Interview Practice — Complete Guide

> **18 Low-Level Design problems** — zero filler, every problem teaches a unique pattern. Ranked by 2026 interview frequency.

---

## 🚀 How to Run

```bash
cd src
javac Main.java
java Main
```

---

## 📚 Tier 1 — System Design Crossovers (Must Know)

> These bridge LLD and system design. Asked at Google, Amazon, Meta, Flipkart, and every startup.

| # | Problem | Key Patterns | Guide | Source |
|---|---|---|---|---|
| 1 | **Rate Limiter** | Strategy (Token Bucket, Sliding Window, Leaky Bucket, Fixed Window) | [Guide](docs/01_RateLimiter.md) | `src/ratelimiter/` |
| 2 | **Payment Gateway / UPI Switch** | Strategy, State, Chain of Responsibility | [Guide](docs/02_PaymentGateway.md) | `src/paymentgateway/` |
| 3 | **Ride-Sharing (Uber/Ola)** | Strategy, State, Observer | [Guide](docs/03_RideSharing.md) | `src/ridesharing/` |
| 4 | **Task Scheduler / Thread Pool** | Producer-Consumer, BlockingQueues, Concurrency | [Guide](docs/04_TaskScheduler.md) | `src/taskscheduler/` |
| 5 | **Pub-Sub Message Queue** | Observer, Producer-Consumer, Offset Tracking | [Guide](docs/05_PubSubQueue.md) | `src/pubsub/` |
| 6 | **BookMyShow** | Concurrency, Atomic Transactions, Seat Locking | [Guide](docs/06_BookMyShow.md) | `src/bookmyshow/` |
| 7 | **LRU Cache** | HashMap + Doubly Linked List | [Guide](docs/07_LRUCache.md) | `src/lrucache/` |
| 8 | **LFU Cache** | 3 HashMaps + MinFreq Tracking | [Guide](docs/08_LFUCache.md) | `src/lfucache/` |
| 9 | **Splitwise** | Strategy (Equal/Exact/Percentage), Debt Simplification | [Guide](docs/09_Splitwise.md) | `src/splitwise/` |

## 📚 Tier 2 — Advanced OOP & Modern Architecture

> Tests structural/behavioral patterns, AI-era systems, and media streaming.

| # | Problem | Key Patterns | Guide | Source |
|---|---|---|---|---|
| 10 | **In-Memory Vector Database** | Strategy (Cosine/Euclidean), KNN with Min-Heap | [Guide](docs/10_VectorDatabase.md) | `src/vectordb/` |
| 11 | **LLM Prompt Orchestrator** | Chain of Responsibility, Factory | [Guide](docs/11_LLMOrchestrator.md) | `src/llmorchestrator/` |
| 12 | **Video Buffer / Segment Manager** | Sliding Window, TreeMap, Eviction Policy | [Guide](docs/12_VideoBuffer.md) | `src/videobuffer/` |
| 13 | **Logging Framework** | Singleton, Chain of Responsibility, Template Method | [Guide](docs/13_LoggingFramework.md) | `src/logger/` |
| 14 | **Elevator System** | Strategy, SCAN Algorithm, State | [Guide](docs/14_ElevatorSystem.md) | `src/elevator/` |
| 15 | **Vending Machine** | State Pattern, Transition Logic | [Guide](docs/15_VendingMachine.md) | `src/vendingmachine/` |

## 📚 Tier 3 — Foundational Classics

> Baseline expectations. If you can't do these, nothing else matters.

| # | Problem | Key Patterns | Guide | Source |
|---|---|---|---|---|
| 16 | **Notification System** | Observer, Strategy (SMS/Email/Push) | [Guide](docs/16_NotificationSystem.md) | `src/notification/` |
| 17 | **Parking Lot** | Singleton, Strategy (Spot Allocation) | [Guide](docs/17_ParkingLot.md) | `src/parkinglot/` |
| 18 | **File System** | Composite, Recursion | [Guide](docs/18_FileSystem.md) | `src/filesystem/` |

---

## ❌ What Was Cut & Why

| Dropped | Reason |
|---|---|
| **Live Scoreboard** | Just Observer with `CopyOnWriteArrayList` — Notification System already covers Observer. The "read-heavy" angle is a system design talking point, not an LLD coding question. |
| **Cloud Resource Allocator** | Too niche. Nobody asks "design a cloud allocator." It's just Strategy + a health check loop — both already covered by Rate Limiter and Uber. |
| **Shopping Cart** | Strategy for pricing = same pattern as Splitwise. A cart is just CRUD + discount function — too simple for a standalone LLD problem. |
| **ATM Machine** | State + CoR already covered by Vending Machine + Payment Gateway. ATM is the less interesting version of both. |
| **Hotel Booking** | Date overlap check is interesting but niche. BookMyShow covers the concurrency/booking problem better. |
| **Tic-Tac-Toe** | O(1) win detection is a DSA trick, not an LLD problem. |
| **Snake & Ladder** | Pure OOP warm-up. Not asked at experienced level. |
| **Chess** | Move validation is interesting OOP but rarely asked as LLD. |
| **Library Management** | CRUD with no interesting patterns. |
| **Figma (Design Tool)** | Command + Composite is elegant but almost never asked. |

---

## 🧩 Design Patterns — Every Pattern Is Covered

| Pattern | Unique Problem That Teaches It | Others |
|---|---|---|
| **Strategy** | Rate Limiter (4 algorithms!) | Uber, Payment Gateway, Vector DB, Splitwise |
| **State** | Vending Machine (textbook example) | Uber (Driver), Payment (Transaction) |
| **Observer** | Notification System | Pub-Sub, Uber |
| **Chain of Responsibility** | Payment Gateway (fraud chain) | LLM Orchestrator, Logger |
| **Singleton** | Logger | Parking Lot |
| **Producer-Consumer** | Task Scheduler (BlockingQueue) | Pub-Sub |
| **Composite** | File System (the classic example) | — |
| **Factory** | LLM Orchestrator (provider swap) | — |
| **Template Method** | Logger (handle → write) | — |

**Every pattern appears at least once. No problem is just a copy of another.**
