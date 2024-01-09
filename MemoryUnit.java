// MemoryUnit.java
public class MemoryUnit {
    private int data;
    private final Object lock = new Object();

    // Método para leitura segura dos dados
    public int readData() {
        synchronized (lock) {
            return data;
        }
    }

    // Método para escrita segura dos dados
    public void writeData(int data) {
        synchronized (lock) {
            this.data = data;
        }
    }

    // Outros métodos relacionados à gestão de dados, se necessário
}
