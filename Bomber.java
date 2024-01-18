public class Bomber {
    private final Middleware middleware;

    public Bomber(Middleware middleware) {
        this.middleware = middleware;
    }

    public void iniciarBombardeio() {
        // Criação de 5 threads para bombardear mensagens
        for (int i = 0; i < 5; i++) {
            Thread thread = new Thread(new BombardeadorRunnable());
            thread.start(); // Inicia cada thread
        }
    }

    private class BombardeadorRunnable implements Runnable {
        @Override
        public void run() {
            try {
                for (int j = 0; j < 10; j++) {
                    String message = "Mensagem da Thread " + Thread.currentThread().getId() + ": " + j;
                    System.out.println("Enviando mensagem: " + message);
                    middleware.messageManager(message);
                    Thread.sleep(3000); // Simula um intervalo entre mensagens
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}
