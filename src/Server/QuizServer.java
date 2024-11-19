package Server;

import Messages.QuizAnswer;

import java.io.*;
import java.net.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

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
                ObjectOutputStream out = new ObjectOutputStream(clientSocket.getOutputStream());
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
                    ObjectOutputStream out = playerSocket.getClientObjectOutputStream();
                    out.writeObject(question);

                    ObjectInputStream in = playerSocket.getClientObjectInputStream();
                    Object answer = in.readObject();

                    String playerName = playerSocket.toString(); //removed getRemoteAddress to use same value

                    if (answer instanceof QuizAnswer quizAnswer) {
                        //gameEngine.checkAnswer(playerName, quizAnswer.getAnswer());
                        out.writeObject(question.isCorrect(quizAnswer.getAnswer()) ? "Rätt svar!" : "Fell svar!");
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
            ObjectOutputStream out = socket.getClientObjectOutputStream();

            try {

                if (playerNumber == 1) {
                    out.writeObject("Du är Player One!");
                } else if (playerNumber == 2) {
                    out.writeObject("Du är Player Two!");
                } else {
                    out.writeObject("Max antal spelare är uppnått!");
                    // close socket
                }

            } catch (IOException e) {
                e.printStackTrace();
            }

            gameEngine.addPlayer(socket.toString());
        }
    }

    public static void main(String[] args) {
        QuizServer server = new QuizServer();
    }
}
