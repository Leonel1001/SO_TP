import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.concurrent.Semaphore;

public class MemoryUnit extends Thread {
    private final Middleware middleware;
    private final String fileName = "Memory_logs.txt";
    private FileWriter fileWriter;
    private final Semaphore semaphore;

    public MemoryUnit(Middleware middleware, Semaphore semaphore) {
        this.middleware = middleware;
        this.semaphore = semaphore;

        try {
            fileWriter = new FileWriter(fileName, true);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void run() {
        while (true) {
            try {
                String interfaceMessage = middleware.receiveMessageFromInterface();
                if (interfaceMessage != null) {
                    writeToFile("Interface Message: " + interfaceMessage);
                }

                String cpuResponse = middleware.receiveCpuResponse();
                if (cpuResponse != null) {
                    writeToFile("CPU Response: " + cpuResponse);
                }

                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
                Thread.currentThread().interrupt();
            }
        }
    }

    private void writeToFile(String message) {
        try {
            String formattedLog = "[" + LocalDateTime.now() + "] " + message;
            fileWriter.write(formattedLog + "\n");
            fileWriter.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void sendMessageToMemoryUnit(String message) {
        // Implemente conforme necessário
    }
}
