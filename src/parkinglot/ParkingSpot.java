package parkinglot;

public class ParkingSpot {
    private final int spotId;
    private final VehicleType supportedType;
    private Vehicle parkedVehicle;

    public ParkingSpot(int spotId, VehicleType supportedType) {
        this.spotId = spotId;
        this.supportedType = supportedType;
    }

    public boolean isAvailable() {
        return parkedVehicle == null;
    }

    public boolean canFit(Vehicle vehicle) {
        return isAvailable() && vehicle.getType() == supportedType;
    }

    public void park(Vehicle vehicle) {
        if (!canFit(vehicle)) throw new IllegalStateException("Cannot park here");
        this.parkedVehicle = vehicle;
    }

    public Vehicle unpark() {
        Vehicle v = parkedVehicle;
        parkedVehicle = null;
        return v;
    }

    public int getSpotId() { return spotId; }
    public VehicleType getSupportedType() { return supportedType; }
    public Vehicle getParkedVehicle() { return parkedVehicle; }

    @Override
    public String toString() {
        return "Spot-" + spotId + "(" + supportedType + ")" +
                (isAvailable() ? " [FREE]" : " [OCCUPIED by " + parkedVehicle + "]");
    }
}

