import java.util.concurrent.LinkedBlockingQueue;

public class Cpu extends Thread {
    private final Kernel kernel;
    private final MemoryUnit mem;
    private final LinkedBlockingQueue<String> messageQueue;

    public Cpu(Kernel kernel, MemoryUnit mem, Middleware middleware, LinkedBlockingQueue<String> messageQueue) {
        this.kernel = kernel;
        this.mem = mem;
        this.messageQueue = messageQueue;
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
        System.out.println("CPU processando mensagem: " + message);
        mem.saveMessage(message);
        responseMessage(message);
    }
}
