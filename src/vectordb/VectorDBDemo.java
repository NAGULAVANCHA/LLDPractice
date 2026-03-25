package vectordb;

public class VectorDBDemo {
    public static void main(String[] args) {
        System.out.println("=== In-Memory Vector Database Demo ===\n");

        VectorDatabase db = new VectorDatabase(3, new CosineSimilarity());

        // Insert "embeddings" (simplified 3D vectors)
        db.insert("cat",    new double[]{1.0, 0.8, 0.2}, "animal, pet");
        db.insert("dog",    new double[]{0.9, 0.9, 0.3}, "animal, pet");
        db.insert("car",    new double[]{0.1, 0.2, 0.9}, "vehicle");
        db.insert("truck",  new double[]{0.2, 0.1, 0.8}, "vehicle");
        db.insert("kitten", new double[]{0.95, 0.85, 0.15}, "animal, baby");
        System.out.println("Inserted " + db.size() + " vectors\n");

        // Search: "what's similar to cat?"
        System.out.println("--- Search: similar to 'cat' (Cosine) ---");
        var results = db.search(new double[]{1.0, 0.8, 0.2}, 3);
        for (var r : results) System.out.println("  " + r);

        // Search: "what's similar to a vehicle?"
        System.out.println("\n--- Search: similar to 'vehicle' query ---");
        results = db.search(new double[]{0.15, 0.15, 0.85}, 3);
        for (var r : results) System.out.println("  " + r);

        // Switch to Euclidean
        System.out.println("\n--- Switch to Euclidean distance ---");
        db.setMetric(new EuclideanDistance());
        results = db.search(new double[]{1.0, 0.8, 0.2}, 3);
        for (var r : results) System.out.println("  " + r);
    }
}

