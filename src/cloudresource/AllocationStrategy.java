package cloudresource;

import java.util.List;

/** Strategy Pattern: different resource allocation algorithms. */
public interface AllocationStrategy {
    Resource allocate(List<Resource> resources, double requiredLoad);
    String getName();
}

