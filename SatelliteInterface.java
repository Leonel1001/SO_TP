import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.*;
import java.util.ArrayList;

public class SatelliteInterface extends JFrame {
    private final Kernel kernel;
    private final LogWindow logWindow;
    private final UserManager userManager;
    private String loggedInUser;

    public SatelliteInterface(LogWindow logWindow, UserManager userManager) {
        this.kernel = new Kernel();
        this.logWindow = logWindow;
        this.userManager = userManager;
        this.loggedInUser = null; // Inicialmente, nenhum usuário está logado
        initComponents();
    }

    private void initComponents() {
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setTitle("Satellite Interface");
        setSize(400, 200);
        setLocationRelativeTo(null);

        // Mostrar a página inicial com a opção de login e botão de registro
        showInitialPage();
    }

    private void showInitialPage() {
        // Página inicial com opção de login e botão de registro
        JPanel panel = new JPanel(new GridLayout(4, 2));
        JTextField usernameField = new JTextField();
        JPasswordField passwordField = new JPasswordField();
        JButton loginButton = new JButton("Login");
        JButton registerButton = new JButton("Register");

        panel.add(new JLabel("Username:"));
        panel.add(usernameField);
        panel.add(new JLabel("Password:"));
        panel.add(passwordField);
        panel.add(loginButton);
        panel.add(registerButton);

        loginButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                performLogin(usernameField.getText(), new String(passwordField.getPassword()));
            }
        });

        registerButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                showRegisterUserDialog();
            }
        });

        add(panel);
    }

    private void performLogin(String username, String password) {
        // Verifica as credenciais do usuário
        if (userManager.authenticateUser(username, password)) {
            loggedInUser = username; // Define o usuário como logado
            JOptionPane.showMessageDialog(null, "Login successful!");

            // Remove a página inicial e mostra a interface principal
            getContentPane().removeAll();
            showMainInterface();
            revalidate(); // Atualiza o layout
        } else {
            JOptionPane.showMessageDialog(null, "Invalid username or password.");
        }
    }

    private void showMainInterface() {
        // Lógica para mostrar a interface principal com envio de mensagens, etc.
        // Pode ser semelhante ao método initComponents() original
        // Por exemplo:
        JTextField messageField = new JTextField();
        JTextArea responseArea = new JTextArea();
        JButton sendButton = new JButton("Send");
        JButton openLogButton = new JButton("Logs");

        JPanel inputPanel = new JPanel();
        inputPanel.setLayout(new BorderLayout());
        inputPanel.add(messageField, BorderLayout.CENTER);
        inputPanel.add(sendButton, BorderLayout.EAST);

        add(inputPanel, BorderLayout.NORTH);
        add(new JScrollPane(responseArea), BorderLayout.CENTER);

        JPanel buttonPanel = new JPanel();
        buttonPanel.add(openLogButton);
        add(buttonPanel, BorderLayout.SOUTH);

        Cpu cpu = kernel.getCpu();
        Middleware middleware = kernel.getMiddleware();
        cpu.setMiddleware(middleware);

        sendButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (loggedInUser != null) {
                    String message = "Message from  " + loggedInUser + ": " + messageField.getText();
                    LogMessage logMessage = new LogMessage(message, kernel.getMiddleware());
                    kernel.getMiddleware().sendMessage(logMessage);
                    messageField.setText("");

                    // Atualizar o campo de mensagens
                    responseArea.append(message + "\n");

                    // Salvar a mensagem nas logs
                    logWindow.addLogMessage(logMessage);
                } else {
                    JOptionPane.showMessageDialog(null, "To send a message you need to be logged in.");
                }
            }
        });

        openLogButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                logWindow.setVisible(true);
            }
        });
    }

    private void showRegisterUserDialog() {
        // Caixa de diálogo para cadastrar novo usuário
        JPanel panel = new JPanel(new GridLayout(3, 2));
        JTextField usernameField = new JTextField();
        JPasswordField passwordField = new JPasswordField();
        panel.add(new JLabel("New user:"));
        panel.add(usernameField);
        panel.add(new JLabel("Password"));
        panel.add(passwordField);

        int result = JOptionPane.showConfirmDialog(null, panel, "Create a new User", JOptionPane.OK_CANCEL_OPTION);
        if (result == JOptionPane.OK_OPTION) {
            String username = usernameField.getText();
            String password = new String(passwordField.getPassword());

            // Verifica se o nome de usuário já existe
            if (userManager.authenticateUser(username, password)) {
                JOptionPane.showMessageDialog(null, "User already exists. Create a different user.");
            } else {
                // Adiciona o novo usuário
                userManager.addUser(new User(username, password));
                JOptionPane.showMessageDialog(null, "User sucessfully created");

                // Chama saveUsers() após adicionar o usuário
                saveUsers();
            }
        }
    }

    private void saveUsers() {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream("users.dat"))) {
            oos.writeObject(new ArrayList<>(userManager.getUsers()));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            try {
                LogWindow logWindow = new LogWindow();
                UserManager userManager = new UserManager();
                SatelliteInterface interfaceFrame = new SatelliteInterface(logWindow, userManager);
                interfaceFrame.setVisible(true);
                interfaceFrame.kernel.start();
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }
}
