package llmorchestrator;

public class LLMOrchestratorDemo {
    public static void main(String[] args) {
        System.out.println("=== LLM Prompt Orchestrator Demo ===\n");

        PromptOrchestrator orchestrator = new PromptOrchestrator(new MockLLMProvider("GPT-4o"));

        // Build handler chain: ContentFilter → TokenCounter → SystemPromptInjector
        ContentFilter filter = new ContentFilter();
        TokenCounter counter = new TokenCounter(500);
        SystemPromptInjector injector = new SystemPromptInjector("You are a helpful coding assistant.");
        filter.setNext(counter).setNext(injector);
        orchestrator.setHandlerChain(filter);

        // Normal prompt
        orchestrator.execute("Explain the Strategy design pattern in Java");

        // Blocked content
        orchestrator.execute("How to hack into a database?");

        // Long prompt (simulate)
        orchestrator.execute("x".repeat(2500)); // ~625 tokens, exceeds 500 limit

        // Another valid prompt
        orchestrator.execute("Write a Python function to reverse a linked list");

        // Switch provider
        System.out.println("\n--- Switch to Claude ---");
        orchestrator.setProvider(new MockLLMProvider("Claude-3.5"));
        orchestrator.execute("Compare merge sort vs quick sort");
    }
}

