package Server;

import java.io.Serializable;

public class Question implements Serializable {

    String question;
    String[] options;
    int correctOption;

    public Question(String question, String[] options, int correctOption) {
        this.question = question;
        this.options = options;
        this.correctOption = correctOption;
    }

    public String getQuestionText() {
        return this.question;
    }

    public String[] getOptions() {
        return options;
    }

    public int getCorrectOption() {
        return correctOption;
    }

    public void setCorrectOption(int correctOption) {
        this.correctOption = correctOption;
    }

    public boolean isCorrect(String chosenOption) {
        System.out.println("Player answered : " + chosenOption);
        System.out.println("Correct answer should be : " + options[correctOption]);
        return options[correctOption].equals(chosenOption);
    }

}
