import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.Semaphore;
import java.util.concurrent.atomic.AtomicInteger;

public class Cpu extends Thread {
    private final Kernel kernel;
    private final MemoryUnit mem;
    private final LinkedBlockingQueue<String> messageQueue;
    private final Semaphore semaphore;
    private final AtomicInteger activeThreadCount;

    public Cpu(Kernel kernel, MemoryUnit mem, Middleware middleware, LinkedBlockingQueue<String> messageQueue) {
        this.kernel = kernel;
        this.mem = mem;
        this.messageQueue = messageQueue;
        this.semaphore = new Semaphore(1);
        this.activeThreadCount = new AtomicInteger(0);
    }

    // Método para responder a uma mensagem recebida.
    public void responseMessage(String message) {
        String response = "Satélite responde a " + message;
        System.out.println(response);
        mem.saveMessage(response);
    }

    // Sobrescreve o método run() da classe Thread para especificar o comportamento
    // da CPU.
    @Override
    public void run() {
        incrementActiveThreadCount();
        // Executa enquanto o kernel estiver em execução.
        while (kernel.isRunning()) {
            try {
                String message = messageQueue.take();
                processMessage(message);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        decrementActiveThreadCount();
    }

    // Método privado para processar uma mensagem.
    private synchronized void processMessage(String message) {
        try {
            semaphore.acquire();

            System.out.println("CPU processando mensagem: " + message);
            waitForCpuResponse();
            mem.saveMessage(message);
            responseMessage(message);

        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            semaphore.release();
        }
    }

    // Método para obter o número atual de threads ativas.
    public synchronized int getActiveThreadCount() {
        return activeThreadCount.get();
    }

    // Métodos para incrementar e decrementar o contador de threads ativas, de forma
    // segura.
    private synchronized void incrementActiveThreadCount() {
        activeThreadCount.incrementAndGet();
    }

    private synchronized void decrementActiveThreadCount() {
        activeThreadCount.decrementAndGet();
    }

    // Método simulado de espera da CPU antes de responder.
    String waitForCpuResponse() {

        return "Ok! Recebido!";
    }

}
