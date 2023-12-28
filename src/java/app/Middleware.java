package src.java.app;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;

public class Middleware extends Thread {
    private Cpu cpu;
    private volatile boolean isRunning = true;

    public Middleware(Cpu cpu) {
        this.cpu = cpu;
    }

    @Override
    public void run() {
        try (ServerSocket serverSocket = new ServerSocket(12345)) {
            System.out.println("Middleware aguardando conexão...");

            // Aguarda a conexão com a estação
            Socket socket = serverSocket.accept();
            System.out.println("Estação conectada ao Middleware.");

            // Streams de entrada e saída para comunicação com a estação
            ObjectOutputStream outputStream = new ObjectOutputStream(socket.getOutputStream());
            ObjectInputStream inputStream = new ObjectInputStream(socket.getInputStream());

            // Lógica para comunicação com a estação
            while (isRunning) {
                // Aguarda receber uma mensagem da estação
                String mensagemRecebida = (String) inputStream.readObject();
                System.out.println("Estação: " + mensagemRecebida);

                // Responde à estação
                String resposta = "OK";
                outputStream.writeObject(resposta);
                outputStream.flush();
            }
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    public void stopMiddleware() {
        isRunning = false;
    }
}
