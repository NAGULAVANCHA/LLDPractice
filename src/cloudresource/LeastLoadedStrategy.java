package cloudresource;

import java.util.List;

/** Pick the resource with LEAST current load (spread evenly). */
public class LeastLoadedStrategy implements AllocationStrategy {
    @Override
    public Resource allocate(List<Resource> resources, double requiredLoad) {
        Resource best = null;
        for (Resource r : resources) {
            if (!r.isHealthy()) continue;
            if (r.getCurrentLoad() + requiredLoad > 1.0) continue;
            if (best == null || r.getCurrentLoad() < best.getCurrentLoad())
                best = r;
        }
        return best;
    }

    @Override public String getName() { return "LeastLoaded"; }
}

