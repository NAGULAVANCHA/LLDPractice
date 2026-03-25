# LLM Prompt Orchestrator — Complete Guide

---

## Part 1: Understanding the Problem

A pipeline that validates, transforms, and routes user prompts to LLM providers. Like a middleware chain for AI requests.

### Requirements
- ✓ Chain of Responsibility: ContentFilter → TokenCounter → SystemPromptInjector
- ✓ Factory/Strategy: swap LLM providers (GPT-4, Claude, Llama)
- ✓ Content moderation (block harmful prompts)
- ✓ Token counting and limit enforcement
- ✓ System prompt injection

---

## Part 2: Key Design

### Chain of Responsibility — Prompt Pipeline
```
User Prompt: "Explain Strategy pattern"
    ↓
ContentFilter → no blocked words → ✓ PASS
    ↓
TokenCounter → 8 tokens < 500 limit → ✓ PASS
    ↓
SystemPromptInjector → adds "You are a helpful coding assistant."
    ↓
LLMProvider.generate(systemPrompt, userPrompt) → response
```

```
User Prompt: "How to hack a database"
    ↓
ContentFilter → "hack" is blocked → ❌ REJECTED
    ↓ (chain stops — ctx.isRejected())
Never reaches LLM
```

### PromptContext — Data Object Through Chain
```java
class PromptContext {
    String originalPrompt, processedPrompt;
    String systemInstruction, modelName, response;
    boolean rejected; String rejectionReason;
    int tokenCount;
}
```
Each handler reads/modifies the context, then passes to next.

### Factory — LLM Provider Swap
```java
interface LLMProvider {
    String generate(String systemPrompt, String userPrompt);
}

orchestrator.setProvider(new MockLLMProvider("GPT-4o"));
orchestrator.setProvider(new MockLLMProvider("Claude-3.5"));
```

---

## Part 3: Follow-Up Questions

| Question | Answer |
|---|---|
| Rate limiting per user? | Add `RateLimitHandler` to the chain. Uses token bucket per userId. |
| Caching? | Add `CacheHandler` before LLM call. Hash the prompt, check cache, return if hit. |
| Streaming responses? | Return `Iterator<String>` instead of `String`. Stream tokens as they arrive. |
| Multi-model routing? | Router handler picks model based on prompt complexity (simple → small model, complex → GPT-4). |
| Cost tracking? | TokenCounter stores counts. After response, multiply by price-per-token per model. |

---

📁 **Source code:** `src/llmorchestrator/`

