package vectordb;

import java.util.*;

/**
 * In-Memory Vector Database — stores embeddings and performs
 * K-Nearest-Neighbor (KNN) search using a configurable similarity metric.
 */
public class VectorDatabase {
    private final Map<String, VectorEntry> store = new LinkedHashMap<>();
    private SimilarityMetric metric;
    private final int dimension;

    public VectorDatabase(int dimension, SimilarityMetric metric) {
        this.dimension = dimension;
        this.metric = metric;
    }

    public void setMetric(SimilarityMetric metric) { this.metric = metric; }

    public void insert(String key, double[] vector, String metadata) {
        if (vector.length != dimension)
            throw new IllegalArgumentException("Expected dim=" + dimension + ", got " + vector.length);
        store.put(key, new VectorEntry(key, vector, metadata));
    }

    public void delete(String key) { store.remove(key); }

    public VectorEntry get(String key) { return store.get(key); }

    /**
     * KNN Search: find top-K most similar vectors to the query.
     * Brute-force scan — O(n) per query. Real DBs use HNSW/IVF indexes.
     */
    public List<SearchResult> search(double[] query, int topK) {
        PriorityQueue<SearchResult> pq = new PriorityQueue<>(
                Comparator.comparingDouble(SearchResult::getScore));

        for (VectorEntry entry : store.values()) {
            double score = metric.compute(query, entry.getVector());
            pq.offer(new SearchResult(entry, score));
            if (pq.size() > topK) pq.poll(); // keep only top-K
        }

        List<SearchResult> results = new ArrayList<>(pq);
        results.sort((a, b) -> Double.compare(b.getScore(), a.getScore())); // descending
        return results;
    }

    public int size() { return store.size(); }

    public static class SearchResult {
        private final VectorEntry entry;
        private final double score;

        public SearchResult(VectorEntry entry, double score) {
            this.entry = entry;
            this.score = score;
        }

        public VectorEntry getEntry() { return entry; }
        public double getScore() { return score; }

        @Override
        public String toString() {
            return String.format("%.4f", score) + " — " + entry;
        }
    }
}

