package parkinglot;

import java.util.ArrayList;
import java.util.List;

public class ParkingFloor {
    private final int floorNumber;
    private final List<ParkingSpot> spots;

    public ParkingFloor(int floorNumber) {
        this.floorNumber = floorNumber;
        this.spots = new ArrayList<>();
    }

    public void addSpot(ParkingSpot spot) {
        spots.add(spot);
    }

    public int getFloorNumber() { return floorNumber; }
    public List<ParkingSpot> getSpots() { return spots; }

    public long availableCount(VehicleType type) {
        return spots.stream()
                .filter(s -> s.getSupportedType() == type && s.isAvailable())
                .count();
    }

    public void displayStatus() {
        System.out.println("  Floor " + floorNumber + ":");
        for (ParkingSpot spot : spots) {
            System.out.println("    " + spot);
        }
    }
}

