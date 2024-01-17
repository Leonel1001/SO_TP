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
    private JButton toggleAutoSendButton;
    private volatile boolean autoSendMessages;
    private Thread autoSendMessageThread;

    public SatelliteInterface(LogWindow logWindow, UserManager userManager) {
        this.kernel = new Kernel();
        this.logWindow = logWindow;
        this.userManager = userManager;
        this.loggedInUser = null;
        this.autoSendMessages = false;
        initComponents();
    }

    private void initComponents() {
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setTitle("Satellite Interface");
        setSize(600, 400);
        setLocationRelativeTo(null);

        showInitialPage();
    }

    private void showInitialPage() {
        JPanel panel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 2;
        gbc.insets = new Insets(10, 10, 10, 10);

        JLabel titleLabel = new JLabel("Satellite Interface");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 24));
        panel.add(titleLabel, gbc);

        gbc.gridy++;
        gbc.gridwidth = 1;
        gbc.anchor = GridBagConstraints.EAST;
        panel.add(new JLabel("Username:"), gbc);

        gbc.gridx++;
        JTextField usernameField = new JTextField(20);
        panel.add(usernameField, gbc);

        gbc.gridy++;
        gbc.gridx = 0;
        panel.add(new JLabel("Password:"), gbc);

        gbc.gridx++;
        JPasswordField passwordField = new JPasswordField(20);
        panel.add(passwordField, gbc);

        gbc.gridy++;
        gbc.gridx = 0;
        gbc.anchor = GridBagConstraints.CENTER;
        JButton loginButton = new JButton("Login");
        JButton registerButton = new JButton("Register");
        panel.add(loginButton, gbc);

        gbc.gridx++;
        panel.add(registerButton, gbc);

        loginButton
                .addActionListener(e -> performLogin(usernameField.getText(), new String(passwordField.getPassword())));

        registerButton.addActionListener(e -> showRegisterUserDialog());

        add(panel);
    }

    private void toggleAutoSendMessage() {
        autoSendMessages = !autoSendMessages;

        if (autoSendMessages) {
            startAutoSendMessageThread();
            toggleAutoSendButton.setBackground(Color.green);
        } else {
            stopAutoSendMessageThread();
            toggleAutoSendButton.setBackground(UIManager.getColor("Button.background"));
        }

        toggleAutoSendButton.setText(autoSendMessages ? "Stop Auto Send" : "Start Auto Send");
    }


    private void startAutoSendMessageThread() {
        autoSendMessageThread = new Thread(() -> {
            while (autoSendMessages) {
                try {
                    Thread.sleep(2000);
                    String automaticMessage = "Automatic message from " + loggedInUser + ": " + System.currentTimeMillis();
                    LogMessage logMessage = new LogMessage(automaticMessage, kernel.getMiddleware());
                    kernel.getMiddleware().sendMessage(logMessage);
                    logWindow.addLogMessage(logMessage);

                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        });

        autoSendMessageThread.start();
    }

    private void stopAutoSendMessageThread() {
        if (autoSendMessageThread != null) {
            autoSendMessageThread.interrupt();
            autoSendMessageThread = null;
        }
    }

    private void performLogin(String username, String password) {
        if (userManager.authenticateUser(username, password)) {
            loggedInUser = username;
            JOptionPane.showMessageDialog(null, "Login successful!");
            getContentPane().removeAll();
            showMainInterface();
            revalidate();
        } else {
            JOptionPane.showMessageDialog(null, "Invalid username or password.");
        }
    }


    private void showMainInterface() {
        JTextField messageField = new JTextField();
        JTextArea responseArea = new JTextArea();
        JButton sendButton = new JButton("Send");
        JButton openLogButton = new JButton("Logs");
        toggleAutoSendButton = new JButton("Start/Stop Auto Send");
        toggleAutoSendButton.setBackground(UIManager.getColor("Button.background"));
        JPanel inputPanel = new JPanel();
        inputPanel.setLayout(new BorderLayout());
        inputPanel.add(messageField, BorderLayout.CENTER);
        inputPanel.add(sendButton, BorderLayout.EAST);
       
        

        add(inputPanel, BorderLayout.NORTH);
        add(new JScrollPane(responseArea), BorderLayout.CENTER);

        JPanel buttonPanel = new JPanel();
        buttonPanel.add(toggleAutoSendButton);
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
                    responseArea.append(message + "\n");
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

        toggleAutoSendButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                toggleAutoSendMessage();
            }
        });

        toggleAutoSendButton.setText(autoSendMessages ? "Stop Auto Send" : "Start Auto Send");

    }

    private void showRegisterUserDialog() {
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

            if (userManager.authenticateUser(username, password)) {
                JOptionPane.showMessageDialog(null, "User already exists. Create a different user.");
            } else {
                userManager.addUser(new User(username, password));
                JOptionPane.showMessageDialog(null, "User sucessfully created");
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
        try {
            // Configura o Look and Feel Nimbus
            UIManager.setLookAndFeel("javax.swing.plaf.nimbus.NimbusLookAndFeel");

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
        } catch (UnsupportedLookAndFeelException | ClassNotFoundException | InstantiationException
                | IllegalAccessException e) {
            e.printStackTrace();
        }
    }
}
