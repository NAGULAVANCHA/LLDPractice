import java.util.Scanner;

/**
 * LLD Practice — Master Runner (18 Problems, 3 Tiers)
 */
public class Main {
    public static void main(String[] args) throws Exception {
        System.out.println("╔═══════════════════════════════════════════════════════════╗");
        System.out.println("║       LLD Interview Practice Suite (2026 Edition)        ║");
        System.out.println("╠═══════════════════════════════════════════════════════════╣");
        System.out.println("║  ── Tier 1: System Design Crossovers ──                  ║");
        System.out.println("║  1.  Rate Limiter        (Token Bucket, Sliding Window)  ║");
        System.out.println("║  2.  Payment Gateway     (Strategy, State, CoR)          ║");
        System.out.println("║  3.  Ride-Sharing Uber   (Strategy, State, Observer)     ║");
        System.out.println("║  4.  Task Scheduler      (Thread Pool, Queues)           ║");
        System.out.println("║  5.  Pub-Sub Queue       (Producer/Consumer)             ║");
        System.out.println("║  6.  BookMyShow          (Concurrency, Atomic Txns)      ║");
        System.out.println("║  7.  LRU Cache           (HashMap + DLL)                 ║");
        System.out.println("║  8.  LFU Cache           (3 HashMaps + minFreq)          ║");
        System.out.println("║  9.  Splitwise           (Strategy, Debt Simplify)       ║");
        System.out.println("║  ── Tier 2: Advanced OOP & Modern Architecture ──        ║");
        System.out.println("║  10. Vector Database     (KNN Search, Cosine Sim)        ║");
        System.out.println("║  11. LLM Orchestrator    (Prompt Pipeline, CoR)          ║");
        System.out.println("║  12. Video Buffer        (Sliding Window, Segments)      ║");
        System.out.println("║  13. Logging Framework   (Singleton, CoR)                ║");
        System.out.println("║  14. Elevator System     (SCAN Algorithm, State)         ║");
        System.out.println("║  15. Vending Machine     (State Pattern)                 ║");
        System.out.println("║  ── Tier 3: Foundational Classics ──                     ║");
        System.out.println("║  16. Notification System (Observer, Strategy)            ║");
        System.out.println("║  17. Parking Lot         (Singleton, Strategy)           ║");
        System.out.println("║  18. File System         (Composite, Recursion)          ║");
        System.out.println("║  0.  Run ALL demos                                       ║");
        System.out.println("╚═══════════════════════════════════════════════════════════╝");
        System.out.print("Choose (0-18): ");

        Scanner sc = new Scanner(System.in);
        int choice = sc.nextInt();

        switch (choice) {
            case 1  -> { try { ratelimiter.RateLimiterDemo.main(args); } catch (Exception e) { e.printStackTrace(); } }
            case 2  -> paymentgateway.PaymentGatewayDemo.main(args);
            case 3  -> ridesharing.RideSharingDemo.main(args);
            case 4  -> { try { taskscheduler.TaskSchedulerDemo.main(args); } catch (Exception e) { e.printStackTrace(); } }
            case 5  -> pubsub.PubSubDemo.main(args);
            case 6  -> bookmyshow.BookMyShowDemo.main(args);
            case 7  -> lrucache.LRUCache.main(args);
            case 8  -> lfucache.LFUCache.main(args);
            case 9  -> splitwise.SplitwiseDemo.main(args);
            case 10 -> vectordb.VectorDBDemo.main(args);
            case 11 -> llmorchestrator.LLMOrchestratorDemo.main(args);
            case 12 -> videobuffer.VideoBufferDemo.main(args);
            case 13 -> logger.Logger.main(args);
            case 14 -> elevator.ElevatorDemo.main(args);
            case 15 -> vendingmachine.VendingMachineDemo.main(args);
            case 16 -> notification.NotificationService.main(args);
            case 17 -> parkinglot.ParkingLotDemo.main(args);
            case 18 -> filesystem.Directory.main(args);
            case 0  -> {
                String[] demos = {
                    "Rate Limiter", "Payment Gateway", "Ride-Sharing (Uber)",
                    "Task Scheduler", "Pub-Sub Queue", "BookMyShow",
                    "LRU Cache", "LFU Cache", "Splitwise",
                    "Vector Database", "LLM Orchestrator", "Video Buffer",
                    "Logging Framework", "Elevator", "Vending Machine",
                    "Notification System", "Parking Lot", "File System"
                };
                Runnable[] runners = {
                    () -> { try { ratelimiter.RateLimiterDemo.main(args); } catch (Exception e) { e.printStackTrace(); } },
                    () -> paymentgateway.PaymentGatewayDemo.main(args),
                    () -> ridesharing.RideSharingDemo.main(args),
                    () -> { try { taskscheduler.TaskSchedulerDemo.main(args); } catch (Exception e) { e.printStackTrace(); } },
                    () -> pubsub.PubSubDemo.main(args),
                    () -> bookmyshow.BookMyShowDemo.main(args),
                    () -> lrucache.LRUCache.main(args),
                    () -> lfucache.LFUCache.main(args),
                    () -> splitwise.SplitwiseDemo.main(args),
                    () -> vectordb.VectorDBDemo.main(args),
                    () -> llmorchestrator.LLMOrchestratorDemo.main(args),
                    () -> videobuffer.VideoBufferDemo.main(args),
                    () -> logger.Logger.main(args),
                    () -> elevator.ElevatorDemo.main(args),
                    () -> vendingmachine.VendingMachineDemo.main(args),
                    () -> notification.NotificationService.main(args),
                    () -> parkinglot.ParkingLotDemo.main(args),
                    () -> filesystem.Directory.main(args),
                };
                for (int i = 0; i < runners.length; i++) {
                    System.out.println("\n\n" + "=".repeat(60));
                    System.out.println("  DEMO " + (i + 1) + ": " + demos[i]);
                    System.out.println("=".repeat(60));
                    runners[i].run();
                }
            }
            default -> System.out.println("Invalid choice!");
        }
    }
}