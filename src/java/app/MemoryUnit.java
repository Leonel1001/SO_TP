package src.java.app;

public class MemoryUnit {
    private int data;

    public synchronized int readData() {
        return data;
    }

    public synchronized void writeData(int value) {
        data = value;
    }
}
