package cloudresource;

public class CloudAllocatorDemo {
    public static void main(String[] args) {
        System.out.println("=== Cloud Resource Allocator Demo ===\n");

        // --- Least Loaded Strategy ---
        System.out.println("--- Least Loaded Strategy ---");
        CloudAllocator cloud = new CloudAllocator(new LeastLoadedStrategy());
        cloud.provision("VM", 4, 8192);
        cloud.provision("VM", 4, 8192);
        cloud.provision("CONTAINER", 2, 4096);

        cloud.assignWork(0.30);
        cloud.assignWork(0.50);
        cloud.assignWork(0.20);
        cloud.assignWork(0.40);
        cloud.displayResources();

        // --- Switch to Round Robin ---
        System.out.println("\n--- Switch to Round Robin ---");
        cloud.setStrategy(new RoundRobinStrategy());
        cloud.assignWork(0.10);
        cloud.assignWork(0.10);
        cloud.displayResources();

        // --- Health Check ---
        System.out.println("\n--- Simulating resource failure ---");
        cloud.getResources().get(0).markUnhealthy();
        cloud.healthCheck();
        cloud.displayResources();
    }
}

