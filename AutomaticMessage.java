import java.util.concurrent.LinkedBlockingQueue;

public class AutomaticMessage extends Thread {
    private final LinkedBlockingQueue<String> messageQueue;

    public AutomaticMessage(LinkedBlockingQueue<String> messageQueue) {
        this.messageQueue = messageQueue;
    }

   
    @Override
    public void run() {
        while (!Thread.interrupted()) {
            try {
                String automaticMessage = generateAutomaticMessage();
                messageQueue.put(automaticMessage);
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt(); 
            }
        }
    }
    

    public static String generateAutomaticMessage() {
        String automaticMessage = "Mensagem automática: " + System.currentTimeMillis();
        System.out.println("Gerando mensagem automática: " + automaticMessage);
        return automaticMessage;
    }
    
}
