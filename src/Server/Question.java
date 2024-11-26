package Server;

import java.io.Serializable;

//// it reads data from DB and create question object about question, use to send to GUI and client
public class Question implements Serializable {
    private String questionText;
    private String[] options;
    private int correctOption;
    private String category;
    private byte[] image;

    public Question(String questionText, String[] options, int correctOption, String category, byte[] image) {
        this.questionText = questionText;
        this.options = options;
        this.correctOption = correctOption;
        this.category = category;
        this.image = image;
    }

    public String getQuestionText() {
        return this.questionText;
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

    public byte[] getImage() {
        return image;
    }

    public void setImage(byte[] image) {
        this.image = image;
    }

    public boolean isCorrect(String chosenOption) {
        System.out.println("Player answered : " + chosenOption);
        System.out.println("Correct answer should be : " + options[correctOption]);
        return options[correctOption].equals(chosenOption);
    }

}
