import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

public class Middleware extends Thread {
    private final BlockingQueue<LogMessage> messageQueue;
    private final BlockingQueue<LogMessage> responseQueue;

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
                responseQueue.offer(new LogMessage("Resposta do Middleware para CPU", this));

                // Notifica o Cpu que há uma resposta disponível
                synchronized (responseQueue) {
                    responseQueue.notify();
                }

            } catch (InterruptedException e) {
                // Lidar com a interrupção
                Thread.currentThread().interrupt();
                e.printStackTrace();
            }
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

    public LogMessage checkForResponse() {
        // Verifica se há resposta na fila e retorna de maneira sincronizada
        return responseQueue.poll();
    }

    // Método para enviar resposta à CPU
    public void sendResponse(LogMessage response) {
        // Adiciona a resposta à fila de respostas
        responseQueue.offer(response);
    }

    
}
