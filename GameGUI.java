import java.awt.*;
import javax.swing.*;

public class GameGUI extends JFrame {
    private Client client;
    private JTextArea gameArea;
    private JTextField guessField;
    private JButton submitButton;

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
        gameArea.append(message + "\n");
    }
}