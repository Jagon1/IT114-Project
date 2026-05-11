import java.util.Scanner;
public class Main {

    public static void main(String[] args) {
        Client client = new Client("localhost", 12345);
        client.listenForUpdates();
        Scanner scanner = new Scanner(System.in);
        while (true) {
            System.out.print("Enter guess: ");
            String guess = scanner.nextLine();
            client.sendGuess(guess);
        }
    }
}