package ridesharing;

/**
 * Match rider with the highest-rated available driver within a radius.
 */
public class HighestRatedDriverStrategy implements MatchingStrategy {
    private final double maxRadius;

    public HighestRatedDriverStrategy(double maxRadius) {
        this.maxRadius = maxRadius;
    }

    @Override
    public Driver findDriver(RideService service, Rider rider) {
        Driver best = null;

        for (Driver d : service.getDrivers()) {
            if (!d.isAvailable()) continue;
            double dist = d.getCurrentLocation().distanceTo(rider.getCurrentLocation());
            if (dist > maxRadius) continue; // too far
            if (best == null || d.getRating() > best.getRating()) {
                best = d;
            }
        }
        return best;
    }

    @Override
    public String getName() { return "Highest Rated (radius=" + maxRadius + ")"; }
}

