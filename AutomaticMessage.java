public class AutomaticMessage extends Thread {
    private final Middleware middleware;

    public AutomaticMessage(Middleware middleware) {
        this.middleware = middleware;
    }

    @Override
    public void run() {
        while (!Thread.interrupted()) {
            try {
                String automaticMessage = generateAutomaticMessage();
                middleware.messageManager(automaticMessage);
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
