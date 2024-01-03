
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

public class Middleware extends Thread {
    private final BlockingQueue<String> messageQueue;
    private final BlockingQueue<String> responseQueue;

    public Middleware() {
        this.messageQueue = new LinkedBlockingQueue<>();
        this.responseQueue = new LinkedBlockingQueue<>();
    }

    @Override
    public void run() {
        while (!Thread.interrupted()) {
            try {
                // Aguarda até que uma mensagem seja recebida
                String receivedMessage = messageQueue.take();
                System.out.println("Middleware recebeu a mensagem: " + receivedMessage);

                // Lógica de processamento da mensagem
                // ...

                // Envia uma resposta
                String response = "Mensagem recebida com sucesso!";
                responseQueue.put(response);
            } catch (InterruptedException e) {
                // Lidar com a interrupção
                Thread.currentThread().interrupt();
            }
        }
    }

    public void sendMessage(String message) {
        // Lógica para enviar mensagens ao Middleware
        try {
            messageQueue.put(message);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }

    public String checkForResponse() {
        // Verifica se há resposta na fila e retorna
        return responseQueue.poll();
    }   
}
