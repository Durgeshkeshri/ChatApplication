import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.*;

public class ChatifyApp {
    private static final String FILE_NAME = "user_credentials.txt";
    private static final int MIN_USERNAME_LENGTH = 8;
    private static final int MIN_PASSWORD_LENGTH = 6;

    private JFrame mainFrame;
    private JFrame registerFrame;
    private JFrame loginFrame;
    private JTextField registerNameField;
    private JPasswordField registerPassField;
    private JTextField loginNameField;
    private JPasswordField loginPassField;

    private String loggedInUser;

    public ChatifyApp() {
        prepareGUI();
    }

    private void prepareGUI() {
        try {
            UIManager.setLookAndFeel("javax.swing.plaf.nimbus.NimbusLookAndFeel");
        } catch (Exception e) {
            e.printStackTrace();
        }

        mainFrame = new JFrame("Chatify");
        mainFrame.setSize(400, 200);
        mainFrame.setResizable(false); // Disable frame resizing
        centerFrame(mainFrame);
        mainFrame.setLayout(new BorderLayout());
        mainFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        mainFrame.setResizable(false); // Disable maximize button

        // Set application icon
        ImageIcon icon = new ImageIcon(getClass().getResource("/chatify.png"));
        mainFrame.setIconImage(icon.getImage());

        JLabel titleLabel = new JLabel("Chatify", SwingConstants.CENTER);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 40));
        titleLabel.setBorder(BorderFactory.createEmptyBorder(20, 0, 0, 0));
        titleLabel.setForeground(Color.BLUE);
        mainFrame.add(titleLabel, BorderLayout.NORTH);

        JPanel buttonPanel = new JPanel(new GridLayout(1, 2, 20, 20));
        buttonPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        JButton registerButton = new JButton("Register");
        JButton loginButton = new JButton("Login");
        registerButton.setBackground(Color.GREEN);
        loginButton.setBackground(Color.ORANGE);
        registerButton.setFont(new Font("Arial", Font.BOLD, 16));
        loginButton.setFont(new Font("Arial", Font.BOLD, 16));

        // Adding animation effect to buttons
        registerButton.addMouseListener(new MouseAdapter() {
            public void mouseEntered(MouseEvent e) {
                registerButton.setBackground(Color.YELLOW);
            }

            public void mouseExited(MouseEvent e) {
                registerButton.setBackground(Color.GREEN);
            }
        });

        loginButton.addMouseListener(new MouseAdapter() {
            public void mouseEntered(MouseEvent e) {
                loginButton.setBackground(Color.YELLOW);
            }

            public void mouseExited(MouseEvent e) {
                loginButton.setBackground(Color.ORANGE);
            }
        });

        buttonPanel.add(registerButton);
        buttonPanel.add(loginButton);
        mainFrame.add(buttonPanel, BorderLayout.CENTER);

        registerButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                showRegisterWindow();
            }
        });

        loginButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                showLoginWindow();
            }
        });

        mainFrame.getRootPane().setDefaultButton(loginButton); // Set default button for main frame
        mainFrame.setVisible(true);
        ChatServerThread serverThread = new ChatServerThread();
        serverThread.start();
    }


    private void showRegisterWindow() {
        registerFrame = new JFrame("Register");
        registerFrame.setSize(400, 170);
        registerFrame.setResizable(false); // Disable maximize button
        centerFrame(registerFrame);
        registerFrame.setLayout(new GridLayout(3, 1));
        registerFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        ImageIcon icon = new ImageIcon(getClass().getResource("/chatify.png"));
        registerFrame.setIconImage(icon.getImage());

        JPanel contentPane = (JPanel) registerFrame.getContentPane();
        contentPane.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10)); // Add top margin of 10 pixels

        JLabel nameLabel = new JLabel("Name:", SwingConstants.CENTER);
        nameLabel.setFont(new Font("Arial", Font.BOLD, 20));
        JLabel passLabel = new JLabel("Password:", SwingConstants.CENTER);
        passLabel.setFont(new Font("Arial", Font.BOLD, 20));
        registerNameField = new JTextField();
        registerPassField = new JPasswordField();
        JButton registerButton = new JButton("Register");

        registerButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                registerUser();
            }
        });

        registerNameField.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                registerUser();
            }
        });

        registerPassField.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                registerUser();
            }
        });

        registerFrame.add(nameLabel);
        registerFrame.add(registerNameField);
        registerFrame.add(passLabel);
        registerFrame.add(registerPassField);
        registerFrame.add(new JLabel());
        registerFrame.add(registerButton);

        // Adding animation effect to register button
        registerButton.addMouseListener(new MouseAdapter() {
            public void mouseEntered(MouseEvent e) {
                registerButton.setBackground(Color.YELLOW);
            }

            public void mouseExited(MouseEvent e) {
                registerButton.setBackground(Color.GREEN);
            }
        });

        registerFrame.getRootPane().setDefaultButton(registerButton); // Set default button for register frame
        registerFrame.setVisible(true);
    }

    private void showLoginWindow() {
        loginFrame = new JFrame("Login");
        loginFrame.setSize(400, 170);
        loginFrame.setResizable(false); // Disable maximize button
        centerFrame(loginFrame);
        loginFrame.setLayout(new GridLayout(3, 1));
        loginFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        ImageIcon icon = new ImageIcon(getClass().getResource("/chatify.png"));
        loginFrame.setIconImage(icon.getImage());
        JPanel contentPane = (JPanel) loginFrame.getContentPane();
        contentPane.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10)); // Add top margin of 10 pixels

        JLabel nameLabel = new JLabel("Name:", SwingConstants.CENTER);
        nameLabel.setFont(new Font("Arial", Font.BOLD, 20));
        JLabel passLabel = new JLabel("Password:", SwingConstants.CENTER);
        passLabel.setFont(new Font("Arial", Font.BOLD, 20));
        loginNameField = new JTextField();
        loginPassField = new JPasswordField();
        JButton loginButton = new JButton("Login");

        loginButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                loginUser();
            }
        });

        loginNameField.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                loginUser();
            }
        });

        loginPassField.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                loginUser();
            }
        });

        loginFrame.add(nameLabel);
        loginFrame.add(loginNameField);
        loginFrame.add(passLabel);
        loginFrame.add(loginPassField);
        loginFrame.add(new JLabel());
        loginFrame.add(loginButton);

        // Adding animation effect to login button
        loginButton.addMouseListener(new MouseAdapter() {
            public void mouseEntered(MouseEvent e) {
                loginButton.setBackground(Color.YELLOW);
            }

            public void mouseExited(MouseEvent e) {
                loginButton.setBackground(Color.ORANGE);
            }
        });

        loginFrame.getRootPane().setDefaultButton(loginButton); // Set default button for login frame
        loginFrame.setVisible(true);
    }

    private void registerUser() {
        String newUsername = registerNameField.getText();
        String newPassword = new String(registerPassField.getPassword());

        if (newUsername.trim().isEmpty() || newPassword.trim().isEmpty()) {
            JOptionPane.showMessageDialog(registerFrame, "Please enter both username and password.");
            return;
        }

        if (newUsername.length() < MIN_USERNAME_LENGTH) {
            JOptionPane.showMessageDialog(registerFrame, "Username should be at least " + MIN_USERNAME_LENGTH + " characters long.");
            return;
        }

        if (newPassword.length() < MIN_PASSWORD_LENGTH) {
            JOptionPane.showMessageDialog(registerFrame, "Password should be at least " + MIN_PASSWORD_LENGTH + " characters long.");
            return;
        }

        if (newUsername.equals(newPassword)) {
            JOptionPane.showMessageDialog(registerFrame, "Username and password cannot be the same.");
            return;
        }

        try (BufferedReader reader = new BufferedReader(new FileReader(FILE_NAME))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(",");
                if (parts.length >= 1 && parts[0].equals(newUsername)) {
                    JOptionPane.showMessageDialog(registerFrame, "Username already exists! Please choose a different one.");
                    return;
                }
            }
        } catch (IOException e) {
            JOptionPane.showMessageDialog(registerFrame, "Error while checking existing usernames: " + e.getMessage());
            return;
        }

        try (FileWriter writer = new FileWriter(FILE_NAME, true);
             BufferedWriter bw = new BufferedWriter(writer);
             PrintWriter out = new PrintWriter(bw)) {
            out.println(newUsername + "," + newPassword);
            JOptionPane.showMessageDialog(registerFrame, "User registered successfully!");
            registerFrame.dispose();
        } catch (IOException e) {
            JOptionPane.showMessageDialog(registerFrame, "Error while registering user: " + e.getMessage());
        }
    }

    private void loginUser() {
        String username = loginNameField.getText();
        String password = new String(loginPassField.getPassword());
        boolean found = false;

        if (username.trim().isEmpty() || password.trim().isEmpty()) {
            JOptionPane.showMessageDialog(loginFrame, "Please enter both username and password.");
            return;
        }

        if (username.length() < MIN_USERNAME_LENGTH) {
            JOptionPane.showMessageDialog(loginFrame, "Username should be at least " + MIN_USERNAME_LENGTH + " characters long.");
            return;
        }

        if (password.length() < MIN_PASSWORD_LENGTH) {
            JOptionPane.showMessageDialog(loginFrame, "Password should be at least " + MIN_PASSWORD_LENGTH + " characters long.");
            return;
        }

        try (BufferedReader reader = new BufferedReader(new FileReader(FILE_NAME))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(",");
                if (parts.length >= 2 && parts[0].equals(username) && parts[1].equals(password)) {
                    found = true;
                    break;
                }
            }
            if (found) {
                JOptionPane.showMessageDialog(loginFrame, "Login successful!");
                loggedInUser = username;
                openContactsWindow();
                loginFrame.dispose();
            } else {
                JOptionPane.showMessageDialog(loginFrame, "Invalid username or password!");
            }
        } catch (IOException e) {
            JOptionPane.showMessageDialog(loginFrame, "Error while reading user credentials: " + e.getMessage());
        }
    }

    private void openContactsWindow() {
        new ContactManager(loggedInUser);
    }

    private void centerFrame(JFrame frame) {
        Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();
        int w = frame.getSize().width;
        int h = frame.getSize().height;
        int x = (dim.width - w) / 2;
        int y = (dim.height - h) / 2;
        frame.setLocation(x, y);
    }
    // Inner class to start the chat server in a separate thread
    private class ChatServerThread extends Thread {
        @Override
        public void run() {
            new ChatServer();
        }
    }
    public static void main(String[] args) {
        new ChatifyApp();
    }
}
