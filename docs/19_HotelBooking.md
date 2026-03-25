# Problem 19: Hotel Booking

| Pattern | Why |
|---|---|
| **Concurrency** | `synchronized` booking prevents double-booking |
| **Date Range Logic** | Overlap detection for availability |
| **Enum** | RoomType (SINGLE, DOUBLE, SUITE), ReservationStatus |

## Date Overlap — The Key Algorithm
```java
boolean isAvailable(LocalDate checkIn, LocalDate checkOut) {
    for (Reservation r : reservations) {
        if (r.status == CANCELLED) continue;
        // Overlap if: newCheckIn < existingCheckOut AND newCheckOut > existingCheckIn
        if (checkIn.isBefore(r.checkOut) && checkOut.isAfter(r.checkIn))
            return false;
    }
    return true;
}
```

### Visual
```
Existing:     |----Jan 5 to Jan 10----|
New (overlap): |--Jan 8 to Jan 12--|     ← blocked
New (OK):                            |--Jan 10 to Jan 15--| ← allowed
```

## Pricing
```java
long nights = ChronoUnit.DAYS.between(checkIn, checkOut);
totalAmount = nights * room.pricePerNight;
```

## Entities
```
Room        → number, type, pricePerNight, reservations
Reservation → guest, room, dates, amount, status
Hotel       → rooms, searchAvailable, book, cancel
```

📁 `src/hotel/`

