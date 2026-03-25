package logger;

import java.util.ArrayList;
import java.util.List;

/** Simulates file logging by storing in a list. */
public class FileHandler extends LogHandler {
    private final String fileName;
    private final List<String> fileContent = new ArrayList<>();

    public FileHandler(LogLevel level, String fileName) {
        super(level);
        this.fileName = fileName;
    }

    @Override
    protected void write(LogMessage message) {
        String line = message.toString();
        fileContent.add(line);
        System.out.println("  [FILE:" + fileName + "] " + line);
    }

    public List<String> getFileContent() { return fileContent; }
}

