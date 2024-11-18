package Client;

import java.io.*;
import java.net.*;

public class QuizClient {

    String serverAddress = "localhost";
    int port = 1113;

    GrafiskInterface gui;

    public QuizClient() {

        try (Socket socket = new Socket(serverAddress, port);
             BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
             ObjectOutputStream out = new ObjectOutputStream(socket.getOutputStream());
             BufferedReader userInput = new BufferedReader(new InputStreamReader(System.in));
        ) {

            gui = new GrafiskInterface(socket, in, out);

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
                out.writeObject(answer);//skickar tillbaks svaret till servern
                String resultMessage = in.readLine();

            }

            String userInputLine;
            while ((userInputLine = userInput.readLine()) != null) {
                out.writeObject(userInputLine);
                String response = in.readLine();
                System.out.println("Server: " + response);
            }


        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {

        QuizClient client = new QuizClient();

    }
}
