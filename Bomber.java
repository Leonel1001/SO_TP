import java.util.concurrent.atomic.AtomicInteger;

public class Bomber {
    private final Middleware middleware;
    private final AtomicInteger activeThreadCountBomber;

    public Bomber(Middleware middleware) {
        this.middleware = middleware;
        this.activeThreadCountBomber = new AtomicInteger(0);
    }

    public void iniciarBombardeio() {
        // Criação de 5 threads para bombardear mensagens
        for (int i = 0; i < 5; i++) {
            Thread thread = new Thread(new BombardeadorRunnable());
            thread.start(); // Inicia cada thread
        }
    }

    public synchronized int getActiveThreadCount() {
        return activeThreadCountBomber.get();
    }

    private synchronized void incrementActiveThreadCount() {
        activeThreadCountBomber.incrementAndGet();
    }

    private synchronized void decrementActiveThreadCount() {
        activeThreadCountBomber.decrementAndGet();
    }


    private class BombardeadorRunnable implements Runnable {
        @Override
        public void run() {
            try {
                for (int j = 0; j < 3; j++) {
                    incrementActiveThreadCount();
                    String message = "Mensagem da Thread " + Thread.currentThread().getId();
                    System.out.println("Enviando mensagem: " + message);
                    middleware.messageManager(message);
                    Thread.sleep(3000); // Simula um intervalo entre mensagens
                }
                decrementActiveThreadCount();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}
