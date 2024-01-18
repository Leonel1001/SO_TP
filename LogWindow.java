import javax.swing.*;
import java.awt.*;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

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
        try (BufferedReader br = new BufferedReader(new FileReader("log_messages.txt"))) {
            StringBuilder sb = new StringBuilder();
            String line;

            while ((line = br.readLine()) != null) {
                sb.append(line).append("\n");
            }

            logTextArea.setText(sb.toString());
        } catch (IOException e) {
            e.printStackTrace();
            logTextArea.setText("Erro ao ler o arquivo de log.");
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            LogWindow logWindow = new LogWindow();
            logWindow.setVisible(true);
        });
    }
}
