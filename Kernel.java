// Kernel.java
public class Kernel {
    private final MemoryUnit mem;
    private final Cpu cpu;
    private final Middleware middleware;

    private volatile boolean isRunning = true;

    public Kernel() {
        mem = new MemoryUnit();
        middleware = new Middleware();
        cpu = new Cpu(this, mem, middleware);
    }

    public void start() {
        System.out.println("Iniciando o kernel...");

        middleware.start();
        cpu.start();

        // Lógica para manter e controlar tarefas

        System.out.println("Kernel iniciado.");
    }

    public void stop() {
        System.out.println("Encerrando o kernel...");

        isRunning = false;

        // Interrupção das threads
        middleware.interrupt();
        cpu.interrupt();

        // Aguarde a conclusão das threads
        try {
            middleware.join();
            cpu.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        // Lógica para encerrar tarefas

        System.out.println("Kernel encerrado.");
    }

    public boolean isRunning() {
        return isRunning;
    }

    public void communicateWithMiddleware(String message) {
        middleware.sendMessage(new LogMessage(message, middleware));
    }

    // Método para obter referência para Middleware
    public Middleware getMiddleware() {
        return middleware;
    }

    // Método para obter referência para Cpu
    public Cpu getCpu() {
        return cpu;
    }
}
