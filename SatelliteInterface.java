import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class SatelliteInterface extends JFrame {

    private final Kernel kernel;

    public SatelliteInterface() {
        this.kernel = new Kernel();
        initComponents();
    }

    private void initComponents() {
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setTitle("Satellite Interface");
        setSize(400, 200);
        setLocationRelativeTo(null);

        // Criar componentes
        JTextField messageField = new JTextField();
        JTextArea responseArea = new JTextArea();
        JButton sendButton = new JButton("Enviar");

        // Configurar layout
        setLayout(new BorderLayout());
        JPanel inputPanel = new JPanel();
        inputPanel.setLayout(new BorderLayout());
        inputPanel.add(messageField, BorderLayout.CENTER);
        inputPanel.add(sendButton, BorderLayout.EAST);

        // Adicionar componentes ao frame
        add(inputPanel, BorderLayout.NORTH);
        add(new JScrollPane(responseArea), BorderLayout.CENTER);

        // Configurar a referência do Middleware na CPU
        Cpu cpu = kernel.getCpu();
        Middleware middleware = kernel.getMiddleware();
        cpu.setMiddleware(middleware);

        // Ação do botão de envio
        sendButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String message = messageField.getText();
                middleware.sendMessage(message);
                messageField.setText(""); // Limpar o campo de mensagem
            }
        });

        // Thread para receber e imprimir respostas do Middleware
        Thread responseThread = new Thread(() -> {
            while (kernel.isRunning()) {
                String response = middleware.checkForResponse();
                if (response != null) {
                    responseArea.append("Interface recebeu resposta do Middleware: " + response + "\n");
                }
            }
        });
        responseThread.start();
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            SatelliteInterface interfaceFrame = new SatelliteInterface();
            interfaceFrame.setVisible(true);
            interfaceFrame.kernel.start(); // Iniciar o kernel após exibir a interface
        });
    }
}
