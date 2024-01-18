import java.util.concurrent.LinkedBlockingQueue;

public class Middleware extends Thread {
    public LinkedBlockingQueue<String> messageQueue = new LinkedBlockingQueue<>();

    // Construtor da classe Middleware, inicia a queue.
    public Middleware(LinkedBlockingQueue<String> messageQueue) {
        this.messageQueue = messageQueue;
    }

    // Método para manipular uma mensagem, adicionando-a à queue.
    public void messageManager(String message) {
        messageQueue.add(message);
    };

}
