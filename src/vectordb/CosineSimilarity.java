package vectordb;

/** Cosine Similarity: measures angle between vectors. 1.0 = identical, 0 = orthogonal. */
public class CosineSimilarity implements SimilarityMetric {
    @Override
    public double compute(double[] a, double[] b) {
        double dot = 0, normA = 0, normB = 0;
        for (int i = 0; i < a.length; i++) {
            dot += a[i] * b[i];
            normA += a[i] * a[i];
            normB += b[i] * b[i];
        }
        if (normA == 0 || normB == 0) return 0;
        return dot / (Math.sqrt(normA) * Math.sqrt(normB));
    }

    @Override public String getName() { return "Cosine"; }
}

