import java.util.concurrent.LinkedBlockingQueue;

public class Kernel {
    private final MemoryUnit mem;
    private final Cpu cpu;
    private final Middleware middleware;
    private final LinkedBlockingQueue<String> messageQueue = new LinkedBlockingQueue<>();
    private volatile boolean isRunning = true;
    

    public Kernel() {
        mem = new MemoryUnit();
        middleware = new Middleware(messageQueue);
        cpu = new Cpu(this, mem, middleware, messageQueue);
    }

    public void start() {
        System.out.println("Iniciando o kernel...");

        middleware.start();
        cpu.start();

        System.out.println("Kernel iniciado.");
    }

    public void stop() {
        System.out.println("Encerrando o kernel...");

        isRunning = false;

        middleware.interrupt();
        cpu.interrupt();

        try {
            middleware.join();
            cpu.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        System.out.println("Kernel encerrado.");
    }

    public boolean isRunning() {
        return isRunning;
    }

    public Middleware getMiddleware() {
        return middleware;
    }

    public Cpu getCpu() {
        return cpu;
    }
}
