package Server;

import Messages.QuizAnswer;
import Messages.ServerMessage;

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
        gameEngine.addPlayer(player1.getSocket().toString());
        gameEngine.addPlayer(player2.getSocket().toString());
    }

    @Override
    public void run() {
        int nrOfRounds = gameEngine.nrOfRounds;
        int nrOfQuestions = gameEngine.nrOfQuestions;
        List<String> categories = new ArrayList<>(gameEngine.getQuestions().stream().map(Question::getCategory).distinct().collect(Collectors.toList()));

        gameEngine.displayCategories();

        for (int round = 0; round < nrOfRounds; round++) {

            //TODO ask player for category, LATER it needs to changed which player get to choose category

            try {
                player1.writeObject(ServerMessage.CHOOSECATEGORY);
                player2.writeObject(ServerMessage.WAITINGFOROTHERTOCHOOSECATEGORY);

            } catch (IOException e) {
                throw new RuntimeException(e);
            }

            //String category = categories.get(round % categories.size());

            //TODO receive answer from player which category

            Object category;

            try {
                category = player1.readObject();

            } catch (IOException e) {
                throw new RuntimeException(e);
            } catch (ClassNotFoundException e) {
                throw new RuntimeException(e);
            }
            System.out.println(category);
            List<Question> questions = gameEngine.getQuestionsForCategory((String) category);

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

                    System.out.println("Have received answer from both players");

                    if (answer1 instanceof QuizAnswer quizAnswer) {
                        out1.writeObject(question.isCorrect(quizAnswer.getAnswer()) ? "Rätt svar!" : "Fel svar!");
                        if (question.isCorrect(quizAnswer.getAnswer())) {
                            gameEngine.updateScoreHashmap(player1.getSocket().toString());
                            sendScore(); //TODO jag vet inte hur man kan skicka till client eller GUI

                        }
                    } else {
                        out1.writeObject("Spelare 1 svarade inte");
                    }

                    if (answer2 instanceof QuizAnswer quizAnswer) {
                        out2.writeObject(question.isCorrect(quizAnswer.getAnswer()) ? "Rätt svar!" : "Fel svar!");
                        if (question.isCorrect(quizAnswer.getAnswer())) {
                            gameEngine.updateScoreHashmap(player2.getSocket().toString());
                            sendScore();//TODO jag vet inte hur man kan skicka till client eller GUI
                        }
                    } else {
                        out2.writeObject("Spelare 2 svarade inte");
                    }

                } catch (IOException e) {
                    e.printStackTrace();

                } catch (ClassNotFoundException e) {
                    throw new RuntimeException(e);
                }
            }
            try{
                System.out.println("Runda " + (round + 1) + " avklarad!");
                Thread.sleep(1000); //
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

            gameEngine.displayScore();
        }

        public void sendScore(){
            int player1Score = gameEngine.getScoreFromHashmap(player1.getSocket().toString());
            int player2Score = gameEngine.getScoreFromHashmap(player2.getSocket().toString());
            //TODO jag vet inte hur man kan skicka till client eller GUI
        }

}


