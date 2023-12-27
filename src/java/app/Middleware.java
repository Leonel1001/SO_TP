
public class Middleware extends Thread {
    private Cpu cpu;

    public Middleware(Cpu cpu) {
        this.cpu = cpu;
    }

    @Override
    public void run() {
        while (true) {
            // Lógica para comunicação utilizando a estrutura de dados compartilhada
            // Pode envolver troca de mensagens entre a CPU e a aplicação

            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    public void receiveMessage(String message) {
        // Lógica para processar mensagens recebidas da CPU
        System.out.println("Middleware recebeu uma mensagem: " + message);
    }
}
