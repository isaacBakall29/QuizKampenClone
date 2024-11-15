import java.io.*;
import java.net.*;

public class QuizServer {

    int port = 1333;
    int connectedPlayers = 0;
    final int MAX_PLAYERS = 2;
    boolean isFirstPlayer = true;

    public QuizServer() {

        try (ServerSocket serverSocket = new ServerSocket(port)) {
            System.out.println("Servern är igång och väntar på anslutningar...");

            while (true) {
                Socket clientSocket = serverSocket.accept();

                if (connectedPlayers < MAX_PLAYERS) {
                    connectedPlayers++;
                    PrintWriter out = new PrintWriter(clientSocket.getOutputStream(), true);

                    if (connectedPlayers == 1) {
                        System.out.println("Player One har anslutit");
                        out.println("Du är Player One!");

                    } else if (connectedPlayers == 2) {
                        System.out.println("Player Two har anslutit");
                        out.println("Du är Player Two!");
                    }

                } else {
                    PrintWriter out = new PrintWriter(clientSocket.getOutputStream(), true);
                    out.println("Spelet är redan igång. Max antal spelare är anslutna.");
                    clientSocket.close();
                    System.out.println("En ny klient försökte ansluta, men spelet är redan igång.");
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {

        QuizServer server = new QuizServer();

    }
}
