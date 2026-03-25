package vectordb;

/** Euclidean Distance: straight-line distance. Lower = more similar. */
public class EuclideanDistance implements SimilarityMetric {
    @Override
    public double compute(double[] a, double[] b) {
        double sum = 0;
        for (int i = 0; i < a.length; i++) {
            double diff = a[i] - b[i];
            sum += diff * diff;
        }
        // Return as similarity (invert distance): 1/(1+dist)
        return 1.0 / (1.0 + Math.sqrt(sum));
    }

    @Override public String getName() { return "Euclidean"; }
}

