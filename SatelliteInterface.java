import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.*;
import java.util.ArrayList;
import java.util.concurrent.LinkedBlockingQueue;


public class SatelliteInterface extends JFrame {
    private final Kernel kernel;
    private final UserManager userManager;
    private String loggedInUser;
    private JButton toggleAutoSendButton;
    private volatile boolean autoSendMessages;
    private AutomaticMessage autoMessageThread;
    public LinkedBlockingQueue<String> messageQueue;
    private MemoryUnit memoryUnit;
    private ThreadCounter threadCounter; 
    private JLabel activeThreadLabel;
    private final Bomber bomber;


    public SatelliteInterface(LogWindow logWindow, UserManager userManager) {
        this.kernel = new Kernel();
        this.userManager = userManager;
        this.loggedInUser = null;
        this.autoSendMessages = false;
        this.messageQueue = new LinkedBlockingQueue<>();
        this.memoryUnit = new MemoryUnit();
        this.bomber = new Bomber(null);
        this.threadCounter = new ThreadCounter(kernel.getCpu(), bomber, autoMessageThread); 
        
        initComponents();
    }

    private void initComponents() {
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setTitle("Satellite Interface");
        setSize(600, 400);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

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
        add(panel, BorderLayout.CENTER);
    }

    private void openChartPage() {
        ChartData chartExample = new ChartData("Exemplo de Gráfico de Barras", memoryUnit);
        chartExample.setSize(800, 600);
        chartExample.setLocationRelativeTo(null);
        chartExample.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
        chartExample.setVisible(true);
    }

    private void showSatellitePage() {
        JFrame satelliteFrame = new JFrame("Satellite Page");
        satelliteFrame.setSize(800, 600);
        satelliteFrame.setLocationRelativeTo(null);

        JPanel satellitePanel = new JPanel(new BorderLayout());

        JTextArea satelliteTextArea = new JTextArea();
        satelliteTextArea.setEditable(false);
        JScrollPane scrollPane = new JScrollPane(satelliteTextArea);

        satellitePanel.add(scrollPane, BorderLayout.CENTER);

        JButton refreshButton = new JButton("Refresh");
        satellitePanel.add(refreshButton, BorderLayout.SOUTH);

        refreshButton.addActionListener(e -> refreshSatelliteText(satelliteTextArea));

        Timer autoRefreshTimer = new Timer(1000, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                refreshSatelliteText(satelliteTextArea);
            }
        });
        autoRefreshTimer.start();

        refreshSatelliteText(satelliteTextArea);

        satelliteFrame.getContentPane().add(satellitePanel);
        satelliteFrame.setVisible(true);
    }

    private void refreshSatelliteText(JTextArea satelliteTextArea) {
        try (BufferedReader br = new BufferedReader(new FileReader("log_messages.txt"))) {
            StringBuilder sb = new StringBuilder();
            String line;

            while ((line = br.readLine()) != null) {
                sb.append(line).append("\n");
            }

            satelliteTextArea.setText(sb.toString());
        } catch (IOException e) {
            e.printStackTrace();
            satelliteTextArea.setText("Erro ao ler o arquivo de log.");
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
        JButton cleanButton = new JButton("Clean");

        Timer autoRefreshTimer = new Timer(100, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                refreshResponseArea(responseArea);
                updateActiveThreadLabel(); 
            }
        });

        autoRefreshTimer.start();

        JPanel panel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 2;
        gbc.insets = new Insets(10, 10, 10, 10);

        JLabel titleLabel = new JLabel("Main Interface");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 24));
        panel.add(titleLabel, gbc);

        activeThreadLabel = new JLabel("Active Threads: " + threadCounter.getActiveThreadCount());
        gbc.gridy++;
        panel.add(activeThreadLabel, gbc);

        // Inicie a threadCounter
        startThreadCounter();

        toggleAutoSendButton = new JButton("Start Auto Send");
        toggleAutoSendButton.setBackground(UIManager.getColor("Button.background"));
        JButton satelliteButton = new JButton("Satellite");
        JPanel inputPanel = new JPanel();
        inputPanel.setLayout(new BorderLayout());
        inputPanel.add(messageField, BorderLayout.CENTER);
        inputPanel.add(sendButton, BorderLayout.EAST);

        add(inputPanel, BorderLayout.NORTH);
        add(new JScrollPane(responseArea), BorderLayout.CENTER);

        JPanel buttonPanel = new JPanel();
        buttonPanel.add(toggleAutoSendButton);
        buttonPanel.add(cleanButton);
        buttonPanel.add(satelliteButton);
        add(buttonPanel, BorderLayout.SOUTH);
        add(panel, BorderLayout.EAST);

        Middleware middleware = kernel.getMiddleware();

        JButton bombardearButton = new JButton("Bombardear Mensagens");
        buttonPanel.add(bombardearButton);

        cleanButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                responseArea.setText("");
                cleanLogMessagesFile();
            }
        });
        bombardearButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (autoSendMessages) {
                    JOptionPane.showMessageDialog(null,
                            "Por favor, pare o envio automático antes de bombardear mensagens.");
                    return;
                }

                Middleware middleware = kernel.getMiddleware();

                Bomber bombardeador = new Bomber(middleware);
                bombardeador.iniciarBombardeio();
            }
        });
        sendButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (loggedInUser != null) {
                    String messageContent = messageField.getText();
                    String fullMessage = "Mensagem de " + loggedInUser + ": " + messageContent;
                    middleware.messageManager(fullMessage);
                    messageField.setText("");
                    responseArea.append(loggedInUser + ": " + messageContent + "\n");
                } else {
                    JOptionPane.showMessageDialog(null, "To send a message you need to be logged in.");
                }
            }
        });

        toggleAutoSendButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                toggleAutoSendMessage();
            }
        });
        satelliteButton.addActionListener(e -> showSatellitePage());

        satelliteButton.addActionListener(e -> showSatellitePage());

        JButton openChartButton = new JButton("Open Chart Page");
        buttonPanel.add(openChartButton);

        openChartButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                openChartPage();
            }
        });
    }

    private void updateActiveThreadLabel() {
        activeThreadLabel.setText("Active Threads: " + threadCounter.getActiveThreadCount());
    }

    private void startThreadCounter() {
        threadCounter.start();
    }

    private void cleanLogMessagesFile() {
        try (PrintWriter writer = new PrintWriter("log_messages.txt")) {
            writer.print(""); // Limpa o conteúdo do arquivo
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            // Trate a exceção se o arquivo não puder ser encontrado
        }
    }

    private void refreshResponseArea(JTextArea responseArea) {
        try (BufferedReader br = new BufferedReader(new FileReader("log_messages.txt"))) {
            StringBuilder sb = new StringBuilder();
            String line;

            while ((line = br.readLine()) != null) {
                sb.append(line).append("\n");
            }

            responseArea.setText(sb.toString());
        } catch (IOException e) {
            e.printStackTrace();
            responseArea.setText("Erro ao ler o arquivo de log.");
        }
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
        if (autoMessageThread == null) {
            Middleware middleware = kernel.getMiddleware();
            autoMessageThread = new AutomaticMessage(middleware, threadCounter);
            autoMessageThread.start();
        }
    }

    private void stopAutoSendMessageThread() {
        if (autoMessageThread != null) {
            autoMessageThread.interrupt();
            autoMessageThread = null;
        }
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
                JOptionPane.showMessageDialog(null, "User successfully created");
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
