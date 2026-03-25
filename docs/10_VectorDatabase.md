# In-Memory Vector Database — Complete Guide

---

## Part 1: Understanding the Problem

A simplified Pinecone/Weaviate: store high-dimensional embedding vectors and perform K-Nearest-Neighbor (KNN) similarity search. Essential for AI/RAG systems in 2026.

### Requirements
- ✓ Insert, delete, lookup vectors by key
- ✓ KNN search: find top-K most similar vectors to a query
- ✓ Strategy Pattern: swappable similarity metrics (Cosine, Euclidean)
- ✓ Dimension validation

---

## Part 2: Key Design

### Similarity Metrics (Strategy Pattern)
```java
interface SimilarityMetric {
    double compute(double[] a, double[] b);
}

class CosineSimilarity → measures angle between vectors (1.0 = identical)
  dot(a,b) / (||a|| × ||b||)

class EuclideanDistance → straight-line distance (inverted: 1/(1+dist))
  √(Σ(ai-bi)²)
```

### KNN Search — Top-K with Min-Heap
```java
List<SearchResult> search(double[] query, int topK) {
    PriorityQueue<SearchResult> pq = new PriorityQueue<>(byScore);
    for (VectorEntry entry : store.values()) {
        double score = metric.compute(query, entry.getVector());
        pq.offer(new SearchResult(entry, score));
        if (pq.size() > topK) pq.poll(); // evict lowest score
    }
    return sorted(pq); // descending by score
}
```
**Time: O(n log k)** per query (brute-force). Real DBs use HNSW/IVF indexes for O(log n).

---

## Part 3: Follow-Up Questions

| Question | Answer |
|---|---|
| Scale to billions? | Use HNSW (Hierarchical Navigable Small World) index for approximate nearest neighbor (ANN). |
| Filtering? | Add metadata filters: `search(query, topK, filter="category=animal")`. Filter before or after scoring. |
| Namespaces? | Partition vectors by namespace. Each namespace is an independent index. |
| Persistence? | Serialize to disk (memory-mapped files). Load on startup. Write-ahead log for durability. |

---

📁 **Source code:** `src/vectordb/`

