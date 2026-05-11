import java.awt.*;
import javax.swing.*;

public class GameGUI extends JFrame {
    private Client client;
    private JTextArea gameArea;
    private JTextField guessField;
    private JButton submitButton;
    private JLabel timerLabel;
    private JTextArea playerArea;

    public GameGUI() {
        // connect to server
        client = new Client("localhost", 12345);
        buildUI();
        client.listenForUpdates(this);
        setVisible(true);
    }
    private void buildUI() {
        setTitle("Battle to Wordle");
        setSize(500, 400);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());
        
        // Game area interface
        gameArea = new JTextArea();
        gameArea.setEditable(false);
        add(new JScrollPane(gameArea), BorderLayout.CENTER);

        // Timer on the right side and Player list below it
        JPanel rightPanel = new JPanel();
        rightPanel.setLayout(new BorderLayout());
        timerLabel = new JLabel("Timer: 30");
        timerLabel.setFont(new Font("Arial", Font.BOLD, 16));
        rightPanel.add(timerLabel, BorderLayout.NORTH);
        playerArea = new JTextArea(10, 10);
        playerArea.setEditable(false);
        rightPanel.add(new JScrollPane(playerArea), BorderLayout.CENTER);
        add(rightPanel, BorderLayout.EAST);

        // The input field and submit button
        JPanel bottomPanel = new JPanel();
        bottomPanel.setLayout(new FlowLayout());
        guessField = new JTextField(15);
        submitButton = new JButton("Submit");
        bottomPanel.add(guessField);
        bottomPanel.add(submitButton);
        add(bottomPanel, BorderLayout.SOUTH);

        // Action listener for the submit button
        submitButton.addActionListener(e -> sendGuess());
    }

    private void sendGuess() {
        String guess = guessField.getText().trim();
        if (!guess.isEmpty()) {
            client.sendGuess(guess);
            guessField.setText("");
        }
    }

    // Adds the server messages to the game area
    public void addMessage(String message) {
        if (message.startsWith("TIME:")) {
            String time = message.substring(5).trim();
            SwingUtilities.invokeLater(() -> {
                timerLabel.setText("Timer: " + time);
            });
            return;
        }
        if (message.startsWith("PLAYERS ")) {
            String players = message.substring(8).trim();
            SwingUtilities.invokeLater(() -> {
                playerArea.setText("Players:\n" + players.replace(",", "\n"));
            });
            return;
        }
    gameArea.append(message + "\n");
}
}