package videobuffer;

/** A single video segment (e.g., 2 seconds of video at a quality level). */
public class VideoSegment implements Comparable<VideoSegment> {
    private final int segmentId;
    private final double startTimeSec;
    private final double durationSec;
    private final String quality; // "1080p", "720p", "480p"
    private final int sizeKB;

    public VideoSegment(int segmentId, double startTime, double duration, String quality, int sizeKB) {
        this.segmentId = segmentId;
        this.startTimeSec = startTime;
        this.durationSec = duration;
        this.quality = quality;
        this.sizeKB = sizeKB;
    }

    public int getSegmentId() { return segmentId; }
    public double getStartTimeSec() { return startTimeSec; }
    public double getDurationSec() { return durationSec; }
    public double getEndTimeSec() { return startTimeSec + durationSec; }
    public String getQuality() { return quality; }
    public int getSizeKB() { return sizeKB; }

    @Override
    public int compareTo(VideoSegment other) {
        return Double.compare(this.startTimeSec, other.startTimeSec);
    }

    @Override
    public String toString() {
        return String.format("Seg#%d [%.1f-%.1fs] %s %dKB",
                segmentId, startTimeSec, getEndTimeSec(), quality, sizeKB);
    }
}

