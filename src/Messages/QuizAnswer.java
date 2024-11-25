package Messages;

import java.io.Serializable;

//// this class save answers that player has clicked and send them to server
public class QuizAnswer implements Serializable {

    private String answer;

    public String getAnswer() {
        return answer;
    }

    public void setAnswer(String answer) {
        this.answer = answer;
    }
}
