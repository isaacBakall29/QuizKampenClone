import java.io.*;
import java.net.*;

public class QuizClient {
    public static void main(String[] args) {
        String serverAddress = "localhost";
        int port = 1333;

        try (Socket socket = new Socket(serverAddress, port);
             BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
             PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
             BufferedReader userInput = new BufferedReader(new InputStreamReader(System.in))
        ) {

            String initialMessage = in.readLine();
            if (initialMessage != null) {
                System.out.println("Server: " + initialMessage);
            }

            String question;
            while ((question = in.readLine()) != null) {
                System.out.println("Server: " + question + "\n"); // skriver ut frågan
                for (int i = 0; i < 4; i++) {  // läser ut alternativen
                    System.out.println(in.readLine());
                }
                System.out.println("Ditt svar:");
                String answer = userInput.readLine();
                out.println(answer);//skickar tillbaks svaret till servern
                String resultMessage = in.readLine();
                out.println();
            }

            String userInputLine;
            while ((userInputLine = userInput.readLine()) != null) {
                out.println(userInputLine);
                String response = in.readLine();
                System.out.println("Server: " + response);
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
