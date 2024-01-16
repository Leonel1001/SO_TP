public class AutomaticMessage {

    public static void main(String[] args) {
        Kernel kernel = new Kernel();
        MemoryUnit mem = new MemoryUnit();
        Middleware middleware = new Middleware();
        

        Cpu cpu = new Cpu(kernel, mem, middleware);

        // Iniciar o Middleware e a CPU
        middleware.start();
        cpu.start();

        // Enviar mensagens automaticamente em intervalos regulares
        Runnable automaticMessageSender = () -> {
            while (true) {
                try {
                    Thread.sleep(2000); // Aguardar 2 segundos (intervalo arbitrário)

                    // Criar uma mensagem automaticamente
                    String automaticMessage = "Mensagem automática gerada em " + System.currentTimeMillis();
                    LogMessage logMessage = new LogMessage(automaticMessage, null);

                    // Enviar a mensagem para o Middleware
                    middleware.sendMessage(logMessage);

                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        };

        // Iniciar a thread para enviar mensagens automaticamente
        new Thread(automaticMessageSender).start();
    }
}
