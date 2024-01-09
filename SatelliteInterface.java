import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class SatelliteInterface extends JFrame {
    private final Kernel kernel;
    private final LogWindow logWindow;

    public SatelliteInterface(LogWindow logWindow) {
        this.kernel = new Kernel();
        this.logWindow = logWindow;
        initComponents();
    }

    private void initComponents() {
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setTitle("Satellite Interface");
        setSize(400, 200);
        setLocationRelativeTo(null);

        JTextField messageField = new JTextField();
        JTextArea responseArea = new JTextArea();
        JButton sendButton = new JButton("Enviar");
        JButton openLogButton = new JButton("Ver Logs");

        setLayout(new BorderLayout());
        JPanel inputPanel = new JPanel();
        inputPanel.setLayout(new BorderLayout());
        inputPanel.add(messageField, BorderLayout.CENTER);
        inputPanel.add(sendButton, BorderLayout.EAST);

        add(inputPanel, BorderLayout.NORTH);
        add(new JScrollPane(responseArea), BorderLayout.CENTER);
        add(openLogButton, BorderLayout.SOUTH);

        Cpu cpu = kernel.getCpu();
        Middleware middleware = kernel.getMiddleware();
        cpu.setMiddleware(middleware);

        sendButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String message = messageField.getText();
                LogMessage logMessage = new LogMessage(message, middleware);
                middleware.sendMessage(logMessage);
                messageField.setText("");
            }
        });

        openLogButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                logWindow.setVisible(true);
            }
        });

        Thread responseThread = new Thread(() -> {
            while (kernel.isRunning()) {
                LogMessage response = middleware.checkForResponse();
                if (response != null) {
                    SwingUtilities.invokeLater(() -> {
                        responseArea.append("Mensagem: " + response.getMessage() + "\n");
                        logWindow.addLogMessage(response);
                    });
                }
            }
        });
        responseThread.start();
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            try {
                LogWindow logWindow = new LogWindow();
                SatelliteInterface interfaceFrame = new SatelliteInterface(logWindow);
                interfaceFrame.setVisible(true);
                interfaceFrame.kernel.start();
            } catch (Exception e) {
                e.printStackTrace();
                // Handle the exception appropriately
            }
        });
    }
}
