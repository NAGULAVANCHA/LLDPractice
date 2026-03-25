package llmorchestrator;

/** Factory Pattern: create the right model handler based on prompt needs. */
public interface LLMProvider {
    String generate(String systemPrompt, String userPrompt);
    String getName();
}

