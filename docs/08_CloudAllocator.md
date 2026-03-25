# Cloud Resource Allocator — Complete Guide

---

## Part 1: Understanding the Problem

A system that manages cloud resources (VMs, containers) with load-based allocation, health checks, and auto-healing.

### Requirements
- ✓ Provision resources with CPU/memory specs
- ✓ Strategy Pattern: LeastLoaded vs RoundRobin allocation
- ✓ Health check: detect unhealthy resources and auto-replace
- ✓ Resource state: PROVISIONING → HEALTHY → UNHEALTHY → TERMINATED
- ✓ Thread-safe work assignment

---

## Part 2: Key Design

### Strategy — Allocation Algorithms
```java
interface AllocationStrategy {
    Resource allocate(List<Resource> resources, double requiredLoad);
}

class LeastLoadedStrategy  → picks resource with lowest current load (spread evenly)
class RoundRobinStrategy   → cycles through resources in order (fair distribution)
```

### Health Check — Auto-Healing
```java
void healthCheck() {
    for (Resource r : resources) {
        if (r.getStatus() == UNHEALTHY) {
            r.terminate();
            Resource replacement = new Resource(r.getType(), r.getCpu(), r.getMem());
            replacement.setReady();
            resources.add(replacement);  // auto-replace!
        }
    }
}
```

---

## Part 3: Follow-Up Questions

| Question | Answer |
|---|---|
| Auto-scaling? | Monitor avg load. If > 80%, provision new resources. If < 20%, terminate extras. |
| Spot instances? | Add `ResourceTier` (ON_DEMAND, SPOT, RESERVED). Spot can be reclaimed — need graceful migration. |
| Multi-region? | Add `region` field. AllocationStrategy filters by region first, then applies algorithm. |
| Kubernetes-like scheduling? | Add resource requests/limits, affinity rules, taints/tolerations to the strategy. |

---

📁 **Source code:** `src/cloudresource/`

