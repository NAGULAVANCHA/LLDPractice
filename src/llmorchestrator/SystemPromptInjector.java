package llmorchestrator;

/** Prepends a system instruction to the prompt. */
public class SystemPromptInjector extends PromptHandler {
    private final String systemPrompt;

    public SystemPromptInjector(String systemPrompt) { this.systemPrompt = systemPrompt; }

    @Override
    protected PromptContext process(PromptContext ctx) {
        ctx.setSystemInstruction(systemPrompt);
        System.out.println("  ✓ SystemPromptInjector: added system instruction");
        return ctx;
    }

    @Override public String getName() { return "SystemPromptInjector"; }
}

