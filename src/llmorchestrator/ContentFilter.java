package llmorchestrator;

import java.util.Set;

/** Rejects prompts containing blocked keywords. */
public class ContentFilter extends PromptHandler {
    private final Set<String> blockedWords = Set.of("hack", "exploit", "password", "steal");

    @Override
    protected PromptContext process(PromptContext ctx) {
        String lower = ctx.getProcessedPrompt().toLowerCase();
        for (String word : blockedWords) {
            if (lower.contains(word)) {
                ctx.reject("Blocked content: '" + word + "'");
                System.out.println("  🚫 ContentFilter: REJECTED — contains '" + word + "'");
                return ctx;
            }
        }
        System.out.println("  ✓ ContentFilter: passed");
        return ctx;
    }

    @Override public String getName() { return "ContentFilter"; }
}

