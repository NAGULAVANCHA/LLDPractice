# Task Scheduler / Thread Pool — Complete Guide

---

## Part 1: Understanding the Problem

Build a custom thread pool and task scheduler — like a simplified `ExecutorService` / cron system.

### Requirements
- ✓ Fixed pool of worker threads that pull tasks from a shared queue
- ✓ FIFO mode (first-come-first-served) and Priority mode
- ✓ Delayed / scheduled task execution
- ✓ Recurring tasks (like cron jobs)
- ✓ Graceful shutdown: stop accepting, finish existing tasks

### Why This Is Asked in 2026
Thread pools are the backbone of every backend system. Interviewers want to see if you understand:
- Producer-Consumer pattern (callers submit, workers execute)
- `BlockingQueue` and thread-safe data structures
- Thread lifecycle (start, run loop, shutdown)
- Resource management (bounded workers, graceful shutdown)

---

## Part 2: The Key Insight — Producer-Consumer with BlockingQueue

```
Callers (Producers)          Task Queue             Workers (Consumers)
┌────────┐                 ┌──────────┐           ┌──────────┐
│ submit │ ──offer()──→   │ Task-A   │ ←─take()─ │ Worker-1 │ → execute
│ submit │ ──offer()──→   │ Task-B   │ ←─take()─ │ Worker-2 │ → execute
│ submit │ ──offer()──→   │ Task-C   │ ←─take()─ │ Worker-3 │ → execute
└────────┘                 │ Task-D   │           └──────────┘
                           └──────────┘
                        BlockingQueue
                     (thread-safe, blocks
                      when empty)
```

**`BlockingQueue.take()`** blocks the calling thread until a task is available — no busy-waiting, no CPU waste.

---

## Part 3: The Code — Explained

### Task Interface
```java
public interface Task {
    String getName();
    void execute();
    int getPriority();  // higher = more urgent
}
```

### Worker — The Consumer Thread
```java
public class Worker extends Thread {
    private final ThreadPool pool;
    private volatile boolean running = true;

    @Override
    public void run() {
        while (running) {
            Task task = pool.takeTask();  // BLOCKS until task available
            if (task == null) break;
            task.execute();
        }
    }

    public void shutdown() {
        running = false;
        this.interrupt();  // unblock from take()
    }
}
```

**`volatile boolean running`** — ensures visibility across threads. When main thread sets `running = false`, the worker sees it.

### ThreadPool — The Manager
```java
public class ThreadPool {
    private final List<Worker> workers;
    private final BlockingQueue<Task> taskQueue;
    private volatile boolean isShutdown = false;

    public ThreadPool(int poolSize, boolean priorityMode) {
        if (priorityMode)
            taskQueue = new PriorityBlockingQueue<>(11,
                (a, b) -> Integer.compare(b.getPriority(), a.getPriority()));
        else
            taskQueue = new LinkedBlockingQueue<>();

        // Create and start all workers
        for (int i = 0; i < poolSize; i++) {
            Worker w = new Worker("Worker-" + i, this);
            workers.add(w);
            w.start();
        }
    }

    public void submit(Task task) {
        if (isShutdown) { "Rejected!"; return; }
        taskQueue.offer(task);
    }

    Task takeTask() throws InterruptedException {
        return taskQueue.poll(500, TimeUnit.MILLISECONDS);
    }

    public void shutdown() {
        isShutdown = true;               // stop accepting
        while (!taskQueue.isEmpty()) {}  // wait for queue to drain
        for (Worker w : workers) w.shutdown();  // stop workers
    }
}
```

### FIFO vs Priority Mode

| Mode | Queue Type | Behavior |
|---|---|---|
| FIFO | `LinkedBlockingQueue` | Tasks execute in submission order |
| Priority | `PriorityBlockingQueue` | Higher priority tasks jump ahead |

### ScheduledThreadPool — Delayed & Recurring
```java
public class ScheduledThreadPool {
    private final ThreadPool pool;
    private final ScheduledExecutorService scheduler;

    public void schedule(Task task, long delayMs) {
        scheduler.schedule(() -> pool.submit(task), delayMs, TimeUnit.MILLISECONDS);
    }

    public void scheduleAtFixedRate(String name, Runnable work, long delay, long period) {
        scheduler.scheduleAtFixedRate(
            () -> pool.submit(new SimpleTask(name, work)),
            delay, period, TimeUnit.MILLISECONDS);
    }
}
```

---

## Part 4: Data Flow

```
ThreadPool(3 workers, FIFO):

1. submit(Task-A)  → queue: [A]      → Worker-1 takes A, executes
2. submit(Task-B)  → queue: [B]      → Worker-2 takes B, executes
3. submit(Task-C)  → queue: [C]      → Worker-3 takes C, executes
4. submit(Task-D)  → queue: [D]      → all workers busy, D waits
5. Worker-1 finishes A → takes D, executes
6. submit(Task-E)  → queue: [E]      → Worker-2 finishes B → takes E

shutdown():
  → isShutdown = true (no new tasks)
  → wait for queue to empty
  → interrupt all workers
  → workers exit run() loop
```

---

## Part 5: Key Concurrency Concepts

| Concept | Where Used | Why |
|---|---|---|
| **`volatile`** | `isShutdown`, `running` | Cross-thread visibility without locks |
| **`BlockingQueue`** | Task queue | Thread-safe, blocks on empty (no busy-wait) |
| **`PriorityBlockingQueue`** | Priority mode | Auto-sorted, thread-safe priority queue |
| **`Thread.interrupt()`** | Shutdown | Unblocks worker waiting on `take()` |
| **Graceful shutdown** | `shutdown()` | Finish existing work before stopping |

---

## Part 6: Follow-Up Questions

| Question | Answer |
|---|---|
| What if a task throws an exception? | Catch in Worker, log error, continue to next task. Don't let one bad task kill a thread. |
| Dynamic pool sizing? | Track idle workers. If all busy and queue grows, spawn new workers up to a max. Like `CachedThreadPool`. |
| Task timeout? | Wrap execution in a `Future.get(timeout)`. Cancel if it takes too long. |
| How does Java's ExecutorService work? | Same design! `ThreadPoolExecutor` uses `BlockingQueue` + worker threads. Our code is a simplified version. |
| Task dependencies? | Build a DAG of tasks. Only submit a task when all its dependencies are complete (like a build system). |

---

## Part 7: Patterns Recap

| Pattern | Where & Why |
|---|---|
| **Producer-Consumer** | Callers submit, workers consume from queue |
| **Strategy** | FIFO vs Priority via different queue types |
| **Command** | Task wraps a unit of work |
| **Thread Safety** | `volatile`, `BlockingQueue`, `synchronized` |
| **Graceful Lifecycle** | Start workers → accept tasks → drain queue → shutdown |

---

📁 **Source code:** `src/taskscheduler/`

