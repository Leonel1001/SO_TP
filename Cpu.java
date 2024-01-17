import java.util.concurrent.Semaphore;

public class Cpu extends Thread {
    private final Kernel kernel;
    private MemoryUnit mem;
    private Middleware middleware;
    private String lastResponse = "";
    private final Semaphore semaphore;

    public Cpu(Kernel kernel, MemoryUnit mem, Middleware middleware, Semaphore semaphore) {
        this.kernel = kernel;
        this.mem = mem;
        this.middleware = middleware;
        this.semaphore = semaphore;
    }
    @Override
public void run() {
    while (kernel.isRunning()) {
        try {
            // Verifica se há resposta do Middleware
            String receivedMessage = middleware.receiveMessageFromInterface();

            if (receivedMessage != null) {
                System.out.println("Received from Interface: " + receivedMessage);

                // Armazenar a mensagem enviada no objeto LogMessage
                semaphore.acquire();
                lastResponse = "Olá, o satélite recebeu";
                semaphore.release();

                // Enviar resposta à mensagem recebida
                middleware.sendMessageToMiddleware(lastResponse);
                System.out.println("Sent response to Middleware: " + lastResponse);

                // Processar a mensagem recebida
                processMessage(receivedMessage);

                // Agora, se desejar, você pode enviar uma resposta específica para a Interface
                String interfaceResponse = "Mensagem recebida e processada com sucesso!";
                middleware.sendManualCpuResponse(interfaceResponse);
                System.out.println("Sent manual response to Interface: " + interfaceResponse);
            }

            // Processamento em tempo real
            // ...

        } catch (InterruptedException e) {
            // Tratar a exceção de interrupção, se necessário
            e.printStackTrace();
        }
    }
}

    
    public String getLastSentResponse() {
        return lastResponse;
    }

    public void setMiddleware(Middleware middleware) {
        synchronized (this) {
            this.middleware = middleware;
        }
    }

    public String getLastResponse() {
        return lastResponse;
    }

    public void setMemoryUnit(MemoryUnit mem) {
        this.mem = mem;
    }

    public void sendMessageToCPU(String message) {
        processMessage(message);
    }

    void processMessage(String message) {
        mem.sendMessageToMemoryUnit(lastResponse);
    }
}
