package cloudresource;

import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

/** Round-Robin: cycle through resources in order. */
public class RoundRobinStrategy implements AllocationStrategy {
    private final AtomicInteger index = new AtomicInteger(0);

    @Override
    public Resource allocate(List<Resource> resources, double requiredLoad) {
        int size = resources.size();
        for (int i = 0; i < size; i++) {
            int idx = index.getAndIncrement() % size;
            Resource r = resources.get(idx);
            if (r.isHealthy() && r.getCurrentLoad() + requiredLoad <= 1.0)
                return r;
        }
        return null;
    }

    @Override public String getName() { return "RoundRobin"; }
}

