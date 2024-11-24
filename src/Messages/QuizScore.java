package Messages;

import java.io.Serializable;

public class QuizScore implements Serializable {

    private int yourScore; // player score for the one who receives message
    private int opponentScore;

    public QuizScore(int yourScore, int opponentScore) {
        this.yourScore = yourScore;
        this.opponentScore = opponentScore;
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
}
