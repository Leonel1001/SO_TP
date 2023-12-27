public class Main {
    public static void main(String[] args) {
        // Criação e início do Kernel
        Kernel kernel = new Kernel();
        kernel.start();

        // Comunicação com o Middleware (exemplo)
        kernel.communicateWithMiddleware("Mensagem da aplicação para o Middleware");

        // Lógica adicional, se necessário

        // Encerramento do Kernel
        kernel.stop();
    }
}
