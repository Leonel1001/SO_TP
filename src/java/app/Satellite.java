package src.java.app;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

public class Satellite {
    private ObjectOutputStream outputStream;
    private ObjectInputStream inputStream;

    public Satellite() {
        iniciarConexao();
    }

    private void iniciarConexao() {
        try {
            // Conecte-se ao Middleware
            Socket socket = new Socket("localhost", 12345);
            outputStream = new ObjectOutputStream(socket.getOutputStream());
            inputStream = new ObjectInputStream(socket.getInputStream());

            // Inicie uma thread para escutar mensagens do Middleware
            new Thread(this::escutarMensagens).start();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void escutarMensagens() {
        try {
            while (true) {
                // Aguarda receber uma mensagem do Middleware
                String mensagemRecebida = (String) inputStream.readObject();
                System.out.println("Satélite: Mensagem recebida do Middleware - " + mensagemRecebida);

                // Responde ao Middleware
                enviarMensagem("OK");
            }
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    private void enviarMensagem(String mensagem) {
        try {
            // Envia a mensagem para o Middleware
            outputStream.writeObject(mensagem);
            outputStream.flush();
            System.out.println("Satélite: Mensagem enviada para o Middleware - " + mensagem);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
