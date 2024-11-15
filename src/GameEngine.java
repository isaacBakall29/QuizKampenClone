import java.util.*;


public class GameEngine {

    private final List<Question> questions = new ArrayList<>();
    private final Map<String, Integer> scores = new HashMap<>();

    public GameEngine() {
        questions.add(new Question("Vad är huvudstaden i Sverige?",
                new String [] {"Stockholm", "Göteborg", "Malmö", "Uppsala"}, 1));
        questions.add(new Question("Vilken färg har en banan?",

                new String [] {"Blå", "Grön" , "Gul", "Röd"}, 3));
        questions.add(new Question("Vad är huvudstaden i USA?",
                new String [] {"New York", "Los Angeles" , "Washington DC" , "Chicago"}, 3));
        questions.add(new Question("Vilken världsdel tillhör Egypten",
                new String [] {"Afrika", "Asien" , "Europa" , "Sydamerika"}, 3));

        Collections.shuffle(questions);
    }

    public List<Question> getQuestions() {
        return questions;
    }

    public void checkAnswer(String playerName, int chosenOption) {
        Question currentQuestion =  questions.get(0);
        if (currentQuestion.isCorrect(chosenOption)) {
            scores.put(playerName, scores.get(playerName) + 1);
            System.out.println("Rätt svar!");
        } else {
            System.out.println("Fel svar!");
            scores.put(playerName, scores.get(playerName) - 1);  // Ska vi ge minuspoäng om en svarar fel?
        }
    }

    public void displayScore() {
        System.out.println("Poängställning:");
        for (Map.Entry<String, Integer> entry : scores.entrySet()) {  //Nyckeln är PlayerName för att hämta värddet (poängen)
            System.out.println(entry.getKey() + ": " + entry.getValue());
        }
    }

    public void addPlayer(String playerName) { //Adda spelare
        scores.put(playerName, 0);
    }
}

class Question {

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
