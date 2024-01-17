import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

public class Middleware extends Thread {
    private final BlockingQueue<LogMessage> messageQueue;
    private final BlockingQueue<LogMessage> responseQueue;
    private final BlockingQueue<LogMessage> userResponsesQueue;

    public Middleware() {
        this.messageQueue = new LinkedBlockingQueue<>();
        this.responseQueue = new LinkedBlockingQueue<>();
        this.userResponsesQueue = new LinkedBlockingQueue<>();
    }

    public void run() {
        new Thread(() -> {
            while (!Thread.interrupted()) {
                try {
                    LogMessage receivedMessage = messageQueue.take();
                    System.out.println("Middleware recebeu a mensagem: " + receivedMessage.getMessage());

                    synchronized (responseQueue) {
                        responseQueue.offer(new LogMessage("Resposta do Middleware para CPU", this));
                        responseQueue.notify();
                    }

                    
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                    e.printStackTrace();
                }
            }
        }).start();
    }

    // ... (outros métodos existentes)

    public LogMessage checkForResponse() {
        // Verifica se há resposta na fila de maneira sincronizada
        synchronized (responseQueue) {
            return responseQueue.poll();
        }
    }

    public void sendMessage(LogMessage logMessage) {
        // Lógica para enviar mensagens ao Middleware
        try {
            messageQueue.offer(logMessage);
        } catch (Exception e) {
            e.printStackTrace();
            // Pode fazer mais coisas aqui, se necessário
        }
    }

    // Método público para acessar responseQueue
    public BlockingQueue<LogMessage> getResponseQueue() {
        return responseQueue;
    }

    public void sendResponse(LogMessage response) {
        // Adiciona a resposta à fila de respostas
        responseQueue.offer(response);
    }

    public void sendUserResponse(LogMessage response) {
        userResponsesQueue.offer(response);
    }

    public BlockingQueue<LogMessage> getMessageQueue() {
        return messageQueue;
    }
}