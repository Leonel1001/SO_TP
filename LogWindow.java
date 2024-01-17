import javax.swing.*;
import java.awt.*;
import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class LogWindow extends JFrame {
    private JTextArea logTextArea;
    List<LogMessage> logMessages;
    

    public LogWindow() {
        setTitle("Logs");
        setSize(400, 300);
        setLocationRelativeTo(null);

        logMessages = new ArrayList<>();
        loadLogMessages(); // Carrega as mensagens salvas

        logTextArea = new JTextArea();
        logTextArea.setEditable(false);

        JScrollPane scrollPane = new JScrollPane(logTextArea);
        add(scrollPane, BorderLayout.CENTER);

        updateLogTextArea();
    }

    public List<LogMessage> getLogMessages() {
        return logMessages;
    }

    public void addLogMessage(LogMessage logMessage) {
        logMessages.add(logMessage);
        updateLogTextArea();
        saveLogMessages();
    }

    private void updateLogTextArea() {
        StringBuilder sb = new StringBuilder();
        for (LogMessage logMessage : logMessages) {
            sb.append(logMessage.getMessage()).append("\n");
        }
        logTextArea.setText(sb.toString());
    }

    private void loadLogMessages() {
        try (BufferedReader reader = new BufferedReader(new FileReader("log_messages.txt"))) {
            String line;
            while ((line = reader.readLine()) != null) {
                LogMessage logMessage = new LogMessage(line, null); // Considerando que o source pode ser nulo
                logMessages.add(logMessage);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void saveLogMessages() {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter("log_messages.txt"))) {
            for (LogMessage logMessage : logMessages) {
                writer.write(logMessage.getMessage());
                writer.newLine();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            LogWindow logWindow = new LogWindow();
            logWindow.setVisible(true);

            // Exemplo de adição de mensagens
            logWindow.addLogMessage(new LogMessage("Mensagem de log 1", null));
            logWindow.addLogMessage(new LogMessage("Mensagem de log 2", null));
        });
    }
}
