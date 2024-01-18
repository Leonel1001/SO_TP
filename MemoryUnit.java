import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class MemoryUnit {
    // Caminho do arquivo de log para armazenar mensagens.
    private static final String LOG_FILE_PATH = "log_messages.txt";

    public MemoryUnit() {
    }

    // Método para guardar uma mensagem no arquivo de log.
    public void saveMessage(String message) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(LOG_FILE_PATH, true))) {
            writer.write(message + "\n");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // Método para obter contagens de mensagens por utilizador.
    public Map<String, Integer> getUserMessageCounts() {
        Map<String, Integer> userMessageCounts = new HashMap<>();
        try (BufferedReader reader = new BufferedReader(new FileReader(LOG_FILE_PATH))) {
            String line;
            while ((line = reader.readLine()) != null) {
                if (line.startsWith("Mensagem de ")) {
                    String username = getUsernameFromMessage(line);
                    userMessageCounts.put(username, userMessageCounts.getOrDefault(username, 0) + 1);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return userMessageCounts;
    }

    // Método para obter contagens de respostas do satélite por utilizador.
    public Map<String, Integer> getSatelliteResponseCounts() {
        Map<String, Integer> satelliteResponseCounts = new HashMap<>();
        try (BufferedReader reader = new BufferedReader(new FileReader(LOG_FILE_PATH))) {
            String line;
            while ((line = reader.readLine()) != null) {
                if (line.startsWith("Satélite responde a Mensagem de ")) {
                    String username = getUsernameFromMessage(line);
                    satelliteResponseCounts.put(username, satelliteResponseCounts.getOrDefault(username, 0) + 1);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return satelliteResponseCounts;
    }

    // Método privado para extrair o nome de utilizador a partir de uma mensagem.
    private String getUsernameFromMessage(String message) {
        int startIndex = message.indexOf("Mensagem de ") + "Mensagem de ".length();
        int endIndex = message.indexOf(":", startIndex);

        if (startIndex >= 0 && endIndex >= 0) {
            return message.substring(startIndex, endIndex).trim();
        } else {
            return "";
        }
    }
}
