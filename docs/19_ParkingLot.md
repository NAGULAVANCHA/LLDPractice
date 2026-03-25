# Problem 1: Parking Lot System — Complete Guide

---

## Part 1: How to Approach ANY LLD Problem (The Framework)

When the interviewer says: **"Design a Parking Lot system"**

Your brain should follow these 5 steps **IN ORDER**:

### STEP 1: Clarify Requirements (2-3 minutes)

Ask questions! Don't jump into code.

> "Before I start, let me clarify a few things:"

- How many floors? Multi-floor or single?
- What types of vehicles? (bike, car, truck)
- Can a truck park in a car spot? (usually no)
- Do we need payment/pricing?
- Do we need entry/exit gates?
- Is there a specific algorithm for assigning spots?
- Do we need to track time (for hourly billing)?

The interviewer will say "Yes" or "No" — this NARROWS your scope.

**For this problem, we assume:**
- ✓ Multi-floor parking lot
- ✓ 3 vehicle types: BIKE, CAR, TRUCK
- ✓ Each spot supports ONE vehicle type
- ✓ Ticket issued on entry, used to exit
- ✓ Find nearest available spot

### STEP 2: Identify the Core Entities / Nouns (2-3 minutes)

Read the requirements and pick out the **NOUNS** — these become your **CLASSES**.

> "Vehicles park in Spots on Floors in a Parking Lot and get a Ticket"

| Noun → Class |
|---|
| Vehicle |
| VehicleType (BIKE, CAR, TRUCK) |
| ParkingSpot |
| ParkingFloor |
| ParkingLot |
| Ticket |

### STEP 3: Identify Behaviors / Verbs (2 minutes)

What **ACTIONS** does the system need to do?

| Verb | Method |
|---|---|
| park a vehicle | `parkVehicle(vehicle)` → find spot, occupy it, issue ticket |
| unpark a vehicle | `unparkVehicle(ticketId)` → free the spot, return vehicle |
| find a spot | `findSpot(vehicle)` → find an available matching spot |
| display status | `displayStatus()` → show what's occupied/free |

### STEP 4: Identify Relationships (2 minutes)

| Relationship | Type |
|---|---|
| ParkingLot HAS MANY ParkingFloor | Composition — floors don't exist without the lot |
| ParkingFloor HAS MANY ParkingSpot | Composition |
| ParkingSpot HAS ONE Vehicle | (or null if empty) |
| Ticket HAS ONE Vehicle + Spot | records what's parked where |
| Vehicle HAS ONE VehicleType | every vehicle has a type |

### STEP 5: Start Coding — Bottom Up (15-20 minutes)

Code the smallest, independent classes FIRST. Then build up.

**Order:** `VehicleType` → `Vehicle` → `ParkingSpot` → `ParkingFloor` → `Ticket` → `ParkingLot`

---

## Part 2: The Code — Explained Line by Line

### CLASS 1: VehicleType (Enum)

```java
public enum VehicleType {
    BIKE, CAR, TRUCK
}
```

**WHY AN ENUM?**
- We have a **FIXED** set of vehicle types that won't change at runtime
- Enums are **type-safe**: you can't accidentally write `"Car"` vs `"CAR"` vs `"car"`
- Compare with `==` instead of `.equals()` (faster, null-safe)
- If you used Strings like `"BIKE"`, someone could typo `"BIEK"` and it compiles fine
- Enum catches that at **COMPILE time**

### CLASS 2: Vehicle

```java
public class Vehicle {
    private final String licensePlate;  // unique identifier
    private final VehicleType type;     // what kind of vehicle

    public Vehicle(String licensePlate, VehicleType type) {
        this.licensePlate = licensePlate;
        this.type = type;
    }
    // getters...
}
```

**WHY `final`?**
- A vehicle's license plate and type **NEVER change**
- `final` means: set once in constructor, never again
- This makes Vehicle **IMMUTABLE** — safer in multi-threaded environments
- Interviewer loves seeing `final` — shows you think about immutability

**WHY NOT just use a String for type?**
- `Vehicle("ABC-123", "car")` — what if someone types `"Car"`? Or `"CAR"`? Or `"sedan"`?
- `Vehicle("ABC-123", VehicleType.CAR)` — compiler enforces it. Only 3 options.

### CLASS 3: ParkingSpot

```java
public class ParkingSpot {
    private final int spotId;
    private final VehicleType supportedType;
    private Vehicle parkedVehicle;             // null if empty

    public boolean isAvailable() {
        return parkedVehicle == null;
    }

    public boolean canFit(Vehicle vehicle) {
        return isAvailable() && vehicle.getType() == supportedType;
    }

    public void park(Vehicle vehicle) {
        if (!canFit(vehicle)) throw new IllegalStateException("Cannot park here");
        this.parkedVehicle = vehicle;
    }

    public Vehicle unpark() {
        Vehicle v = parkedVehicle;
        parkedVehicle = null;
        return v;
    }
}
```

**KEY DESIGN DECISIONS:**

1. **`canFit()` is on ParkingSpot, NOT on Vehicle**
   - The SPOT decides if a vehicle fits, not the vehicle
   - Think: *"Can this parking spot fit this vehicle?"* ← natural English

2. **`parkedVehicle` is null when empty**
   - Simplest way to represent "no vehicle parked here"

3. **`park()` throws an exception if you try to park illegally**
   - Fail-fast: catch bugs immediately rather than silently continuing

4. **`unpark()` returns the Vehicle and sets parkedVehicle = null**
   - Returns what was parked AND clears the spot in ONE operation

### CLASS 4: ParkingFloor

```java
public class ParkingFloor {
    private final int floorNumber;
    private final List<ParkingSpot> spots;

    public void addSpot(ParkingSpot spot) {
        spots.add(spot);
    }

    public long availableCount(VehicleType type) {
        return spots.stream()
                .filter(s -> s.getSupportedType() == type && s.isAvailable())
                .count();
    }
}
```

**RELATIONSHIP:** ParkingFloor HAS MANY ParkingSpot (Composition)

**WHY A SEPARATE CLASS?**
- Floors are a real-world grouping
- You might want: *"How many car spots are free on floor 2?"*
- **SRP** (Single Responsibility): Floor manages its spots. Lot manages its floors.

### CLASS 5: Ticket

```java
public class Ticket {
    private static int counter = 0;       // class-level counter
    private final int ticketId;
    private final Vehicle vehicle;
    private final ParkingSpot spot;
    private final int floorNumber;
    private final LocalDateTime entryTime;

    public Ticket(Vehicle vehicle, ParkingSpot spot, int floorNumber) {
        this.ticketId = ++counter;         // auto-increment ID
        this.vehicle = vehicle;
        this.spot = spot;
        this.floorNumber = floorNumber;
        this.entryTime = LocalDateTime.now();
    }
}
```

**KEY POINTS:**

| Feature | Why |
|---|---|
| `static int counter` | Shared across ALL tickets. Like a serial number machine. |
| `LocalDateTime entryTime` | Set to "now" automatically. Used for billing later. |
| All fields `final` | Tickets are **immutable** — created once, read many times. |

### CLASS 6: ParkingStrategy (Interface) — STRATEGY PATTERN

```java
public interface ParkingStrategy {
    ParkingSpot findSpot(ParkingLot lot, Vehicle vehicle);
}
```

---

## 🎯 Design Pattern: Strategy Pattern

### Problem It Solves
You have an algorithm that could be done in **MULTIPLE WAYS**, and you want to **switch between them** without changing the rest of your code.

### Real World Analogy
**Google Maps:** You want to go from A to B.
- Strategy 1: Shortest route
- Strategy 2: Fastest route (highways)
- Strategy 3: Avoid tolls

The **MAP** (context) doesn't change. Only the **ROUTE ALGORITHM** changes.

### In Our Parking Lot
- Strategy 1: `NearestSpotStrategy` (first available on lowest floor)
- Strategy 2: Could be `SpreadOutStrategy` (distribute across floors)
- Strategy 3: Could be `VIPStrategy` (reserve best spots for VIPs)

### WITHOUT Strategy Pattern ❌

```java
ParkingSpot findSpot(Vehicle v, String algorithm) {
    if (algorithm.equals("nearest")) {
        // 50 lines of nearest logic
    } else if (algorithm.equals("spread")) {
        // 50 lines of spread logic
    } else if (algorithm.equals("vip")) {
        // 50 lines of vip logic
    }
}
```
- ❌ Violates **OCP** (Open-Closed Principle)
- ❌ Adding a new strategy means **MODIFYING** this method
- ❌ The ParkingLot class keeps getting **BIGGER**

### WITH Strategy Pattern ✅

```java
interface ParkingStrategy {
    ParkingSpot findSpot(ParkingLot lot, Vehicle vehicle);
}

class NearestSpotStrategy implements ParkingStrategy { ... }
class SpreadOutStrategy implements ParkingStrategy { ... }
class VIPStrategy implements ParkingStrategy { ... }

// In ParkingLot:
private ParkingStrategy strategy;
ParkingSpot spot = strategy.findSpot(this, vehicle);
```
- ✅ Adding a new strategy = adding a **NEW CLASS**
- ✅ ParkingLot code **NEVER changes**
- ✅ Each strategy is testable **INDEPENDENTLY**

### Concrete Strategy: NearestSpotStrategy

```java
public class NearestSpotStrategy implements ParkingStrategy {
    @Override
    public ParkingSpot findSpot(ParkingLot lot, Vehicle vehicle) {
        for (ParkingFloor floor : lot.getFloors()) {         // Floor 1 first
            for (ParkingSpot spot : floor.getSpots()) {      // Spot 1 first
                if (spot.canFit(vehicle)) {
                    return spot;                              // First match wins
                }
            }
        }
        return null;                                          // No spot found
    }
}
```

---

### CLASS 7: ParkingLot — SINGLETON PATTERN

```java
public class ParkingLot {
    private static ParkingLot instance;

    private ParkingLot() { ... }                   // PRIVATE constructor!

    public static synchronized ParkingLot getInstance() {
        if (instance == null) instance = new ParkingLot();
        return instance;
    }
}
```

## 🎯 Design Pattern: Singleton Pattern

### Problem It Solves
You want **EXACTLY ONE** instance of a class in the entire application.

### Real World Analogy
A country has **ONE president**. You don't create multiple presidents. Everyone refers to **THE** president (same object).

### In Our Parking Lot
There's only ONE parking lot. Multiple entry gates should all refer to the **SAME lot**. If someone parks on gate 1, gate 2 should see it.

### How It Works

| Step | Code | Purpose |
|---|---|---|
| 1. Make constructor **PRIVATE** | `private ParkingLot() {}` | Nobody outside can do `new ParkingLot()` |
| 2. Static field for the instance | `private static ParkingLot instance;` | Holds the one instance |
| 3. Static method to get it | `getInstance()` | First call creates it; subsequent calls return same instance |
| 4. `synchronized` | On `getInstance()` | Thread-safe — only one thread can enter at a time |

### WITHOUT Singleton ❌
```java
ParkingLot lot1 = new ParkingLot();  // gate 1 uses this
ParkingLot lot2 = new ParkingLot();  // gate 2 uses this
lot1.park(car1);  // car parked in lot1's memory
lot2.isFull();    // doesn't know about car1! BUG!
```

### WITH Singleton ✅
```java
ParkingLot lot1 = ParkingLot.getInstance();  // THE instance
ParkingLot lot2 = ParkingLot.getInstance();  // SAME instance
lot1 == lot2  // TRUE! Both point to same object.
```

### When To Use
- Database connection pool
- Logger (one logger for the app)
- Configuration manager
- Any "there can only be one" scenario

> ⚠️ **INTERVIEW TIP:** Don't overuse Singleton! It makes testing HARDER (global state). Mention this to show you understand the trade-off.

---

## Core Logic: Park & Unpark

```java
public Ticket parkVehicle(Vehicle vehicle) {
    // 1. Use strategy to find a spot
    ParkingSpot spot = strategy.findSpot(this, vehicle);
    if (spot == null) return null;

    // 2. Park the vehicle in the spot
    spot.park(vehicle);

    // 3. Find which floor this spot is on (for the ticket)
    int floorNum = /* locate floor */;

    // 4. Create a ticket and store it
    Ticket ticket = new Ticket(vehicle, spot, floorNum);
    activeTickets.put(ticket.getTicketId(), ticket);
    return ticket;
}

public Vehicle unparkVehicle(int ticketId) {
    // 1. Find and remove the ticket
    Ticket ticket = activeTickets.remove(ticketId);
    if (ticket == null) return null;

    // 2. Free the spot
    return ticket.getSpot().unpark();
}
```

**NOTICE HOW CLEAN THIS IS:**
- `parkVehicle()` doesn't know HOW to find a spot (strategy does that)
- `ParkingSpot` doesn't know about floors or tickets
- `Ticket` doesn't know about parking logic
- Each class does **ONE thing** → SRP

---

## Part 3: How to Answer in the Interview (Script)

1. *"Let me start by clarifying the requirements..."* (Ask 3-4 questions)
2. *"Based on that, here are the core entities I see..."* (Draw/list them)
3. *"The main operations are park and unpark..."* (Explain the flow)
4. *"For the spot-finding algorithm, I'll use the Strategy Pattern so we can swap algorithms without changing ParkingLot code..."*
5. *"I'll make ParkingLot a Singleton since there's only one lot..."*
6. **START CODING** — Bottom-up: VehicleType → Vehicle → ParkingSpot → etc.
7. After coding, say: *"This is extensible because adding a new vehicle type is just an enum value, adding a strategy is a new class, and none of the existing code changes (Open-Closed Principle)"*

---

## Part 4: Class Diagram

```
┌─────────────┐         ┌──────────────────┐
│ VehicleType │         │  ParkingStrategy  │ ◄── interface
│ ─────────── │         │  ────────────────  │
│ BIKE        │         │ +findSpot()       │
│ CAR         │         └────────▲──────────┘
│ TRUCK       │                  │ implements
└──────┬──────┘         ┌────────┴──────────┐
       │                │NearestSpotStrategy │
       │ has-a          └───────────────────┘
┌──────▼──────┐
│   Vehicle   │
│ ──────────  │        ┌──────────────────────────────────┐
│ licensePlate│        │          ParkingLot              │
│ type        │        │  ──────────────────────────────  │
└──────┬──────┘        │ -instance (static, Singleton)   │
       │               │ -floors: List<ParkingFloor>     │
       │               │ -activeTickets: Map<int,Ticket> │
┌──────▼──────┐        │ -strategy: ParkingStrategy      │
│ ParkingSpot │        │  ──────────────────────────────  │
│ ──────────  │◄───────│ +getInstance() (static)         │
│ spotId      │ uses   │ +parkVehicle(): Ticket           │
│ supportedType│       │ +unparkVehicle(): Vehicle        │
│ parkedVehicle│       └──────────────┬─────────────────┘
│ ──────────  │                      │ has-many
│ +canFit()   │               ┌───────▼───────┐
│ +park()     │               │ ParkingFloor  │
│ +unpark()   │◄──────────────│ ────────────  │
└──────┬──────┘   has-many    │ floorNumber   │
       │                      │ spots: List   │
┌──────▼──────┐               └───────────────┘
│   Ticket    │
│ ──────────  │
│ ticketId    │
│ vehicle     │
│ spot        │
│ floorNumber │
│ entryTime   │
└─────────────┘
```

---

## Part 5: Possible Follow-Up Questions

| Question | Answer |
|---|---|
| How would you add payment/pricing? | Create a `PricingStrategy` interface with `calculateCost(entryTime, exitTime, vehicleType)`. Implementations: `HourlyPricing`, `FlatRatePricing`. Another Strategy Pattern. |
| How would you handle multiple entry/exit gates? | Create a `Gate` class with type (ENTRY/EXIT). Each gate uses the same Singleton ParkingLot. |
| What if a truck can park in 2 car spots? | Change `canFit()` to check "size" — spots have sizes (1, 2, 4), vehicles need contiguous spots. |
| How would you handle concurrency? | Make `parkVehicle()` and `unparkVehicle()` `synchronized`. Or use `ConcurrentHashMap` + locks per spot. |
| What if we want to reserve a spot in advance? | Add `Reservation` class with spot + timeSlot. ParkingSpot checks both `parkedVehicle` AND active reservations. |

---

## Part 6: Patterns Recap

| Pattern | Where & Why |
|---|---|
| **Singleton** | ParkingLot — only one lot exists |
| **Strategy** | ParkingStrategy — swappable spot-finding algorithm |
| **SRP (SOLID)** | Each class has ONE job |
| **OCP (SOLID)** | Add strategies without changing existing code |
| **Composition (OOP)** | Lot HAS Floors, Floor HAS Spots |
| **Encapsulation (OOP)** | parkedVehicle is private, accessed via methods |
| **Enum** | VehicleType — type-safe constants |

---

## Part 7: Data Flow — What Happens When a Car Enters

```
1. Vehicle car = new Vehicle("ABC-123", VehicleType.CAR);
   → A car object is created in memory

2. Ticket ticket = parkingLot.parkVehicle(car);
   → ParkingLot calls: strategy.findSpot(this, car)

3. NearestSpotStrategy loops:
   → Floor 1, Spot 1: BIKE spot → canFit(car)? NO (type mismatch)
   → Floor 1, Spot 2: CAR spot, empty → canFit(car)? YES → return spot 2

4. Back in parkVehicle():
   → spot.park(car)  → spot's parkedVehicle = car
   → Create Ticket(car, spot2, floor1)
   → Store in activeTickets map: {1: ticket}
   → Return ticket to caller

5. Later: parkingLot.unparkVehicle(1)
   → Look up ticket #1 in activeTickets
   → Remove from map
   → spot.unpark() → parkedVehicle = null, return car
   → Spot is free again!
```

---

📁 **Source code:** `src/parkinglot/`

