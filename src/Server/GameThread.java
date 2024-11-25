package Server;

import Messages.QuizAnswer;
import Messages.QuizScore;
import Messages.ServerMessage;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.List;

public class GameThread implements Runnable {
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

        gameEngine.displayCategories();

        for (int round = 0; round < nrOfRounds; round++) {

            try {
                if(round%2==0) {
                    chooseCategoryPlayer(player1, player2);
                } else {
                    chooseCategoryPlayer(player2, player1);
                }

            } catch (IOException e) {
                throw new RuntimeException(e);
            }

            //TODO alternative who is getting category and who is getting to wait

            Object category;

            try {
                if(round%2==0) {
                    category = player1.readObject();
                } else {
                    category = player2.readObject();
                }

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
                            gameEngine.player1Answer(round, i, true);
                        } else {
                            gameEngine.player1Answer(round, i, false);
                        }

                    } else {
                        gameEngine.player1Answer(round, i, false);
                        out1.writeObject("Spelare 1 svarade inte");
                    }


                    if (answer2 instanceof QuizAnswer quizAnswer) {
                        out2.writeObject(question.isCorrect(quizAnswer.getAnswer()) ? "Rätt svar!" : "Fel svar!");
                        if (question.isCorrect(quizAnswer.getAnswer())) {
                            gameEngine.updateScoreHashmap(player2.getSocket().toString());
                            gameEngine.player2Answer(round, i, true);
                        } else {
                            gameEngine.player2Answer(round, i, false);
                        }
                    } else {
                        gameEngine.player2Answer(round, i, false);
                        out2.writeObject("Spelare 2 svarade inte");
                    }
                } catch (IOException e) {
                    e.printStackTrace();

                } catch (ClassNotFoundException e) {
                    throw new RuntimeException(e);
                }
                gameEngine.displayScore();

            }
            sendScoreToGui();

            try {
                System.out.println("Runda " + (round + 1) + " avklarad!");
                Thread.sleep(5000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        gameEngine.displayScore();
    }

    private void chooseCategoryPlayer(PlayerInfo chooseCategoryPlayer, PlayerInfo otherPlayer) throws IOException {
        chooseCategoryPlayer.writeObject(ServerMessage.CHOOSECATEGORY);
        chooseCategoryPlayer.writeObject(gameEngine.getCategoriesFromDB());
        otherPlayer.writeObject(ServerMessage.WAITINGFOROTHERTOCHOOSECATEGORY);
    }

    public void sendScoreToGui() {
        int player1Score = gameEngine.getScoreFromHashmap(player1.getSocket().toString());
        int player2Score = gameEngine.getScoreFromHashmap(player2.getSocket().toString());
        Integer[][] player1ScoreBoard = gameEngine.getPlayer1ScoreBoard();
        Integer[][] player2ScoreBoard = gameEngine.getPlayer2ScoreBoard();

        try {
            var quizScoreP1 = new QuizScore(player1Score, player2Score, player1ScoreBoard, player2ScoreBoard);
            var quizScoreP2 = new QuizScore(player2Score, player1Score, player2ScoreBoard, player1ScoreBoard);
            player1.resetOutStream();
            player2.resetOutStream();
            player1.writeObject(ServerMessage.UPDATESCORE);
            player2.writeObject(ServerMessage.UPDATESCORE);
            player1.writeObject(quizScoreP1);
            player2.writeObject(quizScoreP2);

        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }

}




