package Server;

import Messages.QuizAnswer;

import java.io.*;
import java.net.*;
import java.util.ArrayList;
import java.util.List;

public class QuizServer {
    private static int port = 1113;

    private static GameEngine gameEngine = new GameEngine();
    private static List<PlayerInfo> playerSockets = new ArrayList<>();

    public QuizServer() {
        try (ServerSocket serverSocket = new ServerSocket(port)) {
            System.out.println("Servern är igång och väntar på anslutningar...");

            while (true) {
                Socket clientSocket = serverSocket.accept();

                ObjectInputStream in = new ObjectInputStream(clientSocket.getInputStream());
                ObjectOutputStream out = new ObjectOutputStream(clientSocket.getOutputStream());
                PlayerInfo playerInfo = new PlayerInfo(clientSocket, in, out);


                playerSockets.add(playerInfo);
                new Thread(new PlayerHandler(playerInfo, playerSockets.size())).start();

                if (playerSockets.size() % 2 == 0) {
                    PlayerInfo player1 = playerSockets.get(playerSockets.size() - 2);
                    PlayerInfo player2 = playerSockets.get(playerSockets.size() - 1);
                    new Thread(new GameThread(player1, player2)).start();
                }
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
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

                if (playerNumber % 2 == 1) {
                    out.writeObject("Du är Player One!");
                } else {
                    out.writeObject("Du är Player Two!");
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
