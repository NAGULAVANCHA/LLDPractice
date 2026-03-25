/*
╔══════════════════════════════════════════════════════════════════════════════════╗
║                                                                                ║
║               PROBLEM 1: PARKING LOT SYSTEM — COMPLETE GUIDE                   ║
║                                                                                ║
╚══════════════════════════════════════════════════════════════════════════════════╝

═══════════════════════════════════════════════════════════════════════════════════
 PART 1: HOW TO APPROACH ANY LLD PROBLEM (THE FRAMEWORK)
═══════════════════════════════════════════════════════════════════════════════════

When the interviewer says: "Design a Parking Lot system"

Your brain should follow these 5 steps IN ORDER:

  STEP 1: CLARIFY REQUIREMENTS (2-3 minutes)
  ─────────────────────────────────────────
  Ask questions! Don't jump into code.
  
  "Before I start, let me clarify a few things:"
  - How many floors? Multi-floor or single?
  - What types of vehicles? (bike, car, truck)
  - Can a truck park in a car spot? (usually no)
  - Do we need payment/pricing?
  - Do we need entry/exit gates?
  - Is there a specific algorithm for assigning spots?
  - Do we need to track time (for hourly billing)?
  
  The interviewer will say "Yes" or "No" — this NARROWS your scope.
  For this problem, we'll assume:
    ✓ Multi-floor parking lot
    ✓ 3 vehicle types: BIKE, CAR, TRUCK  
    ✓ Each spot supports ONE vehicle type
    ✓ Ticket issued on entry, used to exit
    ✓ Find nearest available spot

  STEP 2: IDENTIFY THE CORE ENTITIES / NOUNS (2-3 minutes)
  ─────────────────────────────────────────────────────────
  Read the requirements and pick out the NOUNS — these become your CLASSES.
  
  From our requirements:
    "Vehicles park in Spots on Floors in a Parking Lot and get a Ticket"
    
  Nouns → Classes:
    • Vehicle
    • VehicleType (BIKE, CAR, TRUCK)
    • ParkingSpot
    • ParkingFloor
    • ParkingLot
    • Ticket

  STEP 3: IDENTIFY BEHAVIORS / VERBS (2 minutes)
  ──────────────────────────────────────────────
  What ACTIONS does the system need to do?
  
    • park(vehicle)        → find spot, occupy it, issue ticket
    • unpark(ticketId)     → free the spot, return vehicle
    • findSpot(vehicle)    → find an available matching spot
    • displayStatus()      → show what's occupied/free
    
  These become METHODS on your classes.

  STEP 4: IDENTIFY RELATIONSHIPS (2 minutes)
  ──────────────────────────────────────────
  How do classes relate to each other?
  
    ParkingLot  HAS MANY  ParkingFloor    (composition — floors don't exist without the lot)
    ParkingFloor HAS MANY ParkingSpot     (composition)
    ParkingSpot  HAS ONE  Vehicle         (or null if empty)
    Ticket       HAS ONE  Vehicle + Spot  (records what's parked where)
    Vehicle      HAS ONE  VehicleType     (every vehicle has a type)

  STEP 5: START CODING — Bottom Up (15-20 minutes)
  ────────────────────────────────────────────────
  Code the smallest, independent classes FIRST. Then build up.
  Order: VehicleType → Vehicle → ParkingSpot → ParkingFloor → Ticket → ParkingLot


═══════════════════════════════════════════════════════════════════════════════════
 PART 2: THE CODE — EXPLAINED LINE BY LINE
═══════════════════════════════════════════════════════════════════════════════════

Let's walk through every class, why each line exists, and what pattern it uses.


─── CLASS 1: VehicleType (Enum) ────────────────────────────────────────────────

    public enum VehicleType {
        BIKE, CAR, TRUCK
    }

  WHY AN ENUM?
    - We have a FIXED set of vehicle types that won't change at runtime
    - Enums are type-safe: you can't accidentally write "Car" vs "CAR" vs "car"
    - Compare with == instead of .equals() (faster, null-safe)
    - If you used Strings like "BIKE", someone could typo "BIEK" and it compiles fine
    - Enum catches that at COMPILE time
    

─── CLASS 2: Vehicle ───────────────────────────────────────────────────────────

    public class Vehicle {
        private final String licensePlate;  // unique identifier
        private final VehicleType type;     // what kind of vehicle

        public Vehicle(String licensePlate, VehicleType type) {
            this.licensePlate = licensePlate;
            this.type = type;
        }
        // getters...
    }

  WHY `final`?
    - A vehicle's license plate and type NEVER change
    - `final` means: set once in constructor, never again
    - This makes Vehicle IMMUTABLE — safer in multi-threaded environments
    - Interviewer loves seeing `final` — shows you think about immutability

  WHY NOT just use a String for type?
    - Vehicle("ABC-123", "car") — what if someone types "Car"? Or "CAR"? Or "sedan"?
    - Vehicle("ABC-123", VehicleType.CAR) — compiler enforces it. Only 3 options.


─── CLASS 3: ParkingSpot ───────────────────────────────────────────────────────

    public class ParkingSpot {
        private final int spotId;
        private final VehicleType supportedType;  // what TYPE of vehicle this spot accepts
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

  KEY DESIGN DECISIONS:
  
  1. `canFit()` is on ParkingSpot, NOT on Vehicle
     - The SPOT decides if a vehicle fits, not the vehicle
     - This is correct because the spot knows its own constraints
     - Think: "Can this parking spot fit this vehicle?" ← natural English
     - NOT: "Can this vehicle fit in this spot?" ← less natural
  
  2. `parkedVehicle` is null when empty
     - Simplest way to represent "no vehicle parked here"
     - isAvailable() just checks for null
     
  3. `park()` throws an exception if you try to park illegally
     - Fail-fast: catch bugs immediately rather than silently continuing
     - The caller should check canFit() first

  4. `unpark()` returns the Vehicle and sets parkedVehicle = null
     - Returns what was parked (so we know what left)
     - Clears the spot in ONE atomic operation


─── CLASS 4: ParkingFloor ──────────────────────────────────────────────────────

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

  RELATIONSHIP: ParkingFloor HAS MANY ParkingSpot (Composition)
  
  WHY A SEPARATE CLASS?
    - You might think: "Why not just put all spots in ParkingLot directly?"
    - Because floors are a real-world grouping
    - You might want: "How many car spots are free on floor 2?"
    - You might want floor-specific rules later
    - SRP (Single Responsibility): Floor manages its spots. Lot manages its floors.
    

─── CLASS 5: Ticket ────────────────────────────────────────────────────────────

    public class Ticket {
        private static int counter = 0;       // class-level counter (shared by ALL tickets)
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

  KEY POINTS:
  
  1. `static int counter` — shared across ALL Ticket objects
     - First ticket: id=1, second: id=2, etc.
     - `static` means it belongs to the CLASS, not any single object
     - Like a serial number machine: the MACHINE has one counter, stamps each ticket
  
  2. `LocalDateTime entryTime` — recorded automatically
     - Used for billing: how long was the car parked?
     - Always set to "now" — the user doesn't choose this
  
  3. All fields are `final` — a ticket never changes once issued
     - Immutable object: created once, read many times
     

─── CLASS 6: ParkingStrategy (Interface) — STRATEGY PATTERN ───────────────────

    public interface ParkingStrategy {
        ParkingSpot findSpot(ParkingLot lot, Vehicle vehicle);
    }

  ╔═══════════════════════════════════════════════════════════════════════════╗
  ║  DESIGN PATTERN: STRATEGY PATTERN                                       ║
  ╠═══════════════════════════════════════════════════════════════════════════╣
  ║                                                                         ║
  ║  PROBLEM IT SOLVES:                                                     ║
  ║  You have an algorithm that could be done in MULTIPLE WAYS, and you     ║
  ║  want to switch between them without changing the rest of your code.    ║
  ║                                                                         ║
  ║  REAL WORLD ANALOGY:                                                    ║
  ║  Google Maps: You want to go from A to B.                               ║
  ║  - Strategy 1: Shortest route                                           ║
  ║  - Strategy 2: Fastest route (highways)                                 ║
  ║  - Strategy 3: Avoid tolls                                              ║
  ║  The MAP (context) doesn't change. Only the ROUTE ALGORITHM changes.    ║
  ║                                                                         ║
  ║  IN OUR PARKING LOT:                                                    ║
  ║  - Strategy 1: NearestSpotStrategy (first available on lowest floor)    ║
  ║  - Strategy 2: Could be SpreadOutStrategy (distribute across floors)    ║
  ║  - Strategy 3: Could be VIPStrategy (reserve best spots for VIPs)      ║
  ║                                                                         ║
  ║  HOW IT WORKS:                                                          ║
  ║                                                                         ║
  ║  1. Define an INTERFACE with one method: findSpot()                     ║
  ║  2. Each strategy IMPLEMENTS that interface differently                 ║
  ║  3. ParkingLot holds a REFERENCE to the interface (not a concrete class)║
  ║  4. You can SWAP the strategy at runtime: lot.setStrategy(new X())     ║
  ║                                                                         ║
  ║  WITHOUT Strategy Pattern (BAD):                                        ║
  ║                                                                         ║
  ║    ParkingSpot findSpot(Vehicle v, String algorithm) {                  ║
  ║        if (algorithm.equals("nearest")) {                               ║
  ║            // 50 lines of nearest logic                                 ║
  ║        } else if (algorithm.equals("spread")) {                         ║
  ║            // 50 lines of spread logic                                  ║
  ║        } else if (algorithm.equals("vip")) {                            ║
  ║            // 50 lines of vip logic                                     ║
  ║        }                                                                ║
  ║    }                                                                    ║
  ║    ^ This violates OCP (Open-Closed Principle)                          ║
  ║    ^ Adding a new strategy means MODIFYING this method                  ║
  ║    ^ The ParkingLot class keeps getting BIGGER                          ║
  ║                                                                         ║
  ║  WITH Strategy Pattern (GOOD):                                          ║
  ║                                                                         ║
  ║    interface ParkingStrategy {                                          ║
  ║        ParkingSpot findSpot(ParkingLot lot, Vehicle vehicle);           ║
  ║    }                                                                    ║
  ║                                                                         ║
  ║    class NearestSpotStrategy implements ParkingStrategy { ... }         ║
  ║    class SpreadOutStrategy implements ParkingStrategy { ... }           ║
  ║    class VIPStrategy implements ParkingStrategy { ... }                 ║
  ║                                                                         ║
  ║    // In ParkingLot:                                                    ║
  ║    private ParkingStrategy strategy;                                    ║
  ║    ParkingSpot spot = strategy.findSpot(this, vehicle);                 ║
  ║                                                                         ║
  ║    ^ Adding a new strategy = adding a NEW CLASS                         ║
  ║    ^ ParkingLot code NEVER changes                                      ║
  ║    ^ Each strategy is testable INDEPENDENTLY                            ║
  ║                                                                         ║
  ╚═══════════════════════════════════════════════════════════════════════════╝


  CONCRETE STRATEGY: NearestSpotStrategy

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

  This is the simplest strategy: iterate floors bottom-up, spots left-to-right,
  return the FIRST available spot. Simple, but could be swapped for something smarter.


─── CLASS 7: ParkingLot — SINGLETON PATTERN ────────────────────────────────────

    public class ParkingLot {
        private static ParkingLot instance;            // THE one instance

        private ParkingLot() { ... }                   // PRIVATE constructor!

        public static synchronized ParkingLot getInstance() {
            if (instance == null) instance = new ParkingLot();
            return instance;
        }
    }

  ╔═══════════════════════════════════════════════════════════════════════════╗
  ║  DESIGN PATTERN: SINGLETON PATTERN                                      ║
  ╠═══════════════════════════════════════════════════════════════════════════╣
  ║                                                                         ║
  ║  PROBLEM IT SOLVES:                                                     ║
  ║  You want EXACTLY ONE instance of a class in the entire application.    ║
  ║                                                                         ║
  ║  REAL WORLD ANALOGY:                                                    ║
  ║  A country has ONE president. You don't create multiple presidents.     ║
  ║  Everyone refers to THE president (same object).                        ║
  ║                                                                         ║
  ║  IN OUR PARKING LOT:                                                    ║
  ║  There's only ONE parking lot. Multiple entry gates should all refer    ║
  ║  to the SAME lot. If someone parks on gate 1, gate 2 should see it.    ║
  ║                                                                         ║
  ║  HOW IT WORKS:                                                          ║
  ║                                                                         ║
  ║  1. Make the constructor PRIVATE                                        ║
  ║     → Nobody outside can do `new ParkingLot()`                          ║
  ║                                                                         ║
  ║  2. Create a STATIC field to hold the one instance                      ║
  ║     → `private static ParkingLot instance;`                             ║
  ║                                                                         ║
  ║  3. Create a STATIC METHOD to get it                                    ║
  ║     → `public static ParkingLot getInstance()`                          ║
  ║     → First call: creates the instance                                  ║
  ║     → All subsequent calls: returns the SAME instance                   ║
  ║                                                                         ║
  ║  4. `synchronized` → thread-safe (only one thread can enter at a time)  ║
  ║                                                                         ║
  ║  WITHOUT Singleton:                                                     ║
  ║    ParkingLot lot1 = new ParkingLot();  // gate 1 uses this             ║
  ║    ParkingLot lot2 = new ParkingLot();  // gate 2 uses this             ║
  ║    lot1.park(car1);  // car parked in lot1's memory                     ║
  ║    lot2.isFull();    // doesn't know about car1! BUG!                   ║
  ║                                                                         ║
  ║  WITH Singleton:                                                        ║
  ║    ParkingLot lot1 = ParkingLot.getInstance();  // THE instance         ║
  ║    ParkingLot lot2 = ParkingLot.getInstance();  // SAME instance        ║
  ║    lot1 == lot2  → TRUE! Both point to same object.                     ║
  ║                                                                         ║
  ║  WHEN TO USE:                                                           ║
  ║  - Database connection pool (one pool shared by all)                    ║
  ║  - Logger (one logger for the app)                                      ║
  ║  - Configuration manager (one config)                                   ║
  ║  - Any "there can only be one" scenario                                 ║
  ║                                                                         ║
  ║  INTERVIEW TIP:                                                         ║
  ║  Don't overuse it! Singleton makes testing HARDER (global state).       ║
  ║  Mention this to show you understand the trade-off.                     ║
  ║                                                                         ║
  ╚═══════════════════════════════════════════════════════════════════════════╝


  THE PARKING LOT — Core Logic:

    public Ticket parkVehicle(Vehicle vehicle) {
        // STEP 1: Use the strategy to find a spot
        ParkingSpot spot = strategy.findSpot(this, vehicle);
        if (spot == null) {
            System.out.println("No spot available for " + vehicle);
            return null;  // lot is full for this type
        }
        
        // STEP 2: Park the vehicle in the spot
        spot.park(vehicle);

        // STEP 3: Find which floor this spot is on (for the ticket)
        int floorNum = -1;
        for (ParkingFloor f : floors) {
            if (f.getSpots().contains(spot)) {
                floorNum = f.getFloorNumber();
                break;
            }
        }

        // STEP 4: Create a ticket and store it
        Ticket ticket = new Ticket(vehicle, spot, floorNum);
        activeTickets.put(ticket.getTicketId(), ticket);
        return ticket;
    }

    public Vehicle unparkVehicle(int ticketId) {
        // STEP 1: Find and remove the ticket
        Ticket ticket = activeTickets.remove(ticketId);
        if (ticket == null) return null;

        // STEP 2: Free the spot
        Vehicle v = ticket.getSpot().unpark();
        return v;
    }

  NOTICE HOW CLEAN THIS IS:
    - parkVehicle() doesn't know HOW to find a spot (strategy does that)
    - ParkingSpot doesn't know about floors or tickets
    - Ticket doesn't know about parking logic
    - Each class does ONE thing → SRP (Single Responsibility Principle)


═══════════════════════════════════════════════════════════════════════════════════
 PART 3: HOW TO ANSWER IN THE INTERVIEW (SCRIPT)
═══════════════════════════════════════════════════════════════════════════════════

Here's roughly what you should SAY:

1. "Let me start by clarifying the requirements..."
   (Ask 3-4 questions)

2. "Based on that, here are the core entities I see..."
   (Draw/list: Vehicle, VehicleType, ParkingSpot, ParkingFloor, ParkingLot, Ticket)

3. "The main operations are park and unpark..."
   (Explain the flow)

4. "For the spot-finding algorithm, I'll use the Strategy Pattern
    so we can swap algorithms without changing the ParkingLot code..."
   (Explain briefly what Strategy Pattern is)

5. "I'll make ParkingLot a Singleton since there's only one lot..."
   (Explain briefly)

6. START CODING — Bottom-up: VehicleType → Vehicle → ParkingSpot → etc.

7. After coding, say:
   "This is extensible because:
    - Adding a new vehicle type: just add to the enum
    - Adding a new parking strategy: implement the interface
    - Adding payment: create a new PaymentService class
    - None of the existing code needs to change (Open-Closed Principle)"


═══════════════════════════════════════════════════════════════════════════════════
 PART 4: COMPLETE CLASS DIAGRAM (draw this on whiteboard)
═══════════════════════════════════════════════════════════════════════════════════

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
    └──────┬──────┘        │ -instance: ParkingLot (static)  │
           │               │ -floors: List<ParkingFloor>     │
           │               │ -activeTickets: Map<int,Ticket> │
           │               │ -strategy: ParkingStrategy      │
    ┌──────▼──────┐        │  ──────────────────────────────  │
    │ ParkingSpot │        │ +getInstance() (static)         │
    │ ──────────  │◄───────│ +parkVehicle(Vehicle): Ticket   │
    │ spotId      │ uses   │ +unparkVehicle(ticketId): Vehicle│
    │ supportedType│       └──────────────┬─────────────────┘
    │ parkedVehicle│                      │ has-many
    │ ──────────  │               ┌───────▼───────┐
    │ +canFit()   │               │ ParkingFloor  │
    │ +park()     │◄──────────────│ ────────────  │
    │ +unpark()   │   has-many    │ floorNumber   │
    └──────┬──────┘               │ spots: List   │
           │                      └───────────────┘
           │
    ┌──────▼──────┐
    │   Ticket    │
    │ ──────────  │
    │ ticketId    │
    │ vehicle     │
    │ spot        │
    │ floorNumber │
    │ entryTime   │
    └─────────────┘


═══════════════════════════════════════════════════════════════════════════════════
 PART 5: POSSIBLE FOLLOW-UP QUESTIONS (and how to answer)
═══════════════════════════════════════════════════════════════════════════════════

  Q: "How would you add payment/pricing?"
  A: Create a PricingStrategy interface with methods like:
     calculateCost(entryTime, exitTime, vehicleType) → double
     Implementations: HourlyPricing, FlatRatePricing, etc.
     This is another Strategy Pattern application.

  Q: "How would you handle multiple entry/exit gates?"
  A: Create Gate class with type (ENTRY/EXIT). Each gate uses the
     same Singleton ParkingLot. This is exactly why Singleton helps here.

  Q: "What if a truck can park in 2 car spots?"
  A: Change canFit() to be smarter. Maybe a spot has a "size" (1, 2, 4)
     and a vehicle needs a certain number of contiguous spots.

  Q: "How would you handle concurrency?"
  A: Make parkVehicle() and unparkVehicle() synchronized.
     Or use ConcurrentHashMap for activeTickets and locks per spot.

  Q: "What if we want to reserve a spot in advance?"
  A: Add a Reservation class with spot + timeSlot. ParkingSpot checks 
     both parkedVehicle AND active reservations.


═══════════════════════════════════════════════════════════════════════════════════
 PART 6: PATTERNS RECAP FOR THIS PROBLEM
═══════════════════════════════════════════════════════════════════════════════════

  ┌──────────────────────┬───────────────────────────────────────────────┐
  │ Pattern              │ Where & Why                                   │
  ├──────────────────────┼───────────────────────────────────────────────┤
  │ Singleton            │ ParkingLot — only one lot exists              │
  │ Strategy             │ ParkingStrategy — swappable spot-finding algo │
  │ SRP (SOLID)          │ Each class has ONE job                        │
  │ OCP (SOLID)          │ Add strategies without changing existing code │
  │ Composition (OOP)    │ Lot HAS Floors, Floor HAS Spots              │
  │ Encapsulation (OOP)  │ parkedVehicle is private, accessed via methods│
  │ Enum (language feat) │ VehicleType — type-safe constants             │
  └──────────────────────┴───────────────────────────────────────────────┘


═══════════════════════════════════════════════════════════════════════════════════
 PART 7: DATA FLOW — WHAT HAPPENS WHEN A CAR ENTERS
═══════════════════════════════════════════════════════════════════════════════════

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

═══════════════════════════════════════════════════════════════════════════════════

  That's Problem 1 done! Say "next" and I'll explain Problem 2: Vending Machine
  (State Pattern) in the same depth.

═══════════════════════════════════════════════════════════════════════════════════
*/

