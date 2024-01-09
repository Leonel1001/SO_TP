
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
            int data = mem.readData();
            // Processamento em tempo real
            // ...

            // Verifica se há resposta do Middleware
            LogMessage response = middleware.checkForResponse();
            if (response != null) {
                System.out.println("CPU recebeu resposta do Middleware: " + response.getMessage());
            }
        }
    }

    // Método para configurar o Middleware
    public void setMiddleware(Middleware middleware) {
        this.middleware = middleware;
    }
}
