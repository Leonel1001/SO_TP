import java.util.concurrent.atomic.AtomicInteger;

// A classe Bomber tem uma referência para um Middleware e um AtomicInteger para contar o número de threads ativas.
public class Bomber {
    private final Middleware middleware;
    private final AtomicInteger activeThreadCountBomber;

    public Bomber(Middleware middleware) {
        this.middleware = middleware;
        this.activeThreadCountBomber = new AtomicInteger(0);
    }

    // Método que inicia o bombardeio, criando e iniciando várias threads.
    public void iniciarBombardeio() {

        for (int i = 0; i < 5; i++) {
            Thread thread = new Thread(new BombardeadorRunnable());
            thread.start();
        }
    }

    // Método sincronizado para obter o número atual de threads ativas.
    public synchronized int getActiveThreadCount() {
        return activeThreadCountBomber.get();
    }

    // Método sincronizado para incrementar o contador de threads ativas.
    private synchronized void incrementActiveThreadCount() {
        activeThreadCountBomber.incrementAndGet();
    }

    // Método sincronizado para decrementar o contador de threads ativas.
    private synchronized void decrementActiveThreadCount() {
        activeThreadCountBomber.decrementAndGet();
    }

    // Classe interna que implementa a interface Runnable para ser usada como tarefa
    // pelas threads.
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
