package Server;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.*;


public class GameEngine {

    private final List<Question> questions = new ArrayList<>();
    private final Map<String, Integer> scores = new HashMap<>();

    Integer nrOfRounds;
    Integer nrOfQuestions;

    public GameEngine() {
        questions.add(new Question("Vad är huvudstaden i Sverige?",
                new String [] {"Stockholm", "Göteborg", "Malmö", "Uppsala"}, 0));
        questions.add(new Question("Vilken färg har en banan?",
                new String [] {"Blå", "Grön" , "Gul", "Röd"}, 2));
        questions.add(new Question("Vad är huvudstaden i USA?",
                new String [] {"New York", "Los Angeles" , "Washington DC" , "Chicago"}, 2));
        questions.add(new Question("Vilken världsdel tillhör Egypten",
                new String [] {"Afrika", "Asien" , "Europa" , "Sydamerika"}, 0));

        Collections.shuffle(questions);

        readPropertiesFile();
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

    private void readPropertiesFile() {

        Properties properties = new Properties();

        try (FileInputStream fileInputStream = new FileInputStream("src/Server/setting.properties")) {
            properties.load(fileInputStream);

            nrOfRounds = Integer.parseInt(properties.getProperty("quizkampen.nrOfRounds"));
            nrOfQuestions = Integer.parseInt(properties.getProperty("quizkampen.nrOfQuestions"));

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}


