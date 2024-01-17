import javax.swing.*;
import java.awt.*;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class LogWindow extends JFrame {
    private JTextArea logTextArea;

    public LogWindow() {
        setTitle("Logs");
        setSize(400, 300);
        setLocationRelativeTo(null);

        logTextArea = new JTextArea();
        logTextArea.setEditable(false);

        JScrollPane scrollPane = new JScrollPane(logTextArea);
        add(scrollPane, BorderLayout.CENTER);

        updateLogTextArea();
    }

    public void updateLogTextArea() {
        List<LogMessage> logMessages = readLogMessagesFromFile();
        StringBuilder sb = new StringBuilder();
        for (LogMessage logMessage : logMessages) {
            sb.append(logMessage.getMessage()).append("\n");
        }
        logTextArea.setText(sb.toString());
    }

    private List<LogMessage> readLogMessagesFromFile() {
        try (BufferedReader reader = new BufferedReader(new FileReader("log_messages.txt"))) {
            // Lista para armazenar as mensagens lidas do arquivo
            List<LogMessage> logMessages = new ArrayList<>();

            String line;
            while ((line = reader.readLine()) != null) {
                LogMessage logMessage = new LogMessage(line, null); // Considerando que o source pode ser nulo
                logMessages.add(logMessage);
            }

            return logMessages;
        } catch (IOException e) {
            e.printStackTrace();
            return Collections.emptyList(); // Retorna uma lista vazia em caso de erro
        }
    }
}
