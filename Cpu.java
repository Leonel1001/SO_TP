public class Cpu extends Thread {
    private final Kernel kernel;
    private final MemoryUnit mem;
    private Middleware middleware;

    public Cpu(Kernel kernel, MemoryUnit mem, Middleware middleware) {
        this.kernel = kernel;
        this.mem = mem;
        this.middleware = middleware;
    }

    @Override
    public void run() {
        while (kernel.isRunning()) {
            // Lógica de gestão, escalonamento e execução de tarefas
            synchronized (mem) {
                // int data = mem.readData();
                // Processamento em tempo real
                // ...

                // Verifica se há resposta do Middleware
                synchronized (middleware) {
                    LogMessage response = middleware.checkForResponse();
                    if (response != null) {
                        System.out.println("CPU recebeu resposta do Middleware: " + response.getMessage());
                    }
                }
            }
        }
    }

    // Método para configurar o Middleware
    public void setMiddleware(Middleware middleware) {
        // Sincroniza o acesso ao middleware ao configurá-lo
        synchronized (this) {
            this.middleware = middleware;
        }
    }
}