import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.Semaphore;

public class Cpu extends Thread {
    private final Kernel kernel;
    private final MemoryUnit mem;
    private final LinkedBlockingQueue<String> messageQueue;
    private final Semaphore semaphore;

    public Cpu(Kernel kernel, MemoryUnit mem, Middleware middleware, LinkedBlockingQueue<String> messageQueue) {
        this.kernel = kernel;
        this.mem = mem;
        this.messageQueue = messageQueue;
        this.semaphore = new Semaphore(1);
    }

    public void responseMessage(String message) {
        String response = "Satélite responde a " + message;
        System.out.println(response);
        mem.saveMessage(response);
    }

    @Override
    public void run() {
        while (kernel.isRunning()) {
            try {
                String message = messageQueue.take();
                processMessage(message);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    private void processMessage(String message) {
        try {
            semaphore.acquire();

            System.out.println("CPU processando mensagem: " + message);
            mem.saveMessage(message);
            responseMessage(message);

        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            semaphore.release();
        }
    }
}
