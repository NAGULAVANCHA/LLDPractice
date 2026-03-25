import java.util.Scanner;

/**
 * LLD Practice — Master Runner (21 Problems, 3 Tiers)
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
        System.out.println("║  4.  Live Scoreboard     (Observer, Read-Heavy)          ║");
        System.out.println("║  5.  Task Scheduler      (Thread Pool, Queues)           ║");
        System.out.println("║  6.  Pub-Sub Queue       (Producer/Consumer)             ║");
        System.out.println("║  7.  BookMyShow          (Concurrency, Atomic Txns)      ║");
        System.out.println("║  8.  Cloud Allocator     (Strategy, Health Checks)       ║");
        System.out.println("║  9.  LRU Cache           (HashMap + DLL)                 ║");
        System.out.println("║  10. LFU Cache           (3 HashMaps + minFreq)          ║");
        System.out.println("║  11. Splitwise           (Strategy, Debt Simplify)       ║");
        System.out.println("║  ── Tier 2: Advanced OOP & Modern Architecture ──        ║");
        System.out.println("║  12. Vector Database     (KNN Search, Cosine Sim)        ║");
        System.out.println("║  13. LLM Orchestrator    (Prompt Pipeline, CoR)          ║");
        System.out.println("║  14. Video Buffer        (Sliding Window, Segments)      ║");
        System.out.println("║  15. Logging Framework   (Singleton, CoR)                ║");
        System.out.println("║  16. Elevator System     (SCAN Algorithm, State)         ║");
        System.out.println("║  17. Vending Machine     (State Pattern)                 ║");
        System.out.println("║  ── Tier 3: Foundational Classics ──                     ║");
        System.out.println("║  18. Notification System (Observer, Strategy)            ║");
        System.out.println("║  19. Parking Lot         (Singleton, Strategy)           ║");
        System.out.println("║  20. File System         (Composite, Recursion)          ║");
        System.out.println("║  21. Shopping Cart       (Strategy, Pricing)             ║");
        System.out.println("║  0.  Run ALL demos                                       ║");
        System.out.println("╚═══════════════════════════════════════════════════════════╝");
        System.out.print("Choose (0-21): ");

        Scanner sc = new Scanner(System.in);
        int choice = sc.nextInt();

        switch (choice) {
            case 1  -> { try { ratelimiter.RateLimiterDemo.main(args); } catch (Exception e) { e.printStackTrace(); } }
            case 2  -> paymentgateway.PaymentGatewayDemo.main(args);
            case 3  -> ridesharing.RideSharingDemo.main(args);
            case 4  -> scoreboard.ScoreboardDemo.main(args);
            case 5  -> { try { taskscheduler.TaskSchedulerDemo.main(args); } catch (Exception e) { e.printStackTrace(); } }
            case 6  -> pubsub.PubSubDemo.main(args);
            case 7  -> bookmyshow.BookMyShowDemo.main(args);
            case 8  -> cloudresource.CloudAllocatorDemo.main(args);
            case 9  -> lrucache.LRUCache.main(args);
            case 10 -> lfucache.LFUCache.main(args);
            case 11 -> splitwise.SplitwiseDemo.main(args);
            case 12 -> vectordb.VectorDBDemo.main(args);
            case 13 -> llmorchestrator.LLMOrchestratorDemo.main(args);
            case 14 -> videobuffer.VideoBufferDemo.main(args);
            case 15 -> logger.Logger.main(args);
            case 16 -> elevator.ElevatorDemo.main(args);
            case 17 -> vendingmachine.VendingMachineDemo.main(args);
            case 18 -> notification.NotificationService.main(args);
            case 19 -> parkinglot.ParkingLotDemo.main(args);
            case 20 -> filesystem.Directory.main(args);
            case 21 -> shoppingcart.ShoppingCartDemo.main(args);
            case 0  -> {
                String[] demos = {
                    "Rate Limiter", "Payment Gateway", "Ride-Sharing (Uber)",
                    "Live Scoreboard", "Task Scheduler", "Pub-Sub Queue",
                    "BookMyShow", "Cloud Allocator", "LRU Cache",
                    "LFU Cache", "Splitwise", "Vector Database",
                    "LLM Orchestrator", "Video Buffer", "Logging Framework",
                    "Elevator", "Vending Machine", "Notification System",
                    "Parking Lot", "File System", "Shopping Cart"
                };
                Runnable[] runners = {
                    () -> { try { ratelimiter.RateLimiterDemo.main(args); } catch (Exception e) { e.printStackTrace(); } },
                    () -> paymentgateway.PaymentGatewayDemo.main(args),
                    () -> ridesharing.RideSharingDemo.main(args),
                    () -> scoreboard.ScoreboardDemo.main(args),
                    () -> { try { taskscheduler.TaskSchedulerDemo.main(args); } catch (Exception e) { e.printStackTrace(); } },
                    () -> pubsub.PubSubDemo.main(args),
                    () -> bookmyshow.BookMyShowDemo.main(args),
                    () -> cloudresource.CloudAllocatorDemo.main(args),
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
                    () -> shoppingcart.ShoppingCartDemo.main(args),
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