package llmorchestrator;

public class MockLLMProvider implements LLMProvider {
    private final String modelName;

    public MockLLMProvider(String modelName) { this.modelName = modelName; }

    @Override
    public String generate(String systemPrompt, String userPrompt) {
        return "[" + modelName + "] Response to: \"" + userPrompt.substring(0, Math.min(50, userPrompt.length())) + "...\"";
    }

    @Override public String getName() { return modelName; }
}

