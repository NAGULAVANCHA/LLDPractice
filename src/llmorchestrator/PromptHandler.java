package llmorchestrator;

/** Chain of Responsibility: each handler transforms/validates the prompt. */
public abstract class PromptHandler {
    private PromptHandler next;

    public PromptHandler setNext(PromptHandler next) {
        this.next = next;
        return next;
    }

    public PromptContext handle(PromptContext ctx) {
        ctx = process(ctx);
        if (ctx.isRejected()) return ctx;
        if (next != null) return next.handle(ctx);
        return ctx;
    }

    protected abstract PromptContext process(PromptContext ctx);
    public abstract String getName();
}

