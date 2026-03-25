package llmorchestrator;

/** Estimates token count and rejects if too long. */
public class TokenCounter extends PromptHandler {
    private final int maxTokens;

    public TokenCounter(int maxTokens) { this.maxTokens = maxTokens; }

    @Override
    protected PromptContext process(PromptContext ctx) {
        // Rough estimate: 1 token ≈ 4 characters
        int tokens = ctx.getProcessedPrompt().length() / 4;
        ctx.setTokenCount(tokens);

        if (tokens > maxTokens) {
            ctx.reject("Token limit exceeded: " + tokens + " > " + maxTokens);
            System.out.println("  🚫 TokenCounter: REJECTED — " + tokens + " tokens > limit " + maxTokens);
            return ctx;
        }
        System.out.println("  ✓ TokenCounter: " + tokens + " tokens (limit " + maxTokens + ")");
        return ctx;
    }

    @Override public String getName() { return "TokenCounter"; }
}

