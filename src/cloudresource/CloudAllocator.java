package cloudresource;

import java.util.*;

public class CloudAllocator {
    private final List<Resource> resources = new ArrayList<>();
    private AllocationStrategy strategy;

    public CloudAllocator(AllocationStrategy strategy) {
        this.strategy = strategy;
    }

    public void setStrategy(AllocationStrategy strategy) {
        this.strategy = strategy;
        System.out.println("  Strategy set to: " + strategy.getName());
    }

    public Resource provision(String type, int cpuCores, int memoryMB) {
        Resource r = new Resource(type, cpuCores, memoryMB);
        r.setReady();
        resources.add(r);
        System.out.println("  ✅ Provisioned: " + r);
        return r;
    }

    public synchronized Resource assignWork(double load) {
        Resource r = strategy.allocate(resources, load);
        if (r == null) {
            System.out.println("  ❌ No resource available for load " + String.format("%.0f%%", load * 100));
            return null;
        }
        r.assignLoad(load);
        System.out.println("  📤 Assigned " + String.format("%.0f%%", load * 100) + " load to " + r);
        return r;
    }

    /** Health check: detect and replace unhealthy resources. */
    public void healthCheck() {
        System.out.println("\n🏥 Health Check:");
        for (Resource r : new ArrayList<>(resources)) {
            if (r.getStatus() == ResourceStatus.UNHEALTHY) {
                System.out.println("  ⚠️ " + r + " is UNHEALTHY → auto-replacing...");
                r.terminate();
                Resource replacement = new Resource(r.getType(), r.getCpuCores(), r.getMemoryMB());
                replacement.setReady();
                resources.add(replacement);
                System.out.println("  ✅ Replaced with: " + replacement);
            } else {
                System.out.println("  ✓ " + r);
            }
        }
        resources.removeIf(r -> r.getStatus() == ResourceStatus.TERMINATED);
    }

    public void displayResources() {
        System.out.println("\n=== Cloud Resources ===");
        resources.forEach(r -> System.out.println("  " + r));
    }

    public List<Resource> getResources() { return resources; }
}

