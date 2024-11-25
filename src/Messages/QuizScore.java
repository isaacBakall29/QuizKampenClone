package Messages;

import java.io.Serializable;
import java.util.List;

public class QuizScore implements Serializable {

    private int yourScore; // player score for the one who receives message
    private int opponentScore;
    private final Integer[][] yourScoreBoard;
    private final Integer[][] opponentScoreBoard;

    public QuizScore(int yourScore, int opponentScore, Integer[][] player1ScoreBoard, Integer[][] player2ScoreBoard) {
        this.yourScore = yourScore;
        this.opponentScore = opponentScore;
        this.yourScoreBoard = player1ScoreBoard;
        this.opponentScoreBoard = player2ScoreBoard;
    }

    public int getOpponentScore() {
        return opponentScore;
    }

    public void setOpponentScore(int opponentScore) {
        this.opponentScore = opponentScore;
    }

    public int getYourScore() {
        return yourScore;
    }

    public void setYourScore(int yourScore) {
        this.yourScore = yourScore;
    }

    public Integer[][] getYourScoreBoard() {
        return yourScoreBoard;
    }

    public Integer[][] getOpponentScoreBoard() {
        return opponentScoreBoard;
    }
}
