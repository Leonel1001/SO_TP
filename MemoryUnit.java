import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class MemoryUnit {
    private final static Object lock = new Object();
    private static List<LogMessage> logMessages;
    private static final String LOG_FILE_PATH = "log_messages.txt";

    static {
        loadLogMessages();
    }

    public MemoryUnit() {
        // Ensure logMessages is initialized
        if (logMessages == null) {
            logMessages = new ArrayList<>();
        }
    }

    public static void addLogMessage(LogMessage logMessage) {
        synchronized (lock) {
            if (logMessage != null) {
                logMessages.add(logMessage);
                saveLogMessages();
            }
        }
    }

    public static List<LogMessage> getLogMessages() {
        synchronized (lock) {
            return new ArrayList<>(logMessages); 
        }
    }

    private static void loadLogMessages() {
    }

    private static void saveLogMessages() {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(LOG_FILE_PATH))) {
            for (LogMessage logMessage : logMessages) {
                writer.write(logMessage.getMessage());
                writer.newLine();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
