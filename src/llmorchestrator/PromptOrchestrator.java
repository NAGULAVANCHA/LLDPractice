package llmorchestrator;

/** Orchestrator: wires up the handler chain and LLM provider. */
public class PromptOrchestrator {
    private PromptHandler handlerChain;
    private LLMProvider provider;

    public PromptOrchestrator(LLMProvider provider) {
        this.provider = provider;
    }

    public void setHandlerChain(PromptHandler chain) { this.handlerChain = chain; }
    public void setProvider(LLMProvider provider) { this.provider = provider; }

    public PromptContext execute(String userPrompt) {
        System.out.println("\n🤖 Prompt: \"" + userPrompt + "\"");
        PromptContext ctx = new PromptContext(userPrompt);

        // Run through handler chain
        if (handlerChain != null) {
            ctx = handlerChain.handle(ctx);
        }

        if (ctx.isRejected()) {
            System.out.println("  ❌ Request rejected: " + ctx.getRejectionReason());
            return ctx;
        }

        // Send to LLM
        String response = provider.generate(ctx.getSystemInstruction(), ctx.getProcessedPrompt());
        ctx.setResponse(response);
        ctx.setModelName(provider.getName());
        System.out.println("  ✅ " + response);
        return ctx;
    }
}

