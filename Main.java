import java.util.Scanner;
import javax.swing.*;
import java.awt.*;
public class Main extends JFrame {
    public static void main(String[] args) {
    Client client = new Client("localhost", 12345);

    // Listen for server messages
    client.listenForUpdates();
    Scanner scanner = new Scanner(System.in);

    // Loop for sending guesses
    while (true) {
        System.out.print("Enter guess: ");
        String guess = scanner.nextLine();
        client.sendGuess(guess);
    }
    GameGUI gameUI = new GameGUI();
}
}
class GameGUI extends JFrame {
    public GameGUI() {
        buildDashboard();
        setVisible(true);
    }

private void buildDashboard() {
        setTitle("Battle to Wordle");
        setSize(400, 300);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        JPanel panel = new JPanel();
        panel.setLayout(new GridLayout(5, 1));
        JButton playButton = new JButton("Play");
        playButton.addActionListener(e -> {
            // Connects a client to the server and starts the game
            Client client = new Client("localhost", 12345);
                client.listenForUpdates();
        });
        panel.add(playButton);
    }
}