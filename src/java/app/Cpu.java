package src.java.app;

public class Cpu extends Thread {
    private MemoryUnit memoryUnit;
    private Kernel kernel;

    public Cpu(MemoryUnit memoryUnit, Kernel kernel) {
        this.memoryUnit = memoryUnit;
        this.kernel = kernel;
    }

    @Override
    public void run() {
        while (isRunning()) {
            // Lógica da CPU

            int inputData = memoryUnit.readData();
            int processedData = processData(inputData);
            memoryUnit.writeData(processedData);

            System.out.println("CPU processou uma tarefa.");

            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }

        System.out.println("CPU encerrada.");
    }

    private int processData(int inputData) {
        // Lógica para processar os dados na CPU
        return inputData; // Exemplo: retorno do mesmo valor por enquanto
    }

    private boolean isRunning() {
        return kernel != null && kernel.isRunning() && !Thread.interrupted();
    }
}
