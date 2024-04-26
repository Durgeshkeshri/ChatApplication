import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.net.*;

public class ChatWindow {
    private String sender;
    private String receiver;
    private Socket socket;
    private BufferedReader in;
    private PrintWriter out;
    private JFrame chatFrame;
    private JTextArea chatTextArea;
    private JTextField inputField;

    public ChatWindow(String sender, String receiver) {
        this.sender = sender;
        this.receiver = receiver;
        prepareGUI();

        try {
            socket = new Socket("localhost", 12345); // Connect to the server running on localhost
            in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            out = new PrintWriter(socket.getOutputStream(), true);

            // Start a new thread to continuously receive messages from the server
            new Thread(new Runnable() {
                @Override
                public void run() {
                    try {
                        String message;
                        while ((message = in.readLine()) != null) {
                            chatTextArea.append(message + "\n"); // Display received messages in chat window
                        }
                    } catch (IOException e) {
                        JOptionPane.showMessageDialog(null, "Error receiving message: " + e.getMessage());
                    }
                }
            }).start();
        } catch (IOException e) {
            JOptionPane.showMessageDialog(null, "Error connecting to server: " + e.getMessage());
        }
    }

    private void prepareGUI() {
        chatFrame = new JFrame("Chat with " + receiver);
        chatFrame.setSize(400, 300);
        centerFrame(chatFrame);
        chatFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        chatFrame.setResizable(false);
        ImageIcon icon = new ImageIcon(getClass().getResource("/profile.png"));
        chatFrame.setIconImage(icon.getImage());
        JPanel mainPanel = new JPanel(new BorderLayout());

        chatTextArea = new JTextArea();
        chatTextArea.setEditable(false);
        chatTextArea.setBackground(Color.WHITE);
        chatTextArea.setForeground(Color.BLACK);
        JScrollPane scrollPane = new JScrollPane(chatTextArea);
        mainPanel.add(scrollPane, BorderLayout.CENTER);

        JPanel buttonPanel = new JPanel(new GridLayout(1, 3));
        buttonPanel.setBackground(Color.WHITE);

        JButton emojiButton = new JButton("😄 Emoji");
        JButton fileButton = new JButton("📁 File");
        JButton clearButton = new JButton("🗑️ Clear"); // New clear chat button

        buttonPanel.add(emojiButton);
        buttonPanel.add(fileButton);
        buttonPanel.add(clearButton); // Add clear button to the panel

        emojiButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String[] emojis = {"😀", "😂", "😃", "😛", "😮", "😐", "😕"};

                String selectedEmoji = (String) JOptionPane.showInputDialog(chatFrame, "Select Emoji:", "Emoji Picker", JOptionPane.PLAIN_MESSAGE, null, emojis, emojis[0]);
                if (selectedEmoji != null && !selectedEmoji.isEmpty()) {
                    sendMessage(selectedEmoji);
                }
            }
        });

        fileButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JFileChooser fileChooser = new JFileChooser();
                int returnValue = fileChooser.showOpenDialog(chatFrame);
                if (returnValue == JFileChooser.APPROVE_OPTION) {
                    File selectedFile = fileChooser.getSelectedFile();
                    sendFile(selectedFile.getAbsolutePath());
                }
            }
        });

        clearButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                chatTextArea.setText(""); // Clear the chat text area
            }
        });

        JPanel inputPanel = new JPanel(new BorderLayout());
        inputPanel.setBackground(Color.WHITE);

        inputField = new JTextField();
        inputField.setBackground(Color.WHITE);
        inputField.setForeground(Color.BLACK);
        inputPanel.add(inputField, BorderLayout.CENTER);

        // Add ActionListener to inputField
        inputField.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String message = inputField.getText();
                if (!message.isEmpty()) {
                    sendMessage(message);
                    inputField.setText("");
                }
            }
        });

        JButton sendButton = new JButton("➡️ Send");
        sendButton.setBackground(Color.BLUE); // Blue color for the button
        sendButton.setForeground(Color.WHITE);
        sendButton.setFocusPainted(false); // Remove button border

        sendButton.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                sendButton.setBackground(Color.YELLOW); // Change background color on hover
                sendButton.setForeground(Color.BLACK); // Change text color on hover
            }

            @Override
            public void mouseExited(MouseEvent e) {
                sendButton.setBackground(Color.BLUE); // Restore original background color
                sendButton.setForeground(Color.WHITE); // Restore original text color
            }
        });

        sendButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String message = inputField.getText();
                if (!message.isEmpty()) {
                    sendMessage(message);
                    inputField.setText("");
                }
            }
        });

        inputPanel.add(sendButton, BorderLayout.EAST);

        mainPanel.add(buttonPanel, BorderLayout.NORTH);
        mainPanel.add(inputPanel, BorderLayout.SOUTH);

        chatFrame.add(mainPanel);
        chatFrame.setVisible(true);
    }

    private void sendMessage(String message) {
        if (out != null) {
            out.println(sender + ": " + message);
            String displayMessage = "You: " + message;
            chatTextArea.append(displayMessage + "\n");
        } else {
            JOptionPane.showMessageDialog(null, "Error: Connection to server is not established.");
        }
    }

    private void sendFile(String filePath) {
        sendMessage("FILE:" + filePath);
    }

    private void centerFrame(JFrame frame) {
        Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();
        int w = frame.getSize().width;
        int h = frame.getSize().height;
        int x = (dim.width - w) / 2;
        int y = (dim.height - h) / 2;
        frame.setLocation(x, y);
    }
}
