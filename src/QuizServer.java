import java.io.*;
import java.net.*;
import java.util.ArrayList;
import java.util.List;

public class QuizServer {
    private static int port = 1333;
    private static int connectedPlayers = 0;
    private static int MAX_PLAYERS = 2;
    private static boolean isFirstPlayer = true;
    private static GameEngine gameEngine = new GameEngine();
    private static List<Socket> playerSockets = new ArrayList<>();

    public static void main(String[] args) {
        try (ServerSocket serverSocket = new ServerSocket(port)) {
            System.out.println("Servern är igång och väntar på anslutningar...");

            while (connectedPlayers < MAX_PLAYERS) {
                Socket clientSocket = serverSocket.accept();
                connectedPlayers++;
                playerSockets.add(clientSocket);
                new Thread(new PlayerHandler(clientSocket, connectedPlayers)).start(); // skapar en ny trådförvarje ansluten spelare
            }
            startGame();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static void startGame(){
        for (Question question : gameEngine.getQuestions()) {
            for (Socket playersocket : playerSockets) {
                try {
                    PrintWriter out = new PrintWriter(playersocket.getOutputStream(), true);
                    out.println(question.getQuestionText());
                    String[] options = question.getOptions();
//                    for (int i = 0; i < options.length; i++) {
//                        out.println(i + 1 + ". " + options[i]);
//                    }
                    for (String option :options) {
                        out.println(option);
                    }
                    BufferedReader in = new BufferedReader(new InputStreamReader(playersocket.getInputStream()));
                    int answer = Integer.parseInt(in.readLine());
                    gameEngine.checkAnswer(playersocket.toString(), answer);

                } catch (IOException e) {
                   e.printStackTrace();
                }

            }
        }
        gameEngine.displayScore();
    }
    private static class PlayerHandler implements Runnable {
        private Socket socket;
        private int playerNumber;

        public PlayerHandler(Socket socket, int playerNumber) {
            this.socket = socket;
            this.playerNumber = playerNumber;
        }

        @Override
        public void run() {
            try {
                PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
                if (playerNumber == 1) {
                    out.println("Du är Player One!");
                } else if (playerNumber == 2) {
                    out.println("Du är Player Two!");
                }
                else {
                    out.println("Max antal spelare är uppnått!");
                    // close socket
                }
                gameEngine.addPlayer(socket.toString());
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
