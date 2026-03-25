package parkinglot;

import java.time.LocalDateTime;

public class Ticket {
    private static int counter = 0;
    private final int ticketId;
    private final Vehicle vehicle;
    private final ParkingSpot spot;
    private final int floorNumber;
    private final LocalDateTime entryTime;

    public Ticket(Vehicle vehicle, ParkingSpot spot, int floorNumber) {
        this.ticketId = ++counter;
        this.vehicle = vehicle;
        this.spot = spot;
        this.floorNumber = floorNumber;
        this.entryTime = LocalDateTime.now();
    }

    public int getTicketId() { return ticketId; }
    public Vehicle getVehicle() { return vehicle; }
    public ParkingSpot getSpot() { return spot; }
    public int getFloorNumber() { return floorNumber; }
    public LocalDateTime getEntryTime() { return entryTime; }

    @Override
    public String toString() {
        return "Ticket#" + ticketId + " | " + vehicle + " | Floor " + floorNumber +
                " | Spot " + spot.getSpotId() + " | Entry: " + entryTime;
    }
}

