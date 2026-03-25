package cloudresource;

public class Resource {
    private static int counter = 0;
    private final int resourceId;
    private final String type; // "VM", "CONTAINER", "LAMBDA"
    private final int cpuCores;
    private final int memoryMB;
    private double currentLoad; // 0.0 to 1.0
    private ResourceStatus status;

    public Resource(String type, int cpuCores, int memoryMB) {
        this.resourceId = ++counter;
        this.type = type;
        this.cpuCores = cpuCores;
        this.memoryMB = memoryMB;
        this.currentLoad = 0.0;
        this.status = ResourceStatus.PROVISIONING;
    }

    public void setReady() { this.status = ResourceStatus.HEALTHY; }

    public boolean assignLoad(double load) {
        if (status != ResourceStatus.HEALTHY) return false;
        if (currentLoad + load > 1.0) return false;
        currentLoad += load;
        return true;
    }

    public void releaseLoad(double load) {
        currentLoad = Math.max(0, currentLoad - load);
    }

    public void markUnhealthy() { this.status = ResourceStatus.UNHEALTHY; }
    public void terminate() { this.status = ResourceStatus.TERMINATED; }
    public void heal() { this.status = ResourceStatus.HEALTHY; }

    public int getResourceId() { return resourceId; }
    public String getType() { return type; }
    public int getCpuCores() { return cpuCores; }
    public int getMemoryMB() { return memoryMB; }
    public double getCurrentLoad() { return currentLoad; }
    public ResourceStatus getStatus() { return status; }
    public boolean isHealthy() { return status == ResourceStatus.HEALTHY; }

    @Override
    public String toString() {
        return "R-" + resourceId + " [" + type + " " + cpuCores + "CPU/" + memoryMB + "MB] " +
                "Load:" + String.format("%.0f%%", currentLoad * 100) + " " + status;
    }
}

