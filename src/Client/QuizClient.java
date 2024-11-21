package Client;

import Messages.ServerMessage;
import Server.Question;

import java.io.*;
import java.net.*;

public class QuizClient {

    String serverAddress = "localhost";
    int port = 1113;

    GrafiskInterface gui;

    public QuizClient() {

        try (Socket socket = new Socket(serverAddress, port);
             ObjectOutputStream out = new ObjectOutputStream(socket.getOutputStream());
             ObjectInputStream in = new ObjectInputStream(socket.getInputStream());
        ) {

            gui = new GrafiskInterface(socket, in, out);

            Object question;

            while ((question = in.readObject()) != null) {
                if (question instanceof Question) {
                    gui.updateQuizPanel((Question) question);

                } else if (question instanceof ServerMessage message) {

                    if (message.equals(ServerMessage.WAITINGFORPLAYERS)){
                        gui.displayWaitingForPlayers();

                    }  else if (message.equals(ServerMessage.CHOOSECATEGORY)){
                        gui.createCategory();
                    }
                }
            }

        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    public static void main(String[] args) {

        QuizClient client = new QuizClient();

    }
}
