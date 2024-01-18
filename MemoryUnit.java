import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;


public class MemoryUnit {

    private static final String LOG_FILE_PATH = "log_messages.txt";


    public MemoryUnit() {
    
}

public void saveMessage(String message){
    try (BufferedWriter writer = new BufferedWriter(
        new FileWriter(LOG_FILE_PATH, true))) {
            writer.write(message + "\n");
} catch (IOException e) {
    e.printStackTrace();
}
}
}