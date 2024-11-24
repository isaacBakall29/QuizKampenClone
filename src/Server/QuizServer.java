package Server;

import Messages.QuizAnswer;
import Messages.ServerMessage;

import java.io.*;
import java.net.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

public class QuizServer {
    private static final int PORT = 1113;
    private static final GameEngine gameEngine = new GameEngine();
    private static final List<PlayerInfo> playerSockets = new ArrayList<>();
    private static final List<PlayerInfo> connectedSockets = new ArrayList<>();

    public QuizServer() {
        try (ServerSocket serverSocket = new ServerSocket(PORT)) {
            System.out.println("Servern är igång och väntar på anslutningar...");

            //// every 3 sek , it will check if there is other player waiting
            new Thread(new Runnable() {
                @Override
                public void run() {

                    while (true) {
                        if (playerSockets.size() > 1) {
                            PlayerInfo player1 = playerSockets.get(0);
                            PlayerInfo player2 = playerSockets.get(1);
                            connectedSockets.add(player1);
                            connectedSockets.add(player2);
                            playerSockets.remove(player1);
                            playerSockets.remove(player2);
                            new Thread(new GameThread(player1, player2)).start();

                        } else {
                            //System.out.println("waiting for more players");
                            try {
                                Thread.sleep(3000);
                            } catch (InterruptedException e) {
                                throw new RuntimeException(e);
                            }
                        }
                    }
                }
            }).start();

            while (true) {
                Socket clientSocket = serverSocket.accept();

                ObjectInputStream in = new ObjectInputStream(clientSocket.getInputStream());
                ObjectOutputStream out = new ObjectOutputStream(clientSocket.getOutputStream());
                PlayerInfo playerInfo = new PlayerInfo(clientSocket, in, out);

                connectedSockets.add(playerInfo);

                new Thread(new PlayerHandler(playerInfo, playerSockets.size())).start();

            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    //// hold connection to wait until player click start button
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
            ObjectInputStream in = socket.getClientObjectInputStream();

            try {
                while (true) {
                    Object gameStart = in.readObject();

                    if (gameStart instanceof String) {
                        connectedSockets.remove(socket);
                        playerSockets.add(socket);
                        out.writeObject(ServerMessage.WAITINGFORPLAYERS);
                        break;
                    }
                }
            } catch (ClassNotFoundException | IOException e) {
                throw new RuntimeException(e);
            }
        }
    }

    public static void main(String[] args) {
        QuizServer server = new QuizServer();
    }
}
