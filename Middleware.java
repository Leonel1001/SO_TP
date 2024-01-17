import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;
import java.util.concurrent.Semaphore;

public class Middleware extends Thread {
    private String receivedMessage;
    private final Cpu cpu;
    private final Lock lock;
    private final Semaphore middlewareSemaphore;

    public Middleware(Cpu cpu, Semaphore middlewareSemaphore) {
        this.cpu = cpu;
        this.lock = new ReentrantLock();
        this.middlewareSemaphore = middlewareSemaphore;
    }

    public void sendMessageToMiddleware(String message) {
        System.out.println("Processing message in Middleware: " + message);
        cpu.sendMessageToCPU(message);
    }

   public synchronized String receiveMessageFromInterface() throws InterruptedException {
    lock.lock();
    try {
        while (receivedMessage == null) {
            System.out.println("Waiting for message from Interface...");
            wait();
        }
        String message = receivedMessage;
        receivedMessage = null;
        System.out.println("Received message from Interface: " + message);
        return message;
    } finally {
        lock.unlock();
    }
}

public synchronized void sendManualMessageFromInterface(String message) {
    lock.lock();
    try {
        receivedMessage = message;
        System.out.println("Sent manual message to Interface: " + message);
        notifyAll(); 
    } finally {
        lock.unlock();
    }
}


    public void sendManualCpuResponse(String response) {
        cpu.sendMessageToCPU(response);
    }

    public String receiveCpuResponse() {
        return cpu.getLastSentResponse();
    }

    public void run() {
        while (!Thread.interrupted()) {
            try {
                getMessageFromInterface();
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                e.printStackTrace();
            }
        }
    }

    private String getMessageFromInterface() throws InterruptedException {
        lock.lock();
        try {
            while (receivedMessage == null) {
                wait();
            }
            return receivedMessage;
        } finally {
            lock.unlock();
        }
    }

    
}
