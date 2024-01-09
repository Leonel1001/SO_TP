import javax.swing.*;
import java.awt.*;

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
    }

    public void addLogMessage(LogMessage logMessage) {
        SwingUtilities.invokeLater(() -> {
            logTextArea.append(logMessage.getMessage() + "\n");
            logTextArea.setCaretPosition(logTextArea.getDocument().getLength());
        });
    }
}
