package Messages;

import java.io.Serializable;

//// it sends back your score, opponent score from Server to GUI and Client
public class QuizScore implements Serializable {

    private final int yourScore; // player score for the one who receives message
    private final int opponentScore;
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

    public int getYourScore() {
        return yourScore;
    }

    public Integer[][] getYourScoreBoard() {
        return yourScoreBoard;
    }

    public Integer[][] getOpponentScoreBoard() {
        return opponentScoreBoard;
    }
}
