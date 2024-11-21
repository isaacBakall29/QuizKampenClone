package Server;

import Messages.QuizAnswer;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

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
        int nrOfRounds = gameEngine.nrOfRounds;
        int nrOfQuestions = gameEngine.nrOfQuestions;
        List<String> categories = new ArrayList<>(gameEngine.getQuestions().stream().map(Question::getCategory).distinct().collect(Collectors.toList()));

        gameEngine.displayCategories();

        for (int round = 0; round < nrOfRounds; round++) {
            String category = categories.get(round % categories.size());
            List<Question> questions = gameEngine.getQuestionsForCategory(category);

            for (int i = 0; i < nrOfQuestions; i++) {
                Question question = questions.get(i);
                try {
                    ObjectOutputStream out1 = player1.getClientObjectOutputStream();
                    ObjectOutputStream out2 = player2.getClientObjectOutputStream();


                    out1.writeObject(question);
                    out2.writeObject(question);

                    ObjectInputStream in1 = player1.getClientObjectInputStream();
                    ObjectInputStream in2 = player2.getClientObjectInputStream();

                    Object answer1 = in1.readObject();
                    Object answer2 = in2.readObject();


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
            try{
                System.out.println("Runda " + (round + 1) + " avklarad!");
                Thread.sleep(5000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

            gameEngine.displayScore();
        }
}


