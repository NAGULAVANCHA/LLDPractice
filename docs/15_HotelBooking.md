# Problem 19: Hotel Booking System — Complete Guide

---

## Part 1: Understanding the Problem

A hotel reservation system with room availability, date-range bookings, and cancellations.

### Requirements
- ✓ Multiple room types (Single, Double, Suite) with pricing
- ✓ Search available rooms by date range and type
- ✓ Book rooms with date-range overlap prevention
- ✓ Cancel reservations (re-opens availability)
- ✓ Thread-safe to prevent double booking

---

## Part 2: The Key Insight — Date Range Overlap Detection

### The Core Algorithm
Two date ranges overlap if:
```
newCheckIn < existingCheckOut  AND  newCheckOut > existingCheckIn
```

### Visual Examples
```
Existing:   |---Jan 5 to Jan 10---|

Case 1: OVERLAP (starts during existing)
New:            |---Jan 8 to Jan 12---|
  Jan 8 < Jan 10 ✓  AND  Jan 12 > Jan 5 ✓  → OVERLAP

Case 2: OVERLAP (contains existing)
New:        |---Jan 3 to Jan 12---|
  Jan 3 < Jan 10 ✓  AND  Jan 12 > Jan 5 ✓  → OVERLAP

Case 3: NO OVERLAP (completely after)
New:                                  |---Jan 10 to Jan 15---|
  Jan 10 < Jan 10? NO → NO OVERLAP ✓  (checkout day = checkin day is OK)

Case 4: NO OVERLAP (completely before)
New:    |---Jan 1 to Jan 5---|
  Jan 1 < Jan 10 ✓  AND  Jan 5 > Jan 5? NO → NO OVERLAP ✓
```

### In Code
```java
public synchronized boolean isAvailable(LocalDate checkIn, LocalDate checkOut) {
    for (Reservation r : reservations) {
        if (r.getStatus() == Status.CANCELLED) continue;  // skip cancelled
        if (checkIn.isBefore(r.getCheckOut()) && checkOut.isAfter(r.getCheckIn())) {
            return false;  // OVERLAP found!
        }
    }
    return true;
}
```

---

## Part 3: The Code — Explained

### Room — Type + Availability
```java
public class Room {
    public enum RoomType { SINGLE, DOUBLE, SUITE }

    private final int roomNumber;
    private final RoomType type;
    private final double pricePerNight;
    private final List<Reservation> reservations;

    public synchronized boolean isAvailable(LocalDate checkIn, LocalDate checkOut) { ... }
    public synchronized void addReservation(Reservation r) { reservations.add(r); }
}
```

### Reservation — Immutable Record + Pricing
```java
public class Reservation {
    public enum Status { CONFIRMED, CANCELLED, CHECKED_IN, CHECKED_OUT }

    private final String guestName;
    private final Room room;
    private final LocalDate checkIn, checkOut;
    private final double totalAmount;  // nights × pricePerNight
    private Status status;

    public Reservation(String guest, Room room, LocalDate in, LocalDate out) {
        long nights = ChronoUnit.DAYS.between(in, out);
        this.totalAmount = nights * room.getPricePerNight();
        this.status = Status.CONFIRMED;
    }

    public void cancel() { this.status = Status.CANCELLED; }
}
```

### Hotel — Search + Book
```java
public class Hotel {
    private final List<Room> rooms;
    private final List<Reservation> reservations;

    public List<Room> searchAvailableRooms(LocalDate in, LocalDate out, RoomType type) {
        return rooms.stream()
            .filter(r -> (type == null || r.getType() == type) && r.isAvailable(in, out))
            .collect(toList());
    }

    public synchronized Reservation bookRoom(String guest, int roomNum,
                                              LocalDate in, LocalDate out) {
        Room room = findRoom(roomNum);
        if (!room.isAvailable(in, out)) { "Not available!"; return null; }

        Reservation r = new Reservation(guest, room, in, out);
        room.addReservation(r);
        reservations.add(r);
        return r;
    }
}
```

---

## Part 4: Data Flow

```
1. Search: Available SINGLE rooms Jan 1-5?
   → Room 101: check reservations → none → AVAILABLE ✓
   → Room 102: check reservations → none → AVAILABLE ✓

2. Alice books Room 101, Jan 1-5 ($100/night × 4 nights = $400)
   → room.isAvailable(Jan 1, Jan 5)? YES
   → Create Reservation(Alice, 101, Jan 1-5, $400, CONFIRMED)
   → room.addReservation(r)

3. Charlie tries Room 101, Jan 3-7
   → room.isAvailable(Jan 3, Jan 7)?
     → Check: Jan 3 < Jan 5 (Alice's checkout) ✓
     →        Jan 7 > Jan 1 (Alice's checkin)   ✓
     → OVERLAP → NOT available ✗

4. Alice cancels
   → reservation.cancel() → status = CANCELLED
   → isAvailable skips cancelled reservations

5. Charlie retries Room 101, Jan 3-7
   → Alice's reservation is CANCELLED → skipped
   → AVAILABLE ✓ → booking succeeds
```

---

## Part 5: Follow-Up Questions

| Question | Answer |
|---|---|
| Dynamic pricing? | Create `PricingStrategy` interface. Implementations: `SeasonalPricing`, `WeekendPricing`, `DemandPricing`. |
| Room amenities search? | Add `Set<Amenity>` to Room. Filter by amenities in search. |
| Group booking? | Book multiple rooms in one transaction. All-or-nothing (like BookMyShow). |
| Waitlist? | If no rooms available, add to waitlist. Notify when cancellation opens a room. |

---

## Part 6: Patterns Recap

| Pattern | Where & Why |
|---|---|
| **Date Overlap** | Core algorithm for availability checking |
| **Concurrency** | `synchronized` on booking prevents double booking |
| **Enum** | RoomType, ReservationStatus — type-safe categories |
| **SRP** | Room manages availability, Reservation stores booking, Hotel orchestrates |
| **Stream API** | Fluent search with filters |

---

📁 **Source code:** `src/hotel/`
