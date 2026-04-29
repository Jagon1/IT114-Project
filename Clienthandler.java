import java.io.*;
import java.net.*;
class ClientHandler implements Runnable {
    private Socket client;
    public ClientHandler(Socket socket) {
    this.client = socket;
}
    public void run() {
        try (
            BufferedReader in = new BufferedReader(new InputStreamReader(client.getInputStream()));
            PrintWriter out = new PrintWriter(client.getOutputStream(), true)) {
            String request = in.readLine();
            System.out.println("Received from client: " + request);
            out.println("Echo: " + request);
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
        try {
            client.close();
        } catch (IOException e) {
            e.printStackTrace();
}
}
}
}