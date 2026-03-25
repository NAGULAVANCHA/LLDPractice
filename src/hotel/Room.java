package hotel;

/**
 * HOTEL BOOKING SYSTEM LLD
 * ==========================
 * Key Concepts:
 *  - Concurrency:     Synchronized booking to prevent double booking
 *  - Strategy Pattern: Room pricing (peak/off-peak/dynamic)
 *  - SRP:             Room, Guest, Reservation, Hotel separate
 *  - Search:          Find available rooms by date range, type, price
 *
 * Interview Points:
 *  - Date-range based availability (not just single day)
 *  - Room types with different pricing
 *  - Overbooking prevention
 *  - Cancellation policy
 */

import java.time.LocalDate;
import java.util.*;

public class Room {
    public enum RoomType { SINGLE, DOUBLE, SUITE }

    private final int roomNumber;
    private final RoomType type;
    private final double pricePerNight;
    private final List<Reservation> reservations;

    public Room(int roomNumber, RoomType type, double pricePerNight) {
        this.roomNumber = roomNumber;
        this.type = type;
        this.pricePerNight = pricePerNight;
        this.reservations = new ArrayList<>();
    }

    public synchronized boolean isAvailable(LocalDate checkIn, LocalDate checkOut) {
        for (Reservation r : reservations) {
            if (r.getStatus() == Reservation.Status.CANCELLED) continue;
            if (checkIn.isBefore(r.getCheckOut()) && checkOut.isAfter(r.getCheckIn())) {
                return false; // overlap
            }
        }
        return true;
    }

    public synchronized void addReservation(Reservation reservation) {
        reservations.add(reservation);
    }

    public int getRoomNumber() { return roomNumber; }
    public RoomType getType() { return type; }
    public double getPricePerNight() { return pricePerNight; }

    @Override
    public String toString() {
        return "Room " + roomNumber + " (" + type + ") $" + String.format("%.2f", pricePerNight) + "/night";
    }
}

