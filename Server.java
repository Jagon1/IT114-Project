import java.io.*;
import java.net.*;
import java.util.*;

public class Server {
    private String secretWord;
    private ServerSocket serverSocket;
    private int timeLeft;
    private Thread timerThread;
    private boolean roundIsActive;

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
                Clienthandler clientHandler = new Clienthandler(socket, this, "Player" + socket.getPort());
                clients.add(clientHandler);
                Thread thread = new Thread(clientHandler);
                thread.start();
                broadcast(clientHandler.getUsername() + " joined the game!");
                updatePlayerList();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
    public void startNewRound() {
        String[] words = {
            "apple", "grape", "brick", "chair", "plant",
            "stone", "flame", "crane", "blush", "storm"
        };
        Random rand = new Random();
        secretWord = words[rand.nextInt(words.length)];
        roundIsActive = true;
        broadcast("NEW ROUND HAS STARTED!");
        broadcast("Guess the 5-letter word!");
        broadcast("ONLY USE 5 LETTER WORDS OR BE KICKED!");
        System.out.println("DEBUG WORD: " + secretWord);
        startTimer(30);
    }

    // Send message to ALL players
    public void broadcast(String message) {
        for (Clienthandler client : clients) {
            try {
                client.sendMessage(message);
            } catch (Exception e) {
                System.out.println("Failed to send message.");
            }
        }
    }

    public void handleGuess(Clienthandler player, String guess) {
        guess = guess.trim().toLowerCase();
        // Only 5 letters and only letters allowed
        if (guess.length() != 5) {
            player.sendMessage("Your guess must be exactly 5 letters!");
            return;
        }
        if (!guess.matches("[a-zA-Z]+")) {
            player.sendMessage("Only letters allowed!");
            return;
        }
        System.out.println("Guess received: " + guess);
        if (guess.equalsIgnoreCase(secretWord)) {
            player.sendMessage("Correct! You win!");
            broadcast("A player has guessed the word!");
            roundIsActive = false;
            startNewRound();
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
    public void startTimer(int seconds) {
        if (timerThread != null && timerThread.isAlive()) {
            timerThread.interrupt();
        }
        timeLeft = seconds;
        timerThread = new Thread(() -> {
            while (timeLeft > 0 && roundIsActive && !Thread.currentThread().isInterrupted()) {
                broadcast("TIME: " + timeLeft);
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    return;
                }
                timeLeft--;
            }
        if (timeLeft <= 0) {
            broadcast("TIME IS UP!");
            roundIsActive = false;
            startNewRound();
        }
    });
    timerThread.start();
    }
    public void updatePlayerList() {
        StringBuilder players = new StringBuilder();
        players.append("PLAYERS ");
        for (Clienthandler client : clients) {
            players.append(client.getUsername()).append(",");
        }
        broadcast(players.toString());
    }

    public static void main(String[] args) {
        Server server = new Server(12345);
        server.startNewRound();
        server.start();
    }
}