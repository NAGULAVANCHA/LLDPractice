# Ride-Sharing Service (Uber/Ola) — Complete Guide

---

## Part 1: Understanding the Problem

Build a simplified Uber/Ola where riders request rides, the system matches them with drivers, and manages the ride lifecycle.

### Requirements
- ✓ Riders request rides (pickup → destination)
- ✓ System matches rider with best driver (Strategy Pattern)
- ✓ Driver state management: AVAILABLE → EN_ROUTE → ON_TRIP → AVAILABLE
- ✓ Fare calculation (base fare + distance × rate)
- ✓ Rating system for drivers and riders
- ✓ Cancellation support

### Why This Is Asked in 2026
Tests multiple patterns at once, geospatial thinking, and state management — all things relevant to modern app backends.

---

## Part 2: Driver State Machine

```
                 requestRide()        startTrip()        completeTrip()
AVAILABLE ──────────────→ EN_ROUTE ──────────→ ON_TRIP ──────────→ AVAILABLE
    ↑                        │                                        ↑
    │                   cancel()                                      │
    └────────────────────────┘                                        │
                                                                cancel()
    OFFLINE ← goOffline() / → goOnline() → AVAILABLE
```

**EN_ROUTE:** Driver accepted the ride, driving to the pickup location.
**ON_TRIP:** Rider is in the car, driving to destination.

---

## Part 3: The Key Insight — Strategy Pattern for Matching

### The Problem
How to match riders with drivers? Multiple algorithms:
- **Nearest driver** — minimize pickup time
- **Highest-rated driver** — premium experience
- **Cheapest driver** — budget option
- **Driver with right vehicle type** — SUV, sedan, etc.

### Strategy Pattern
```java
interface MatchingStrategy {
    Driver findDriver(RideService service, Rider rider);
}

class NearestDriverStrategy implements MatchingStrategy {
    Driver findDriver(RideService service, Rider rider) {
        // Loop all available drivers, find minimum distance
        for (Driver d : service.getDrivers()) {
            if (!d.isAvailable()) continue;
            double dist = d.getLocation().distanceTo(rider.getLocation());
            // track minimum
        }
        return nearest;
    }
}

class HighestRatedDriverStrategy implements MatchingStrategy {
    Driver findDriver(RideService service, Rider rider) {
        // Only consider drivers within maxRadius
        // Among those, pick highest rated
    }
}
```

**Swap at runtime:**
```java
service.setMatchingStrategy(new NearestDriverStrategy());       // for regular rides
service.setMatchingStrategy(new HighestRatedDriverStrategy(50)); // for premium rides
```

---

## Part 4: The Code — Explained

### Location — Geospatial (Simplified)
```java
public class Location {
    private double latitude, longitude;

    public double distanceTo(Location other) {
        double dx = this.latitude - other.latitude;
        double dy = this.longitude - other.longitude;
        return Math.sqrt(dx * dx + dy * dy);  // Euclidean (real-world: Haversine)
    }
}
```

### Ride — The Trip Lifecycle
```java
public class Ride {
    enum RideStatus { REQUESTED, DRIVER_ASSIGNED, IN_PROGRESS, COMPLETED, CANCELLED }

    private final Rider rider;
    private Driver driver;
    private final Location pickup, destination;
    private RideStatus status;
    private double fare;

    public void assignDriver(Driver driver) {
        this.driver = driver;
        this.status = DRIVER_ASSIGNED;
        driver.setStatus(EN_ROUTE);       // driver state changes!
    }

    public void startTrip() {
        this.status = IN_PROGRESS;
        driver.setStatus(ON_TRIP);         // driver state changes!
        driver.setCurrentLocation(pickup); // driver is at pickup point
    }

    public void completeTrip() {
        double distance = pickup.distanceTo(destination);
        this.fare = BASE_FARE + distance * RATE_PER_KM;
        this.status = COMPLETED;
        driver.setCurrentLocation(destination);  // driver is at destination
        driver.setStatus(AVAILABLE);              // driver is free again!
    }

    public void cancel() {
        this.status = CANCELLED;
        if (driver != null) driver.setStatus(AVAILABLE);  // free the driver
    }
}
```

### RideService — The Orchestrator
```java
public class RideService {
    private final List<Driver> drivers;
    private MatchingStrategy matchingStrategy;

    public synchronized Ride requestRide(Rider rider, Location pickup, Location dest) {
        Driver driver = matchingStrategy.findDriver(this, rider);
        if (driver == null) { "No drivers available!"; return null; }

        Ride ride = new Ride(rider, pickup, dest);
        ride.assignDriver(driver);
        return ride;
    }
}
```

### Rating System — Running Average
```java
public void addRating(double newRating) {
    rating = (rating * totalRides + newRating) / (totalRides + 1);
    totalRides++;
}
```

---

## Part 5: Data Flow — Complete Ride

```
1. John at (8,8) requests ride to (25,25)
   
2. NearestDriverStrategy scans:
   Alice at (10,10): dist = 2.8   ← nearest!
   Bob at (20,20):   dist = 17.0
   Charlie at (5,5): dist = 4.2
   → Select Alice

3. ride.assignDriver(Alice)
   → Alice: AVAILABLE → EN_ROUTE
   → Ride: REQUESTED → DRIVER_ASSIGNED

4. ride.startTrip()
   → Alice: EN_ROUTE → ON_TRIP
   → Alice.location = (8,8) (pickup)
   → Ride: DRIVER_ASSIGNED → IN_PROGRESS

5. ride.completeTrip()
   → distance = sqrt((25-8)² + (25-8)²) = 24.0 km
   → fare = $5.00 + 24.0 × $10.00 = $245.04
   → Alice: ON_TRIP → AVAILABLE
   → Alice.location = (25,25) (destination)
   → Ride: IN_PROGRESS → COMPLETED

6. Driver/Rider rate each other
   → alice.addRating(4.5)
   → john.addRating(5.0)
```

---

## Part 6: Class Diagram

```
┌──────────────────┐      ┌──────────────────┐
│  MatchingStrategy│◄─────│NearestDriverStrat│
│   (interface)    │      ├──────────────────┤
│ +findDriver()    │◄─────│HighestRatedStrat │
└────────┬─────────┘      └──────────────────┘
         │ uses
┌────────▼──────────┐
│   RideService     │
│ ──────────────────│     ┌──────────┐
│ drivers: List     │────→│  Driver  │
│ riders: List      │     │ ──────── │
│ matchingStrategy  │     │ name     │
│ ──────────────────│     │ status   │ ← AVAILABLE/EN_ROUTE/ON_TRIP
│ +requestRide()    │     │ location │
│ +setStrategy()    │     │ rating   │
└────────┬──────────┘     └──────────┘
         │ creates
┌────────▼──────────┐     ┌──────────┐
│      Ride         │     │  Rider   │
│ ──────────────────│     │ ──────── │
│ rider, driver     │────→│ name     │
│ pickup, dest      │     │ location │
│ status, fare      │     │ rating   │
│ ──────────────────│     └──────────┘
│ +assignDriver()   │
│ +startTrip()      │     ┌──────────┐
│ +completeTrip()   │────→│ Location │
│ +cancel()         │     │ lat, lng │
└───────────────────┘     │ distanceTo│
                          └──────────┘
```

---

## Part 7: Follow-Up Questions

| Question | Answer |
|---|---|
| How to handle surge pricing? | Create `PricingStrategy` interface with `NormalPricing`, `SurgePricing(multiplier)`. Ride uses the current strategy for fare calculation. |
| How to handle ride-sharing (pool)? | Match multiple riders going in the same direction. Create `SharedRide` that has `List<Rider>` and handles per-rider fares. |
| Real geospatial indexing? | Use a **QuadTree** or **GeoHash** to efficiently find nearby drivers without scanning all. In production, use PostGIS or Redis GEO commands. |
| ETA calculation? | Integrate with a routing service (Google Maps API). Or use straight-line distance / average speed as approximation. |
| Driver availability updates? | Drivers send heartbeats. If no heartbeat for 60 seconds, mark as OFFLINE. Observer pattern to notify riders. |
| Payment integration? | Separate `PaymentService` (SRP). After ride completion, charge rider via `paymentService.charge(riderId, fare)`. |

---

## Part 8: Patterns Recap

| Pattern | Where & Why |
|---|---|
| **Strategy** | MatchingStrategy — swappable driver selection algorithm |
| **State** | DriverStatus — behavior/availability changes with state |
| **Observer** | Could notify riders of driver location updates |
| **SRP** | Rider, Driver, Ride, RideService, Location all separate concerns |
| **OCP** | Add new matching strategies without modifying RideService |
| **Synchronized** | `requestRide()` is thread-safe — prevents assigning same driver twice |

---

📁 **Source code:** `src/ridesharing/`

