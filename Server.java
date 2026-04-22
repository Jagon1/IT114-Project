import java.io.*;
import java.net.*;

public class Server {
    private ServerSocket serverSocket;
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
            // Accepts a client for now
            Socket clientSocket = serverSocket.accept();
            System.out.println("Client connected");

            // Input/output streams
            BufferedReader input = new BufferedReader(
                    new InputStreamReader(clientSocket.getInputStream()));
            PrintWriter output = new PrintWriter(
                    clientSocket.getOutputStream(), true);
            output.println("Battle to Wordle!");
            String message;

            // Reads messages from clients
            while ((message = input.readLine()) != null) {
                System.out.println("Received: " + message);
            }
            clientSocket.close();
            System.out.println("Client disconnected.");

        } catch (IOException e) {
            System.out.println("Connection error.");
            e.printStackTrace();
        }
    }
    public static void main(String[] args) {
        Server server = new Server(12345);
        server.start();
    }
}