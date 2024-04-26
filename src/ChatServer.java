import java.io.*;
import java.net.*;
import java.util.*;
import javax.swing.*;

public class ChatServer {
    private static final int PORT = 12345;
    private List<ClientHandler> clients = new ArrayList<>();

    public ChatServer() {
        try (ServerSocket serverSocket = new ServerSocket(PORT)) {
            System.out.println("Server started. Waiting for clients...");

            while (true) {
                Socket clientSocket = serverSocket.accept();
                System.out.println("Client connected: " + clientSocket.getInetAddress().getHostName());
                ClientHandler clientHandler = new ClientHandler(clientSocket);
                clients.add(clientHandler);
                clientHandler.start();
            }
        } catch (IOException e) {
            JOptionPane.showMessageDialog(null, "Error starting server: " + e.getMessage());
        }
    }

    private class ClientHandler extends Thread {
        private Socket clientSocket;
        private PrintWriter out;

        public ClientHandler(Socket clientSocket) {
            this.clientSocket = clientSocket;
        }

        public void run() {
            try {
                BufferedReader in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
                out = new PrintWriter(clientSocket.getOutputStream(), true);
                String message;
                while ((message = in.readLine()) != null) {
                    if (message.startsWith("FILE:")) {
                        receiveFile(message.substring(5));
                    } else if (message.startsWith("IMAGE:")) {
                        receiveImage(message.substring(6));
                    } else {
                        System.out.println(message);
                        sendMessageWithEmojis(message);
                    }
                }
            } catch (IOException e) {
                JOptionPane.showMessageDialog(null, "Error receiving message: " + e.getMessage());
            }
        }

        private void broadcastMessage(String message) {
            for (ClientHandler client : clients) {
                if (client != this) {
                    client.sendMessage(message);
                }
            }
        }

        private void sendMessage(String message) {
            if (out != null) {
                out.println(message);
            }
        }

        private void sendMessageWithEmojis(String message) {
            // Add your emoji handling logic here
            broadcastMessage(message); // For now, just broadcast the message without handling emojis
        }

        private void receiveFile(String filename) throws IOException {
            byte[] buffer = new byte[8192];
            int bytesRead;
            InputStream fileStream = clientSocket.getInputStream();
            FileOutputStream fileOutputStream = new FileOutputStream(filename);
            while ((bytesRead = fileStream.read(buffer)) != -1) {
                fileOutputStream.write(buffer, 0, bytesRead);
            }
            fileOutputStream.close();
        }

        private void receiveImage(String filename) throws IOException {
            receiveFile(filename); // For simplicity, treating images the same as files
        }
    }

    public static void main(String[] args) {
        new ChatServer();
    }
}
