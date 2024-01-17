import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class LogWindow extends JFrame {
    private JTextArea logTextArea;
    private List<String> logMessages;

    public LogWindow() {
        setTitle("Logs");
        setSize(400, 300);
        setLocationRelativeTo(null);

        logMessages = new ArrayList<>();

        logTextArea = new JTextArea();
        logTextArea.setEditable(false);

        JScrollPane scrollPane = new JScrollPane(logTextArea);
        add(scrollPane, BorderLayout.CENTER);

        updateLogTextArea();
    }

    public List<String> getLogMessages() {
        return new ArrayList<>(logMessages);
    }

    public void addLogMessage(String message) {
        logMessages.add(message);
        updateLogTextArea();
    }

    private void updateLogTextArea() {
        StringBuilder sb = new StringBuilder();
        for (String message : logMessages) {
            sb.append(message).append("\n");
        }
        logTextArea.setText(sb.toString());
    }
}
