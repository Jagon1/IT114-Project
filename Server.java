import java.io.*;
import java.net.*;
import java.util.*;

public class Server {
    private ServerSocket serverSocket;
    private int timeLeft;
    private boolean roundActive;
    private List<ClientConnection> clients = new ArrayList<>();
    public Server(int port) {
        try {
            serverSocket = new ServerSocket(port);
            System.out.println("Server started on port " + port);
        } catch (IOException e) {
            System.out.println("Error starting server.");
            e.printStackTrace();
        }
        
    }

    public void start() {
        try {
            System.out.println("Waiting for players...");
            // Accepts a client for now
            Socket clientSocket = serverSocket.accept();
            System.out.println("Player connected");

            // Input/output streams
            BufferedReader input = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
            PrintWriter output = new PrintWriter(clientSocket.getOutputStream(), true);
            // Add players to array of clients
            clients.add(new ClientConnection(clientSocket, input, output));
            output.println("Battle to Wordle!");

            // Starts a countdown of 30 seconds to guess something
            startCountdown(30);
            new Thread(() -> {
                try {
                    String message;
                    while ((message = input.readLine()) != null) {
                        System.out.println("Received: " + message);
                    }
                } catch (IOException e) {
                    System.out.println("Client error or disconnected.");
                }
            }).start();
        } catch (IOException e) {
            System.out.println("Connection error.");
            e.printStackTrace();
        }
    }
    class ClientConnection {
        Socket socket;
        BufferedReader input;
        PrintWriter output;
        ClientConnection(Socket socket, BufferedReader input, PrintWriter output) {
            this.socket = socket;
            this.input = input;
            this.output = output;
        }
    }
     public void startCountdown(int seconds) {
        // New thread to not freeze server
        new Thread(() -> {
            // Synchronize for deliverable and lock timeLeft and roundActive variables
            synchronized (this) {
                timeLeft = seconds;
                roundActive = true;
            }
            while (true) {
                try {
                    Thread.sleep(1000);
                    // Synchronize to safely update timeLeft and check roundActive
                    synchronized (this) {
                        if (!roundActive) {
                            break;
                        }
                        // This will decrease the time by one second
                        timeLeft--;
                        broadcast("TIME " + timeLeft);
                        System.out.println("Time left: " + timeLeft);
                        if (timeLeft <= 0) {
                            roundActive = false;
                            broadcast("TIME_IS_UP");
                            break;
                        }
                    }
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }
    // Broadcasts a message to all players
    public void broadcast(String message) {
        for (ClientConnection client : clients) {
            client.output.println(message);
        }
    }
    public static void main(String[] args) {
        Server server = new Server(12345);
        server.start();
    }
}