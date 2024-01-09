// MemoryUnit.java
public class MemoryUnit {
    private int data;
    private final Object lock = new Object();

    public int readData() {
        synchronized (lock) {
            return data;
        }
    }

    public void writeData(int data) {
        synchronized (lock) {
            this.data = data;
        }
    }
}
