package Client;

import Messages.QuizScore;
import Messages.ServerMessage;
import Server.Question;
import java.io.*;
import java.net.*;
import java.util.List;
import java.util.Properties;

public class QuizClient {

    private String [] categories;

    String serverAddress = "localhost";
    int port = 1113;

    GrafiskInterface gui;

    public QuizClient() {

        try (Socket socket = new Socket(serverAddress, port);
             ObjectOutputStream out = new ObjectOutputStream(socket.getOutputStream());
             ObjectInputStream in = new ObjectInputStream(socket.getInputStream());
        ) {

            gui = new GrafiskInterface(in, out);
            readCategoryFromPropertiesFile();

            Object task;
            while ((task = in.readObject()) != null) {
                if (task instanceof Question) {
                    gui.updateQuizPanel((Question) task);
                } else if (task instanceof ServerMessage message) {
                    if (message.equals(ServerMessage.WAITINGFORPLAYERS)) {
                        gui.displayWaitingForPlayers();
                    } else if (message.equals(ServerMessage.CHOOSECATEGORY)) {
                        Object object = in.readObject();
                        if (object instanceof List categoryList) {
                            gui.createCategory(categoryList);
                        } else {
                            gui.createCategory(List.of(categories));
                        }
                    } else if (message.equals(ServerMessage.UPDATESCORE)) {
                        Object score = in.readObject();
                        if (score instanceof QuizScore quizScore) {
                            gui.scorePanelBetweenRounds(quizScore);
                        }
                    } else if (message.equals(ServerMessage.GAMEFINISHED)) {
                        Object score = in.readObject();
                        if (score instanceof QuizScore quizScore) {
                            gui.finalScorePanel(quizScore);
                        }
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    private void readCategoryFromPropertiesFile() {

        Properties properties = new Properties();

        try (FileInputStream fileInputStream = new FileInputStream("src/Server/setting.properties")) {
            properties.load(fileInputStream);

            categories = properties.getProperty("gui.categories").split(",");

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static void main(String[] args) {

        QuizClient client = new QuizClient();

    }
}
