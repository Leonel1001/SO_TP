package src.java.app;

import javax.swing.*;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;

public class Station extends Thread {
    private UserInterface userInterface;
    private ObjectOutputStream outputStream;

    public Station(UserInterface userInterface) {
        this.userInterface = userInterface;
    }

    @Override
    public void run() {
        try (ServerSocket serverSocket = new ServerSocket(12345)) {
            System.out.println("Estação aguardando conexão...");

            // Aguarda a conexão com o Middleware
            Socket socket = serverSocket.accept();
            System.out.println("Middleware conectado à Estação.");

            // Streams de entrada e saída para comunicação com o Middleware
            outputStream = new ObjectOutputStream(socket.getOutputStream());
            ObjectInputStream inputStream = new ObjectInputStream(socket.getInputStream());

            // Lógica para comunicação com o Middleware
            while (true) {
                // Aguarda receber uma mensagem do Middleware
                String mensagemRecebida = (String) inputStream.readObject();
                userInterface.exibirMensagemRecebida(mensagemRecebida);

                // Responde ao Middleware (apenas para simulação)
                String resposta = "Recebida";
                enviarResposta(resposta);
            }
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    private void enviarResposta(String resposta) {
        try {
            outputStream.writeObject(resposta);
            outputStream.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void exibir(UserInterface userInterface) {
        SwingUtilities.invokeLater(() -> {
            Station station = new Station(userInterface);
            station.start();
        });
    }
}
