import javax.swing.JOptionPane;
public class Cpu extends Thread {
    private final Kernel kernel;
    private final MemoryUnit mem;
    private Middleware middleware;
    private String lastResponse = ""; 
    

    public Cpu(Kernel kernel, MemoryUnit mem, Middleware middleware) {
        this.kernel = kernel;
        this.mem = mem;
        this.middleware = middleware;
    }

    @Override
    public void run() {
        while (kernel.isRunning()) {
            // Lógica de gestão, escalonamento e execução de tarefas
            synchronized (mem) {
                // int data = mem.readData();
                // Processamento em tempo real
                // ...

                // Verifica se há resposta do Middleware
                synchronized (middleware) {
                    LogMessage response = middleware.checkForResponse();
                    if (response != null && !response.getMessage().equals(lastResponse)) {
                        String message = response.getMessage();

                        // Exibir mensagem em uma janela de notificação
                        showNotification(message);

                        // Enviar resposta à mensagem recebida
                        sendResponse("Mensagem Recebida no Satelite!");

                        // Update the lastResponse to the current response message
                        lastResponse = message;
                    }
                }
            }
        }
    }

    // Método para exibir mensagem em uma janela de notificação
    private void showNotification(String message) {
        JOptionPane.showMessageDialog(null, message, "Notificação", JOptionPane.INFORMATION_MESSAGE);
    }

    // Método para configurar o Middleware
    public void setMiddleware(Middleware middleware) {
        // Sincroniza o acesso ao middleware ao configurá-lo
        synchronized (this) {
            this.middleware = middleware;
        }
    }

    public void sendResponse(String responseMessage) {
        LogMessage response = new LogMessage(responseMessage, this);

        // Synchronize access to the middleware when sending the response
        synchronized (middleware) {
            middleware.sendResponse(response);
        }
    }

    // Método para receber mensagem do usuário
    public void receiveUserMessage(String userMessage) {
        // Faça o processamento necessário com a mensagem do usuário
        // Por exemplo, exibir em uma janela de notificação
        showNotification("Mensagem do Utilizador recebida: " + userMessage);
    }

}
