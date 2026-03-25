# LLD Interview Practice — Complete Guide

> **21 Low-Level Design problems** ranked by 2026 interview frequency, with Java implementations, detailed guides, and design pattern explanations.

---

## 🚀 How to Run

```bash
cd src
javac Main.java
java Main
```

---

## 📚 Tier 1 — System Design Crossovers (Must Know)

> Asked at Google, Amazon, Meta, and top startups. These bridge LLD and system design.

| # | Problem | Key Patterns | Guide | Source |
|---|---|---|---|---|
| 1 | **Rate Limiter** | Strategy (Token Bucket, Sliding Window) | [Guide](docs/01_RateLimiter.md) | `src/ratelimiter/` |
| 2 | **Payment Gateway / UPI Switch** | Strategy, State, Chain of Responsibility | [Guide](docs/02_PaymentGateway.md) | `src/paymentgateway/` |
| 3 | **Ride-Sharing (Uber/Ola)** | Strategy, State, Observer | [Guide](docs/03_RideSharing.md) | `src/ridesharing/` |
| 4 | **Live Sports Scoreboard** | Observer, Concurrency, Read-Heavy | [Guide](docs/04_LiveScoreboard.md) | `src/scoreboard/` |
| 5 | **Task Scheduler / Thread Pool** | Producer-Consumer, BlockingQueues | [Guide](docs/05_TaskScheduler.md) | `src/taskscheduler/` |
| 6 | **Pub-Sub Message Queue** | Observer, Producer-Consumer | [Guide](docs/06_PubSubQueue.md) | `src/pubsub/` |
| 7 | **BookMyShow** | Concurrency, Atomic Transactions | [Guide](docs/07_BookMyShow.md) | `src/bookmyshow/` |
| 8 | **Cloud Resource Allocator** | Strategy, Health Check State | [Guide](docs/08_CloudAllocator.md) | `src/cloudresource/` |
| 9 | **LRU Cache** | HashMap + Doubly Linked List | [Guide](docs/09_LRUCache.md) | `src/lrucache/` |
| 10 | **LFU Cache** | 3 HashMaps + MinFreq Tracking | [Guide](docs/10_LFUCache.md) | `src/lfucache/` |
| 11 | **Splitwise** | Strategy, Graph/Math Algorithms | [Guide](docs/11_Splitwise.md) | `src/splitwise/` |

## 📚 Tier 2 — Advanced OOP & Modern Architecture

> Tests structural/behavioral patterns, AI pipelines, and media streaming.

| # | Problem | Key Patterns | Guide | Source |
|---|---|---|---|---|
| 12 | **In-Memory Vector Database** | Strategy (Cosine/Euclidean), KNN Search | [Guide](docs/12_VectorDatabase.md) | `src/vectordb/` |
| 13 | **LLM Prompt Orchestrator** | Chain of Responsibility, Factory | [Guide](docs/13_LLMOrchestrator.md) | `src/llmorchestrator/` |
| 14 | **Video Buffer / Segment Manager** | Sliding Window, TreeMap, Eviction | [Guide](docs/14_VideoBuffer.md) | `src/videobuffer/` |
| 15 | **Logging Framework** | Singleton, Chain of Responsibility | [Guide](docs/15_LoggingFramework.md) | `src/logger/` |
| 16 | **Elevator System** | Strategy, SCAN Algorithm, State | [Guide](docs/16_ElevatorSystem.md) | `src/elevator/` |
| 17 | **Vending Machine** | State Pattern, Transition Logic | [Guide](docs/17_VendingMachine.md) | `src/vendingmachine/` |

## 📚 Tier 3 — Foundational Classics

> Baseline expectations. Fundamental class hierarchies and standard patterns.

| # | Problem | Key Patterns | Guide | Source |
|---|---|---|---|---|
| 18 | **Notification System** | Observer, Strategy (SMS/Email/Push) | [Guide](docs/18_NotificationSystem.md) | `src/notification/` |
| 19 | **Parking Lot** | Singleton, Strategy (Spot Allocation) | [Guide](docs/19_ParkingLot.md) | `src/parkinglot/` |
| 20 | **File System** | Composite, Recursion | [Guide](docs/20_FileSystem.md) | `src/filesystem/` |
| 21 | **Shopping Cart** | Strategy (Pricing), Decorator | [Guide](docs/21_ShoppingCart.md) | `src/shoppingcart/` |

---

## 🧩 Design Patterns Cheat Sheet

| Pattern | When to Use | Problems Using It |
|---|---|---|
| **Strategy** | Multiple interchangeable algorithms | Rate Limiter, Payment Gateway, Uber, Cloud Allocator, Vector DB, Splitwise, Shopping Cart |
| **State** | Behavior changes with internal state | Vending Machine, Payment Gateway, Uber (Driver), Cloud (Resource) |
| **Observer** | Notify subscribers of changes | Scoreboard, Notification, Pub-Sub, Uber |
| **Chain of Responsibility** | Pipeline of handlers | Payment Gateway (fraud), LLM Orchestrator, Logging |
| **Singleton** | One global instance | Parking Lot, Logger |
| **Producer-Consumer** | Decouple submission from execution | Thread Pool, Pub-Sub |
| **Composite** | Tree part-whole hierarchies | File System |
| **Factory** | Create objects without specifying class | LLM Provider selection |

---

## 📖 SOLID Principles

| Principle | Example in This Repo |
|---|---|
| **S** — Single Responsibility | `PaymentMethod` only processes payments, `FraudChecker` only validates |
| **O** — Open-Closed | Add `WalletPayment` without changing `PaymentGateway` |
| **L** — Liskov Substitution | Any `SimilarityMetric` works in `VectorDatabase` |
| **I** — Interface Segregation | `ScoreObserver` has only `onScoreUpdate()` |
| **D** — Dependency Inversion | `CloudAllocator` depends on `AllocationStrategy` interface, not `LeastLoadedStrategy` |
