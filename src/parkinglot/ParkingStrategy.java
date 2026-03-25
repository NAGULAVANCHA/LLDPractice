package parkinglot;

/**
 * Strategy Pattern: Defines HOW we find a parking spot.
 * Allows swapping algorithms without changing ParkingLot code.
 */
public interface ParkingStrategy {
    ParkingSpot findSpot(ParkingLot lot, Vehicle vehicle);
}

