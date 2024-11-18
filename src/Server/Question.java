package Server;

public class Question {

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

    public boolean isCorrect(int chosenOption) {
        return chosenOption == correctOption;
    }

}
