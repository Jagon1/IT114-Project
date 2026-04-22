import java.io.*;
import java.net.*;

public class Client {
    private Socket socket;
    private BufferedReader input;
    private PrintWriter output;

    public Client(String host, int port) {
        try {
            socket = new Socket(host, port);
            input = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            output = new PrintWriter(socket.getOutputStream(), true);
            System.out.println("Connected to server " + host + ":" + port);
        } catch (IOException e) {
            System.out.println("Error connecting to server");
            e.printStackTrace();
        }
    }

    public void sendGuess(String guess) {
        output.println(guess);
    }

    public void listenForUpdates() {
        Thread listener = new Thread(() -> {
            try {
                String response;
                while ((response = input.readLine()) != null) {
                    System.out.println(response);
                }
            } catch (IOException e) {
                System.out.println("Disconnected from server.");
            }
        });
        listener.start();
    }
}