package videobuffer;

public class VideoBufferDemo {
    public static void main(String[] args) {
        System.out.println("=== Video Buffer / Segment Manager Demo ===\n");

        VideoBuffer buffer = new VideoBuffer(500); // 500KB max buffer

        // Simulate loading segments (each 2 seconds, varying quality/size)
        System.out.println("--- Loading segments ---");
        buffer.addSegment(new VideoSegment(1, 0.0, 2.0, "1080p", 120));
        buffer.addSegment(new VideoSegment(2, 2.0, 2.0, "1080p", 120));
        buffer.addSegment(new VideoSegment(3, 4.0, 2.0, "720p", 80));
        buffer.addSegment(new VideoSegment(4, 6.0, 2.0, "720p", 80));
        buffer.addSegment(new VideoSegment(5, 8.0, 2.0, "1080p", 120));
        buffer.displayBuffer();

        // Simulate playback
        System.out.println("\n--- Playback ---");
        buffer.advancePlayhead(0.0);
        buffer.advancePlayhead(1.5);
        buffer.advancePlayhead(3.0);
        buffer.advancePlayhead(5.0);

        // Try to buffer more — should evict old segments
        System.out.println("\n--- Adding more segments (triggers eviction) ---");
        buffer.addSegment(new VideoSegment(6, 10.0, 2.0, "1080p", 120));
        buffer.displayBuffer();

        buffer.advancePlayhead(7.0);
        buffer.advancePlayhead(9.0);

        // Seek forward — causes rebuffering
        System.out.println("\n--- Seek to 15.0s (not buffered) ---");
        buffer.advancePlayhead(15.0);

        System.out.println("\n--- Load segment for seek position ---");
        buffer.addSegment(new VideoSegment(8, 14.0, 2.0, "480p", 50));
        buffer.advancePlayhead(15.0);
        buffer.displayBuffer();
    }
}

