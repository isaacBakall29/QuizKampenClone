package Server;

import java.io.Serializable;

//// it reads data from DB and create question object about question, use to send to GUI and client
public class Question implements Serializable {

    String question;
    String[] options;
    int correctOption;
    String category;

    public Question(String question, String[] options, int correctOption, String category) {
        this.question = question;
        this.options = options;
        this.correctOption = correctOption;
        this.category = category;
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

    public String getCategory() {
        return category;
    }

    public boolean isCorrect(String chosenOption) {
        System.out.println("Player answered : " + chosenOption);
        System.out.println("Correct answer should be : " + options[correctOption]);
        return options[correctOption].equals(chosenOption);
    }

}
