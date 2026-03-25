package parkinglot;

/**
 * Nearest-first strategy: finds the first available spot
 * on the lowest floor.
 */
public class NearestSpotStrategy implements ParkingStrategy {
    @Override
    public ParkingSpot findSpot(ParkingLot lot, Vehicle vehicle) {
        for (ParkingFloor floor : lot.getFloors()) {
            for (ParkingSpot spot : floor.getSpots()) {
                if (spot.canFit(vehicle)) {
                    return spot;
                }
            }
        }
        return null; // No spot available
    }
}

