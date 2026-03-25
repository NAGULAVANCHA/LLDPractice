package vectordb;

/** Strategy Pattern: different similarity/distance metrics. */
public interface SimilarityMetric {
    double compute(double[] a, double[] b);
    String getName();
}

