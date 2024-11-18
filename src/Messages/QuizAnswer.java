package Messages;

import java.io.Serializable;

public class QuizAnswer implements Serializable {

    private String answer;

    public String getAnswer() {
        return answer;
    }

    public void setAnswer(String answer) {
        this.answer = answer;
    }
}
