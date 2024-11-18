package Server;

import Messages.QuizAnswer;

import java.io.*;
import java.net.*;
import java.util.ArrayList;
import java.util.List;

public class QuizServer {
    private static int port = 1113;
    private static int connectedPlayers = 0;
    private static int MAX_PLAYERS = 2;
    private static boolean isFirstPlayer = true;
    private static GameEngine gameEngine = new GameEngine();
    private static List<PlayerInfo> playerSockets = new ArrayList<>();

    public QuizServer() {
        try (ServerSocket serverSocket = new ServerSocket(port)) {
            System.out.println("Servern är igång och väntar på anslutningar...");

            while (connectedPlayers < MAX_PLAYERS) {
                Socket clientSocket = serverSocket.accept();

                ObjectInputStream in = new ObjectInputStream(clientSocket.getInputStream());
                PrintWriter out = new PrintWriter(clientSocket.getOutputStream(), true);
                PlayerInfo playerInfo = new PlayerInfo(clientSocket, in, out);

                connectedPlayers++;
                playerSockets.add(playerInfo);
                new Thread(new PlayerHandler(playerInfo, connectedPlayers)).start(); // skapar en ny trådförvarje ansluten spelare
            }

            startGame();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static void startGame() {
        for (Question question : gameEngine.getQuestions()) {
            for (PlayerInfo playerSocket : playerSockets) {
                try {
                    PrintWriter out = playerSocket.getOut();
                    out.println(question.getQuestionText());
                    String[] options = question.getOptions();

                    for (String option : options) {
                        out.println(option);
                    }
                    ObjectInputStream in = playerSocket.getIn();
                    Object answer = in.readObject();

                    String playerName = playerSocket.toString(); //removed getRemoteAddress to use same value

                    if (answer instanceof QuizAnswer quizAnswer) {
                        gameEngine.checkAnswer(playerName, quizAnswer.getAnswer());
                        out.println((gameEngine.isAnswerCorrect(playerName, quizAnswer.getAnswer())) ? "Rätt svar!" : "Fell svar!");
                    }

                } catch (IOException e) {
                    e.printStackTrace();

                } catch (ClassNotFoundException e) {
                    throw new RuntimeException(e);
                }
            }
        }
        gameEngine.displayScore();
    }

    private static class PlayerHandler implements Runnable {
        private PlayerInfo socket;
        private int playerNumber;

        public PlayerHandler(PlayerInfo socket, int playerNumber) {
            this.socket = socket;
            this.playerNumber = playerNumber;
        }

        @Override
        public void run() {
            PrintWriter out = socket.getOut();
            if (playerNumber == 1) {
                out.println("Du är Player One!");
            } else if (playerNumber == 2) {
                out.println("Du är Player Two!");
            } else {
                out.println("Max antal spelare är uppnått!");
                // close socket
            }
            gameEngine.addPlayer(socket.toString());
        }
    }

    public static void main(String[] args) {
        QuizServer server = new QuizServer();
    }
}
