package vectordb;

import java.util.Arrays;

/** An embedding vector with a key and metadata. */
public class VectorEntry {
    private final String key;
    private final double[] vector;
    private final String metadata;

    public VectorEntry(String key, double[] vector, String metadata) {
        this.key = key;
        this.vector = Arrays.copyOf(vector, vector.length);
        this.metadata = metadata;
    }

    public String getKey() { return key; }
    public double[] getVector() { return vector; }
    public String getMetadata() { return metadata; }
    public int getDimension() { return vector.length; }

    @Override
    public String toString() {
        return key + " [dim=" + vector.length + "] — " + metadata;
    }
}

