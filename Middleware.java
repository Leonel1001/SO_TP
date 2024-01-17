import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import javax.swing.SwingUtilities;
import javax.swing.JOptionPane;

public class Middleware extends Thread {
    private final BlockingQueue<LogMessage> messageQueue;
    private final BlockingQueue<LogMessage> responseQueue;
    private final BlockingQueue<LogMessage> userResponsesQueue;

    public Middleware() {
        this.messageQueue = new LinkedBlockingQueue<>();
        this.responseQueue = new LinkedBlockingQueue<>();
        this.userResponsesQueue = new LinkedBlockingQueue<>();
    }

    @Override
    public void run() {
        new Thread(() -> {
            while (!Thread.interrupted()) {
                try {
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

                    // Exibir a mensagem recebida em uma janela de diálogo
                    SwingUtilities.invokeLater(() -> {
                        JOptionPane.showMessageDialog(
                                null,
                                "Mensagem Recebida no Satelite\n" + receivedMessage.getMessage(),
                                "Mensagem Recebida",
                                JOptionPane.INFORMATION_MESSAGE);
                    });

                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                    e.printStackTrace();
                }
            }
        }).start();

        // Lógica de processamento da userResponsesQueue
        new Thread(() -> {
            while (!Thread.interrupted()) {
                try {
                    LogMessage userResponse = userResponsesQueue.take();

                    // Exibir a mensagem recebida em uma janela de diálogo
                    SwingUtilities.invokeLater(() -> {
                        JOptionPane.showMessageDialog(null, "Mensagem Recebida de " + userResponse.getMessage(),
                                "Mensagem Recebida", JOptionPane.INFORMATION_MESSAGE);
                    });

                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                    e.printStackTrace();
                }
            }
        }).start();
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

    public void sendUserResponse(LogMessage response) {
        userResponsesQueue.offer(response);
    }
    
}
