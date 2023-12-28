package src.java.app;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.ObjectOutputStream;

public class UserInterface {
    private JFrame frame;
    private JTextField textField;
    private JTextArea textArea;
    private ObjectOutputStream outputStream;

    public UserInterface(ObjectOutputStream outputStream) {
        this.outputStream = outputStream;
        initialize();
    }

    private void initialize() {
        frame = new JFrame("User Interface");
        frame.setBounds(100, 100, 400, 300);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        JPanel panel = new JPanel();
        frame.getContentPane().add(panel, BorderLayout.CENTER);
        panel.setLayout(new BorderLayout(0, 0));

        textField = new JTextField();
        panel.add(textField, BorderLayout.NORTH);
        textField.setColumns(10);

        JButton btnEnviar = new JButton("Enviar");
        btnEnviar.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                enviarMensagem();
            }
        });
        panel.add(btnEnviar, BorderLayout.SOUTH);

        textArea = new JTextArea();
        panel.add(new JScrollPane(textArea), BorderLayout.CENTER);

        frame.setVisible(true);
    }

    private void enviarMensagem() {
        String mensagem = textField.getText();
        textArea.append("Você: " + mensagem + "\n");

        try {
            if (outputStream != null) {
                outputStream.writeObject(mensagem);
                outputStream.flush();
            } else {
                System.err.println("O fluxo de saída não está inicializado.");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        textField.setText("");
    }

    public void setOutputStream(ObjectOutputStream outputStream) {
        this.outputStream = outputStream;
    }

    public void exibirMensagemRecebida(String mensagem) {
        textArea.append("Estação: " + mensagem + "\n");
    }
}
