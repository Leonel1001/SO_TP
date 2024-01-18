import java.util.concurrent.LinkedBlockingQueue;

public class Middleware extends Thread {
    public LinkedBlockingQueue<String> messageQueue = new LinkedBlockingQueue<>();

    public Middleware(LinkedBlockingQueue<String> messageQueue) {
        this.messageQueue = messageQueue;
    }



    public void messageManager (String message){
        messageQueue.add(message);
    };

   
}
