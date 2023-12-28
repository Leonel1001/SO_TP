package src.java.app;

public class Kernel {
    private MemoryUnit mem;
    private Cpu cpu;
    private Middleware middleware;

    private volatile boolean isRunning = true;

    public Kernel() {
        mem = new MemoryUnit();
        cpu = new Cpu(mem, this);
        middleware = new Middleware(cpu);
    }

    public void start() {
        System.out.println("Iniciando o kernel...");

        middleware.start(); // Inicie o Middleware primeiro
        cpu.start();

        // Lógica para manter e controlar tarefas

        System.out.println("Kernel iniciado.");
    }

    public void stop() {
        System.out.println("Encerrando o kernel...");

        isRunning = false;

        // Aguarde a conclusão das threads
        try {
            cpu.join();
            middleware.join();
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
        // Lógica para comunicação com o Middleware
    }
}
