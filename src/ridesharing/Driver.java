package ridesharing;

public class Driver {
    private final String driverId;
    private final String name;
    private final String vehicleInfo;
    private Location currentLocation;
    private DriverStatus status;
    private double rating;
    private int totalRides;

    public Driver(String driverId, String name, String vehicleInfo, Location location) {
        this.driverId = driverId;
        this.name = name;
        this.vehicleInfo = vehicleInfo;
        this.currentLocation = location;
        this.status = DriverStatus.AVAILABLE;
        this.rating = 5.0;
        this.totalRides = 0;
    }

    public void addRating(double r) {
        rating = (rating * totalRides + r) / (totalRides + 1);
        totalRides++;
    }

    public String getDriverId() { return driverId; }
    public String getName() { return name; }
    public String getVehicleInfo() { return vehicleInfo; }
    public Location getCurrentLocation() { return currentLocation; }
    public void setCurrentLocation(Location loc) { this.currentLocation = loc; }
    public DriverStatus getStatus() { return status; }
    public void setStatus(DriverStatus status) {
        System.out.println("  [Driver " + name + "] " + this.status + " → " + status);
        this.status = status;
    }
    public double getRating() { return rating; }
    public boolean isAvailable() { return status == DriverStatus.AVAILABLE; }

    @Override
    public String toString() {
        return name + " (" + vehicleInfo + ") ★" + String.format("%.1f", rating) +
                " [" + status + "] at " + currentLocation;
    }
}

