package src.java.app;

public class Cpu extends Thread {
    private Kernel kernel;

    public Cpu(Kernel kernel) {
        this.kernel = kernel;
    }

    @Override
    public void run() {
        while (isRunning()) {
            // Lógica da CPU

            // Processamento fictício
            System.out.println("CPU processou uma tarefa.");

            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }

        System.out.println("CPU encerrada.");
    }

    private boolean isRunning() {
        return kernel != null && kernel.isRunning() && !Thread.interrupted();
    }

    public void processarTarefa(String mensagem) {
        System.out.println("CPU processou a tarefa: " + mensagem);
    }
}
