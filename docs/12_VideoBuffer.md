# Video Buffer / Segment Manager — Complete Guide

---

## Part 1: Understanding the Problem

A video player buffer that manages pre-fetched segments, handles seeks, detects rebuffering, and evicts old segments — like YouTube/Netflix's player core.

### Requirements
- ✓ Sliding window buffer with max size (KB)
- ✓ Add segments, evict old ones when full
- ✓ Playback: advance playhead, detect rebuffering
- ✓ Seek: jump to unbuffered position triggers rebuffer
- ✓ Buffer-ahead measurement

---

## Part 2: Key Design

### TreeMap — Sorted Segment Store
```java
TreeMap<Double, VideoSegment> buffer; // key = startTimeSec

// O(log n) lookups:
buffer.floorEntry(timeSec)  → segment containing this time
buffer.firstEntry()          → oldest segment (for eviction)
buffer.lastEntry()           → newest segment (for buffer-ahead calc)
```

### Eviction Strategy
```
Buffer full (500KB), need to add new segment:
  → Find oldest segment (buffer.firstEntry())
  → Only evict if it's BEHIND the playhead (already watched)
  → Remove it, free space
  → Add new segment
```

### Rebuffering Detection
```java
boolean advancePlayhead(double timeSec) {
    VideoSegment seg = getSegmentAt(timeSec);
    if (seg == null) {
        "⏳ BUFFERING — segment not loaded!";
        return false;  // player must pause and wait
    }
    "▶️ Playing";
    return true;
}
```

---

## Part 3: Data Flow

```
1. Load segments 0-10s (5 segments × 2s each)
   Buffer: [0-2s][2-4s][4-6s][6-8s][8-10s] = 520KB

2. Play at 0.0s → Seg#1 [0-2s] ▶️
3. Play at 5.0s → Seg#3 [4-6s] ▶️

4. Add Seg#6 [10-12s] → buffer full!
   → Evict Seg#1 [0-2s] (behind playhead at 5.0s)
   → Buffer: [2-4s][4-6s][6-8s][8-10s][10-12s]

5. Seek to 15.0s → getSegmentAt(15.0) = null
   → ⏳ BUFFERING! → need to fetch segment for 14-16s

6. Load Seg#8 [14-16s] → advancePlayhead(15.0) → ▶️ Playing
```

---

## Part 4: Follow-Up Questions

| Question | Answer |
|---|---|
| Adaptive bitrate? | Monitor bandwidth. If slow, request lower quality segments (480p instead of 1080p). |
| Pre-fetching? | Background thread loads N segments ahead of playhead automatically. |
| Live streaming? | Segments arrive in real-time. Buffer window is small (2-5 seconds). No seeking backward. |
| DRM? | Decrypt segments on-the-fly before adding to buffer. Decryption key fetched from license server. |

---

📁 **Source code:** `src/videobuffer/`

