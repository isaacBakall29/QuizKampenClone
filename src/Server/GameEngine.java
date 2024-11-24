package Server;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;


public class GameEngine {

    private final List<Question> questions = new ArrayList<>();
    private final Map<String, Integer> scores = new ConcurrentHashMap<>();
    public final Map<String, List<Question>> questionsByCategory = new ConcurrentHashMap<>();

    Integer nrOfRounds = 3;
    Integer nrOfQuestions = 2;

    public GameEngine() {
        myJDBC db = new myJDBC();
        List<Question> dbQuestions = db.getQuestionsFromDB();

        if (!dbQuestions.isEmpty()) {
            questions.addAll(dbQuestions);
        } else {
            System.out.println("No questions found in the database.");
        }

        Collections.shuffle(questions);
        groupQuestionsByCategory();
        readPropertiesFile();
    }

    private void groupQuestionsByCategory() {
        questionsByCategory.putAll(questions.stream().collect(Collectors.groupingBy(Question::getCategory)));
    }


    public List<Question> getQuestionsForCategory(String category){
        return questionsByCategory.getOrDefault(category, new ArrayList<>());
    }

    public void displayCategories() {
        System.out.println("Kategorier: \n");
        for (String category : questionsByCategory.keySet()) {
            System.out.println(category);
        }
    }

    public List<Question> getQuestions() {
        return questions;
    }

//    public void checkAnswer(String playerName, String chosenOption) {
//        Question currentQuestion =  questions.get(0);
//        if (currentQuestion.isCorrect(chosenOption)) {
//            scores.put(playerName, scores.get(playerName) + 1);
//            System.out.println("Rätt svar!");
//        } else {
//            System.out.println("Fel svar!");
//            scores.put(playerName, scores.get(playerName));
//        }
//    }

    public void displayScore() {
        System.out.println("Poängställning:");
        for (Map.Entry<String, Integer> entry : scores.entrySet()) {
            System.out.println(entry.getKey() + ": " + entry.getValue());
        }
    }

    public void addPlayer(String playerName) {
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

    public int nrOfRounds() {
        return this.nrOfRounds;
    }

    public int nrOfQuestions() {
        return this.nrOfQuestions;
    }

    public void updateScoreHashmap(String player) {
        Integer score = scores.get(player);
        scores.put(player, score + 1);
    }
}


