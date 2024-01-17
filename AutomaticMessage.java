public class AutomaticMessage {

    // Inicializador estático para iniciar automaticamente
    static {
        Kernel kernel = new Kernel();
        kernel.start();

        Cpu cpu = kernel.getCpu();

        // Enviar mensagens automaticamente em intervalos regulares
        Runnable automaticMessageSender = () -> {
            while (true) {
                try {
                    Thread.sleep(2000); // Aguardar 2 segundos (intervalo arbitrário)

                    // Criar uma mensagem automática
                    String automaticMessage = "Mensagem automática gerada em " + System.currentTimeMillis();

                    // Enviar a mensagem para o CPU usando o método do CPU
                    cpu.sendMessageToCPU(automaticMessage);

                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        };

        // Iniciar a thread para enviar mensagens automaticamente
        new Thread(automaticMessageSender).start();
    }
}
