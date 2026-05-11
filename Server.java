import java.io.*;
import java.net.*;
import java.util.*;

public class Server {

    private ServerSocket serverSocket;

    // Store all connected clients
    private List<Clienthandler> clients = new ArrayList<>();

    public Server(int port) {
        try {
            serverSocket = new ServerSocket(port);
            System.out.println("Server started on port " + port);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void start() {
        while (true) {
            try {
                System.out.println("Waiting for players...");
                Socket socket = serverSocket.accept();
                System.out.println("Player connected!");
                Clienthandler clientHandler = new Clienthandler(socket, this); 
                clients.add(clientHandler);
                Thread thread = new Thread(clientHandler);
                thread.start();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    // Send message to ALL players
    public void broadcast(String message) {
        for (Clienthandler client : clients) {
            client.sendMessage(message);
        }
    }

    public static void main(String[] args) {
        Server server = new Server(12345);
        server.start();
    }
}