import java.io.*;
import java.net.*;
import java.util.*;

public class Server {
    private String secretWord;
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
    public void startGame() {
        String[] words = {
            "apple", "grape", "brick", "chair", "plant",
            "stone", "flame", "crane", "blush", "storm"
        };

        Random rand = new Random();
        secretWord = words[rand.nextInt(words.length)];
        System.out.println("DEBUG (server only): " + secretWord);
    }

    // Send message to ALL players
    public void broadcast(String message) {
        for (Clienthandler client : clients) {
            client.sendMessage(message);
        }
    }
    public void handleGuess(Clienthandler player, String guess) {
        System.out.println("Guess received: " + guess);
        if (guess.equalsIgnoreCase(secretWord)) {
            player.sendMessage("Correct! You win!");
            broadcast("A player has guessed the word!");
        } else {
            String feedback = guessFeedback(guess);
            player.sendMessage("Wrong guess: " + feedback);
        }
        broadcast("A player has made a guess!");
    }
    private String guessFeedback(String guess) {
        StringBuilder result = new StringBuilder();
        for (int i = 0; i < 5; i++) {
            if (guess.charAt(i) == secretWord.charAt(i)) {
                result.append("o");
            } else {
                result.append("x");
            }
        }
        return result.toString();
    }

    public static void main(String[] args) {
        Server server = new Server(12345);
        server.startGame();
        server.start();
    }
}