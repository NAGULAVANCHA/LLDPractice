package hotel;

import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

public class Hotel {
    private final String name;
    private final List<Room> rooms;
    private final List<Reservation> reservations;

    public Hotel(String name) {
        this.name = name;
        this.rooms = new ArrayList<>();
        this.reservations = new ArrayList<>();
    }

    public void addRoom(Room room) { rooms.add(room); }

    public List<Room> searchAvailableRooms(LocalDate checkIn, LocalDate checkOut, Room.RoomType type) {
        return rooms.stream()
                .filter(r -> (type == null || r.getType() == type) && r.isAvailable(checkIn, checkOut))
                .collect(Collectors.toList());
    }

    public synchronized Reservation bookRoom(String guestName, int roomNumber,
                                              LocalDate checkIn, LocalDate checkOut) {
        Room room = rooms.stream().filter(r -> r.getRoomNumber() == roomNumber).findFirst().orElse(null);
        if (room == null) { System.out.println("  Room " + roomNumber + " not found!"); return null; }
        if (!room.isAvailable(checkIn, checkOut)) {
            System.out.println("  ❌ Room " + roomNumber + " not available for these dates!");
            return null;
        }

        Reservation reservation = new Reservation(guestName, room, checkIn, checkOut);
        room.addReservation(reservation);
        reservations.add(reservation);
        System.out.println("  ✅ " + reservation);
        return reservation;
    }

    public void cancelReservation(int reservationId) {
        Reservation r = reservations.stream()
                .filter(res -> res.getReservationId() == reservationId).findFirst().orElse(null);
        if (r == null) { System.out.println("  Reservation not found!"); return; }
        r.cancel();
        System.out.println("  Cancelled: " + r);
    }

    // --- Demo ---
    public static void main(String[] args) {
        System.out.println("=== Hotel Booking System Demo ===\n");

        Hotel hotel = new Hotel("Grand Hotel");
        hotel.addRoom(new Room(101, Room.RoomType.SINGLE, 100));
        hotel.addRoom(new Room(102, Room.RoomType.SINGLE, 100));
        hotel.addRoom(new Room(201, Room.RoomType.DOUBLE, 180));
        hotel.addRoom(new Room(301, Room.RoomType.SUITE, 350));

        LocalDate jan1 = LocalDate.of(2026, 1, 1);
        LocalDate jan5 = LocalDate.of(2026, 1, 5);
        LocalDate jan3 = LocalDate.of(2026, 1, 3);
        LocalDate jan7 = LocalDate.of(2026, 1, 7);

        // Search
        System.out.println("--- Available Singles (Jan 1-5) ---");
        hotel.searchAvailableRooms(jan1, jan5, Room.RoomType.SINGLE)
                .forEach(r -> System.out.println("  " + r));

        // Book
        System.out.println("\n--- Bookings ---");
        Reservation r1 = hotel.bookRoom("Alice", 101, jan1, jan5);
        Reservation r2 = hotel.bookRoom("Bob", 201, jan3, jan7);

        // Try overlapping booking (should fail)
        System.out.println("\n--- Overlapping booking attempt ---");
        hotel.bookRoom("Charlie", 101, jan3, jan7);

        // Cancel and rebook
        System.out.println("\n--- Cancel & rebook ---");
        hotel.cancelReservation(r1.getReservationId());
        hotel.bookRoom("Charlie", 101, jan3, jan7); // should work now

        // Search suites
        System.out.println("\n--- Available Suites (Jan 1-5) ---");
        hotel.searchAvailableRooms(jan1, jan5, Room.RoomType.SUITE)
                .forEach(r -> System.out.println("  " + r));
    }
}

