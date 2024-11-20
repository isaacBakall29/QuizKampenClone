package Server;

import java.util.*;


public class GameEngine {

    private final List<Question> questions = new ArrayList<>();
    private final Map<String, Integer> scores = new HashMap<>();

    public GameEngine() {
        myJDBC db = new myJDBC();
        List<Question> dbQuestions = db.getQuestionsFromDB();

        if (!dbQuestions.isEmpty()) {
            questions.addAll(dbQuestions);
        } else {
            System.out.println("No questions found in the database.");
        }
        Collections.shuffle(questions);
    }

    public List<Question> getQuestions() {
        return questions;
    }

    public void checkAnswer(String playerName, String chosenOption) {
        Question currentQuestion =  questions.get(0);
        if (currentQuestion.isCorrect(chosenOption)) {
            scores.put(playerName, scores.get(playerName) + 1);
            System.out.println("Rätt svar!");
        } else {
            System.out.println("Fel svar!");
            scores.put(playerName, scores.get(playerName));  // Ska vi ge minuspoäng om en svarar fel?
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

    public boolean isAnswerCorrect(String playerName, String chosenOption) {
        Question currentQuestion = questions.get(0);
        return currentQuestion.isCorrect(chosenOption);
    }
}


