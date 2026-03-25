package parkinglot;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Singleton ParkingLot — the main entry point.
 *
 * Uses:
 *  - Singleton:  One parking lot instance
 *  - Strategy:   Pluggable spot-finding algorithm
 *  - SRP:        Each class has one job
 */
public class ParkingLot {
    private static ParkingLot instance;

    private final List<ParkingFloor> floors;
    private final Map<Integer, Ticket> activeTickets; // ticketId -> Ticket
    private ParkingStrategy strategy;

    private ParkingLot() {
        floors = new ArrayList<>();
        activeTickets = new HashMap<>();
        strategy = new NearestSpotStrategy(); // default
    }

    public static synchronized ParkingLot getInstance() {
        if (instance == null) instance = new ParkingLot();
        return instance;
    }

    // For testing: reset singleton
    public static void resetInstance() { instance = null; }

    public void setStrategy(ParkingStrategy strategy) {
        this.strategy = strategy;
    }

    public void addFloor(ParkingFloor floor) {
        floors.add(floor);
    }

    public List<ParkingFloor> getFloors() { return floors; }

    /**
     * Park a vehicle: find a spot via strategy, issue a ticket.
     */
    public Ticket parkVehicle(Vehicle vehicle) {
        ParkingSpot spot = strategy.findSpot(this, vehicle);
        if (spot == null) {
            System.out.println("No spot available for " + vehicle);
            return null;
        }
        spot.park(vehicle);

        // Find which floor this spot belongs to
        int floorNum = -1;
        for (ParkingFloor f : floors) {
            if (f.getSpots().contains(spot)) {
                floorNum = f.getFloorNumber();
                break;
            }
        }

        Ticket ticket = new Ticket(vehicle, spot, floorNum);
        activeTickets.put(ticket.getTicketId(), ticket);
        System.out.println("Parked: " + ticket);
        return ticket;
    }

    /**
     * Unpark a vehicle using the ticket.
     */
    public Vehicle unparkVehicle(int ticketId) {
        Ticket ticket = activeTickets.remove(ticketId);
        if (ticket == null) {
            System.out.println("Invalid ticket: " + ticketId);
            return null;
        }
        Vehicle v = ticket.getSpot().unpark();
        System.out.println("Unparked: " + v + " (Ticket#" + ticketId + ")");
        return v;
    }

    public void displayStatus() {
        System.out.println("=== Parking Lot Status ===");
        for (ParkingFloor floor : floors) {
            floor.displayStatus();
        }
        System.out.println("==========================");
    }
}

