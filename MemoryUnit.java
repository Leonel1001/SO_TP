
public class MemoryUnit {
    private int data;
    private final Object lock = new Object();

    public synchronized int readData() {
        return data;
    }

    public synchronized void writeData(int data) {
        this.data = data;
    }
}
