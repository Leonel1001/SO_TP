import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

public class Middleware extends Thread {
    private final BlockingQueue<LogMessage> messageQueue;
    private final BlockingQueue<LogMessage> responseQueue;
    private final Object responseLock = new Object();

    public Middleware() {
        this.messageQueue = new LinkedBlockingQueue<>();
        this.responseQueue = new LinkedBlockingQueue<>();
    }

    @Override
    public void run() {
        while (!Thread.interrupted()) {
            try {
                // Aguarda até que uma mensagem seja recebida
                LogMessage receivedMessage = messageQueue.take();
                System.out.println("Middleware recebeu a mensagem: " + receivedMessage.getMessage());

                // Lógica de processamento da mensagem
                // ...

                // Envia uma resposta de maneira sincronizada
                synchronized (responseLock) {
                    responseQueue.put(receivedMessage); // Envia a própria mensagem como resposta
                }
            } catch (InterruptedException e) {
                // Lidar com a interrupção
                Thread.currentThread().interrupt();
            }
        }
    }

    public void sendMessage(LogMessage logMessage) {
        // Lógica para enviar mensagens ao Middleware
        try {
            messageQueue.put(logMessage);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }

    public LogMessage checkForResponse() {
        // Verifica se há resposta na fila e retorna de maneira sincronizada
        synchronized (responseLock) {
            return responseQueue.poll();
        }
    }
}
