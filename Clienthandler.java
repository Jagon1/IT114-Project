import java.io.*;
import java.net.*;

public class Clienthandler implements Runnable {
    private Socket socket;
    private Server server;
    private BufferedReader input;
    private PrintWriter output;
    public Clienthandler(Socket socket, Server server) {
        this.socket = socket;
        this.server = server;
        try {
            input = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            output = new PrintWriter(socket.getOutputStream(), true);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void run() {
        try {
            output.println("Welcome to Battle to Wordle!");
            String message;
            while ((message = input.readLine()) != null) {
                System.out.println("Player guessed: " + message);
                server.handleGuess(this, message);
            }
        } catch (IOException e) {
            System.out.println("Client disconnected.");
        } finally {
            try {
                socket.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public void sendMessage(String message) {
        output.println(message);
    }
}