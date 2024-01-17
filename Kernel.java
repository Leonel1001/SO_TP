import java.util.concurrent.Semaphore;

public class Kernel {
    private MemoryUnit mem;
    private Cpu cpu;
    private Middleware middleware;

    private volatile boolean isRunning = true;
    private final Semaphore memSemaphore = new Semaphore(1);
    private final Semaphore middlewareSemaphore = new Semaphore(1);

    public Kernel() {
        cpu = new Cpu(this, null, null, memSemaphore);
        middleware = new Middleware(cpu, middlewareSemaphore);
        mem = new MemoryUnit(middleware, memSemaphore);

        cpu.setMemoryUnit(mem);
        cpu.setMiddleware(middleware);
    }

    public void start() {
        System.out.println("Iniciando o kernel...");
        mem.start();
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
