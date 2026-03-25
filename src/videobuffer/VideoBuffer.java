package videobuffer;

import java.util.*;

/**
 * Sliding-window buffer that manages pre-fetched video segments.
 * Maintains a window of buffered segments around the current playback position.
 * Evicts old segments and pre-fetches upcoming ones.
 */
public class VideoBuffer {
    private final int maxBufferSizeKB;
    private final TreeMap<Double, VideoSegment> buffer = new TreeMap<>(); // startTime → segment
    private int currentSizeKB = 0;
    private double playheadSec = 0;

    public VideoBuffer(int maxBufferSizeKB) {
        this.maxBufferSizeKB = maxBufferSizeKB;
    }

    /** Add a segment to the buffer. Evicts oldest if full. */
    public synchronized boolean addSegment(VideoSegment segment) {
        // Evict segments behind playhead to make room
        while (currentSizeKB + segment.getSizeKB() > maxBufferSizeKB && !buffer.isEmpty()) {
            Map.Entry<Double, VideoSegment> oldest = buffer.firstEntry();
            if (oldest.getKey() >= playheadSec) break; // don't evict future segments
            evict(oldest.getValue());
        }

        if (currentSizeKB + segment.getSizeKB() > maxBufferSizeKB) {
            System.out.println("  ⚠️ Buffer full, cannot add: " + segment);
            return false;
        }

        buffer.put(segment.getStartTimeSec(), segment);
        currentSizeKB += segment.getSizeKB();
        System.out.println("  📥 Buffered: " + segment + " (buffer: " + currentSizeKB + "/" + maxBufferSizeKB + "KB)");
        return true;
    }

    private void evict(VideoSegment segment) {
        buffer.remove(segment.getStartTimeSec());
        currentSizeKB -= segment.getSizeKB();
        System.out.println("  🗑️ Evicted: " + segment);
    }

    /** Get the segment at the current playhead position. */
    public synchronized VideoSegment getSegmentAt(double timeSec) {
        Map.Entry<Double, VideoSegment> entry = buffer.floorEntry(timeSec);
        if (entry != null && timeSec < entry.getValue().getEndTimeSec())
            return entry.getValue();
        return null; // buffering needed!
    }

    /** Advance the playhead and check for rebuffering. */
    public synchronized boolean advancePlayhead(double timeSec) {
        this.playheadSec = timeSec;
        VideoSegment seg = getSegmentAt(timeSec);
        if (seg == null) {
            System.out.println("  ⏳ BUFFERING at " + String.format("%.1fs", timeSec) + " — segment not loaded!");
            return false;
        }
        System.out.println("  ▶️ Playing " + String.format("%.1fs", timeSec) + " — " + seg);
        return true;
    }

    /** How many seconds of video are buffered ahead of the playhead? */
    public double getBufferAheadSec() {
        if (buffer.isEmpty()) return 0;
        Map.Entry<Double, VideoSegment> last = buffer.lastEntry();
        return Math.max(0, last.getValue().getEndTimeSec() - playheadSec);
    }

    public void displayBuffer() {
        System.out.println("\n  📊 Buffer (" + currentSizeKB + "/" + maxBufferSizeKB + "KB" +
                ", ahead=" + String.format("%.1fs", getBufferAheadSec()) + "):");
        buffer.values().forEach(s -> {
            String marker = (playheadSec >= s.getStartTimeSec() && playheadSec < s.getEndTimeSec()) ? " ◄ PLAYING" : "";
            System.out.println("    " + s + marker);
        });
    }
}

