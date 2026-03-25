package llmorchestrator;

/** Context object passed through the handler chain. */
public class PromptContext {
    private String originalPrompt;
    private String processedPrompt;
    private String systemInstruction;
    private String modelName;
    private String response;
    private boolean rejected;
    private String rejectionReason;
    private int tokenCount;

    public PromptContext(String prompt) {
        this.originalPrompt = prompt;
        this.processedPrompt = prompt;
        this.systemInstruction = "";
        this.rejected = false;
        this.tokenCount = 0;
    }

    public void reject(String reason) {
        this.rejected = true;
        this.rejectionReason = reason;
    }

    public String getOriginalPrompt() { return originalPrompt; }
    public String getProcessedPrompt() { return processedPrompt; }
    public void setProcessedPrompt(String p) { this.processedPrompt = p; }
    public String getSystemInstruction() { return systemInstruction; }
    public void setSystemInstruction(String s) { this.systemInstruction = s; }
    public String getModelName() { return modelName; }
    public void setModelName(String m) { this.modelName = m; }
    public String getResponse() { return response; }
    public void setResponse(String r) { this.response = r; }
    public boolean isRejected() { return rejected; }
    public String getRejectionReason() { return rejectionReason; }
    public int getTokenCount() { return tokenCount; }
    public void setTokenCount(int t) { this.tokenCount = t; }
}

