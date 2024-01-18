import java.util.concurrent.atomic.AtomicInteger;

public class AutomaticMessage extends Thread {
    private final Middleware middleware;
    private final AtomicInteger activeThreadCount;
    private volatile boolean isActive;

    // Construtor que recebe uma instância de Middleware e um contador de threads
    public AutomaticMessage(Middleware middleware, ThreadCounter threadCounter) {
        this.middleware = middleware;
        this.activeThreadCount = threadCounter.activeThreadCount;
        this.isActive = true;
    }

    public void stopThread() {
        isActive = false;
        interrupt();
    }

    // Método que representa a execução da thread
    @Override
    public void run() {
        try {

            while (isActive && !Thread.interrupted()) {

                String automaticMessage = generateAutomaticMessage();

                middleware.messageManager(automaticMessage);

                Thread.sleep(3000);

                activeThreadCount.incrementAndGet();
            }
        } catch (InterruptedException e) {

            Thread.currentThread().interrupt();
        } finally {

            activeThreadCount.decrementAndGet();
        }
    }

    // Método synchronized para obter o número de threads ativas
    public synchronized int getActiveThreadCount() {
        return activeThreadCount.get();
    }

    // Método estático para gerar uma mensagem automática
    public static String generateAutomaticMessage() {

        String automaticMessage = "Mensagem automática: " + System.currentTimeMillis();
        System.out.println("Gerando mensagem automática: " + automaticMessage);
        return automaticMessage;
    }
}
