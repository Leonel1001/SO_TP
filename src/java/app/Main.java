package src.java.app;

public class Main {
    public static void main(String[] args) {
        Kernel kernel = new Kernel();
        kernel.start();

        // Aguarde algum tempo para garantir que o Middleware esteja pronto
        try {
            Thread.sleep(1000); // 1 segundo
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        // Inicie a Estação após o Middleware e o Kernel estarem prontos
        Station.exibir();

        // Encerre o Kernel quando a Estação for fechada
        kernel.stop();
    }
}
