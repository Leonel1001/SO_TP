package src.java.app;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.Socket;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;

public class Station {
    private ObjectOutputStream outputStream;
    private JTextField textField;

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> exibir());
    }

    public static void exibir() {
        JFrame frame = new JFrame("Estação");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(300, 200);

        JPanel panel = new JPanel();
        frame.getContentPane().add(panel, BorderLayout.CENTER);

        JButton button = new JButton("Enviar Mensagem");
        panel.add(button);

        Station estacao = new Station();

        // Adicione uma caixa de texto para a mensagem
        estacao.textField = new JTextField(20);
        panel.add(estacao.textField);

        // Inicie a conexão antes de exibir o frame
        if (estacao.iniciarConexao()) {
            button.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    String mensagem = estacao.textField.getText();
                    estacao.enviarMensagem(mensagem);
                }
            });
        } else {
            // Trate o erro de conexão aqui
            System.err.println("Erro ao iniciar a conexão.");
        }

        frame.setVisible(true);
    }

    public boolean iniciarConexao() {
        try {
            Socket socket = new Socket("localhost", 12345);
            outputStream = new ObjectOutputStream(socket.getOutputStream());
            return true;
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }

    public void enviarMensagem(String mensagem) {
        try {
            outputStream.writeObject(mensagem);
            outputStream.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
