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

        Collections.shuffle(questions); // this way you get shuffled questions within each category
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


    public void addPlayer(String playerName) {
        scores.put(playerName, 0);
    }

    public void displayScore() {
        System.out.println("Poängställning:");
        int playerNr = 1;
        for (Map.Entry<String, Integer> entry : scores.entrySet()) {
            String playerName = "Player " + playerNr;
            System.out.println(playerName + ": " + entry.getValue());
            playerNr++;
        }
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

    public void updateScoreHashmap(String player) {
        Integer score = scores.get(player);
        scores.put(player, score + 1);
    }

    public Integer getScoreFromHashmap(String player) {
        return scores.get(player);
    }
}


