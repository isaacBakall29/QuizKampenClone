package Server;

import Messages.QuizAnswer;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.List;

public class GameThread implements Runnable{
    private PlayerInfo player1;
    private PlayerInfo player2;
    private GameEngine gameEngine;

    public GameThread(PlayerInfo player1, PlayerInfo player2) {
        this.player1 = player1;
        this.player2 = player2;
        this.gameEngine = new GameEngine();
    }

    @Override
    public void run() {
        List<Question> questions = gameEngine.getQuestions();

        for (Question question : questions) {
            try {
                ObjectOutputStream out1 = player1.getClientObjectOutputStream();
                ObjectOutputStream out2 = player2.getClientObjectOutputStream();

                out1.writeObject(question);
                out2.writeObject(question);

                ObjectInputStream in1 = player1.getClientObjectInputStream();
                ObjectInputStream in2 = player2.getClientObjectInputStream();

                Object answer1 = in1.readObject();
                Object answer2 = in2.readObject();

//                    String playerName1 = player1.toString();
//                    String playerName2 = player2.toString();

                if (answer1 instanceof QuizAnswer quizAnswer) {
                    out1.writeObject(question.isCorrect(quizAnswer.getAnswer()) ? "Rätt svar!" : "Fell svar!");
                }

                if (answer2 instanceof QuizAnswer quizAnswer) {
                    out1.writeObject(question.isCorrect(quizAnswer.getAnswer()) ? "Rätt svar!" : "Fell svar!");
                }
            } catch (IOException e) {
                e.printStackTrace();

            } catch (ClassNotFoundException e) {
                throw new RuntimeException(e);
            }
            }
            gameEngine.displayScore();
        }

    }


