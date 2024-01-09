// Kernel.java
public class Kernel {
    private final MemoryUnit mem;
    private final Cpu cpu;
    private final Middleware middleware;

    private volatile boolean isRunning = true;

    // Inicialização do kernel com suas unidades essenciais
    public Kernel() {
        mem = new MemoryUnit();
        middleware = new Middleware();
        cpu = new Cpu(this, mem, middleware);
    }

    // Iniciar o kernel, inicializando também as threads essenciais
    public void start() {
        System.out.println("Iniciando o kernel...");

        middleware.start();
        cpu.start();

        // Lógica para manter e controlar tarefas
        // (Pode ser implementado conforme necessário)

        System.out.println("Kernel iniciado.");
    }

    // Encerrar o kernel, interrompendo as threads e aguardando a conclusão
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

        // Lógica para encerrar tarefas
        // (Pode ser implementado conforme necessário)

        System.out.println("Kernel encerrado.");
    }

    // Verificar se o kernel está em execução
    public boolean isRunning() {
        return isRunning;
    }

    // Comunicar com o Middleware enviando uma mensagem
    public void communicateWithMiddleware(String message) {
        middleware.sendMessage(new LogMessage(message, middleware));
    }

    // Obter referência para o Middleware
    public Middleware getMiddleware() {
        return middleware;
    }

    // Obter referência para a CPU
    public Cpu getCpu() {
        return cpu;
    }
}
