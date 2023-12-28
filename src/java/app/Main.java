package src.java.app;

import javax.swing.*;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

public class Main {
    public static void main(String[] args) {
        // Inicializa o kernel
        Kernel kernel = new Kernel();
        kernel.start();

        // Espera algum tempo para garantir que o Middleware esteja pronto
        esperarMiddlewarePronto();

        // Cria uma conexão com a Estação
        Socket estacaoSocket = conectarEstacao();

        if (estacaoSocket != null) {
            // Inicia a interface do usuário
            iniciarInterfaceUsuario(estacaoSocket);
        } else {
            System.err.println("Não foi possível conectar à Estação.");
        }

        // Encerra o kernel quando a interface do usuário for fechada
        kernel.stop();
    }

    private static void esperarMiddlewarePronto() {
        try {
            Thread.sleep(1000); // 1 segundo
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    private static Socket conectarEstacao() {
        try {
            Socket socket = new Socket("localhost", 12345);
            System.out.println("Estação conectada ao Middleware.");
            return socket;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    private static void iniciarInterfaceUsuario(Socket estacaoSocket) {
        try {
            ObjectOutputStream estacaoOutputStream = new ObjectOutputStream(estacaoSocket.getOutputStream());
            ObjectInputStream middlewareInputStream = new ObjectInputStream(estacaoSocket.getInputStream());

            SwingUtilities.invokeLater(() -> {
                UserInterface userInterface = new UserInterface(estacaoOutputStream);

                // Aguarde mensagens da Estação e exiba na interface do usuário
                new Thread(() -> {
                    while (true) {
                        try {
                            Object mensagem = middlewareInputStream.readObject();
                            userInterface.exibirMensagemRecebida(mensagem.toString());
                        } catch (IOException | ClassNotFoundException e) {
                            e.printStackTrace();
                        }
                    }
                }).start();
            });
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
