import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.*;
import java.io.*;

public class ContactManager {
    private static final String USER_CREDENTIALS_FILE = "user_credentials.txt";

    private JFrame contactsFrame;
    private DefaultListModel<String> contactsListModel;
    private JList<String> contactsList;
    private String loggedInUser;

    public ContactManager(String loggedInUser) {
        this.loggedInUser = loggedInUser;
        prepareGUI();
        loadContacts();
    }

    private void prepareGUI() {
        // Setting up the frame
        contactsFrame = new JFrame("Contacts");
        contactsFrame.setSize(400, 300);
        centerFrame(contactsFrame);
        contactsFrame.setLayout(new BorderLayout());
        contactsFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        contactsFrame.setResizable(false);
        // Panel for contacts list and chat button
        JPanel contactsPanel = new JPanel(new BorderLayout());
        contactsPanel.setBackground(Color.white);
        ImageIcon icon = new ImageIcon(getClass().getResource("/chatify.png"));
        contactsFrame.setIconImage(icon.getImage());
        // Creating a default list model and JList for contacts
        contactsListModel = new DefaultListModel<>();
        contactsList = new JList<>(contactsListModel);
        contactsList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        contactsList.setBackground(Color.WHITE);
        contactsList.setFont(new Font("Arial", Font.PLAIN, 14));

        // Set cell renderer to customize rendering of list items
        contactsList.setCellRenderer(new MyListCellRenderer());

        JScrollPane contactsScrollPane = new JScrollPane(contactsList);

        // Adding empty border to the contacts scroll pane for padding
        contactsScrollPane.setBorder(new EmptyBorder(10, 10, 10, 10));

        // Chat button with increased height and animation effect
        JButton chatButton = new JButton("Chat");
        chatButton.setBackground(Color.BLUE);
        chatButton.setForeground(Color.WHITE);
        chatButton.setFont(new Font("Arial", Font.BOLD, 14));
        chatButton.setBorder(BorderFactory.createLineBorder(Color.BLACK));
        chatButton.setFocusPainted(false);

        // Set preferred size to increase the height
        chatButton.setPreferredSize(new Dimension(100, 40));

        // Adding animation effect to the chat button
        chatButton.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                chatButton.setBackground(Color.YELLOW);
                chatButton.setForeground(Color.BLACK); // Change text color
            }

            @Override
            public void mouseExited(MouseEvent e) {
                chatButton.setBackground(Color.BLUE);
                chatButton.setForeground(Color.WHITE); // Change text color back
            }
        });


// Action listener for the chat button
        chatButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String selectedContact = contactsList.getSelectedValue();
                if (selectedContact != null) {
                    // Close the contacts window
                    contactsFrame.dispose();
                    openChatWindow(selectedContact);
                } else {
                    JOptionPane.showMessageDialog(contactsFrame, "Please select a contact.");
                }
            }
        });


        // Adding components to the contacts panel
        contactsPanel.add(contactsScrollPane, BorderLayout.CENTER);
        contactsPanel.add(chatButton, BorderLayout.SOUTH);

        // Adding contacts panel to the frame
        contactsFrame.add(contactsPanel);
        contactsFrame.setVisible(true);
    }

    private void loadContacts() {
        try (BufferedReader reader = new BufferedReader(new FileReader(USER_CREDENTIALS_FILE))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(",");
                String username = parts[0];
                if (!username.equals(loggedInUser)) {
                    contactsListModel.addElement(username);
                }
            }
        } catch (IOException e) {
            JOptionPane.showMessageDialog(contactsFrame, "Error while loading contacts: " + e.getMessage());
        }
    }

    private void openChatWindow(String contactName) {
        new ChatWindow(loggedInUser, contactName);
    }

    private void centerFrame(JFrame frame) {
        Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();
        int w = frame.getSize().width;
        int h = frame.getSize().height;
        int x = (dim.width - w) / 2;
        int y = (dim.height - h) / 2;
        frame.setLocation(x, y);
    }

    // Custom cell renderer class to customize rendering of list items
    private class MyListCellRenderer extends DefaultListCellRenderer {
        @Override
        public Component getListCellRendererComponent(JList<?> list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
            super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);

            // Set font and size
            setFont(new Font("Arial", Font.PLAIN, 18));

            // Set background color
            if (isSelected) {
                setBackground(Color.BLUE);
            } else {
                setBackground(Color.WHITE);
            }

            // Set text color
            setForeground(isSelected ? Color.WHITE : Color.BLACK);

            return this;
        }
    }

}
