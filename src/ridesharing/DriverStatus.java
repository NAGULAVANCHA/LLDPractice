package ridesharing;

public enum DriverStatus {
    AVAILABLE,
    EN_ROUTE,   // accepted ride, driving to pickup
    ON_TRIP,    // picked up rider, driving to destination
    OFFLINE
}

