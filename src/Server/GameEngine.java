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
    private final List<String> categoryUsed = new ArrayList<>();
    private final Integer [][] player1ScoreBoard;
    private final Integer [][] player2ScoreBoard;

    Integer nrOfRounds;
    Integer nrOfQuestions;

    myJDBC db = new myJDBC();

    public GameEngine() {
        List<Question> dbQuestions = db.getQuestionsFromDB();

        if (!dbQuestions.isEmpty()) {
            questions.addAll(dbQuestions);
        } else {
            System.out.println("No questions found in the database.");
        }

        Collections.shuffle(questions);
        groupQuestionsByCategory();
        readPropertiesFile();

        player1ScoreBoard = new Integer[nrOfRounds][nrOfQuestions];
        player2ScoreBoard = new Integer[nrOfRounds][nrOfQuestions];
        //Setup default value for array of Integer
        for(int i = 0; i < nrOfRounds; i++) {
            for(int j = 0; j < nrOfQuestions; j++) {
                player1ScoreBoard[i][j] = 0;
                player2ScoreBoard[i][j] = 0;
            }
        }
    }

    private void groupQuestionsByCategory() {
        questionsByCategory.putAll(questions.stream().collect(Collectors.groupingBy(Question::getCategory)));
    }

    public List<Question> getQuestionsForCategory(String category){
        categoryUsed.add(category);
        return questionsByCategory.getOrDefault(category, new ArrayList<>());
    }

    //// to filter out used categories
    public List <String> getCategoriesFromDB(){
        List<String> categories = db.getCategoriesFromDB();
        categories.removeAll(categoryUsed);
        return categories;
    }

    public void displayCategories() {
        System.out.println("Kategorier: \n");
        for (String category : questionsByCategory.keySet()) {
            System.out.println(category);
        }
    }

    public void displayScore() {
        System.out.println("Poängställning:");
        for (Map.Entry<String, Integer> entry : scores.entrySet()) {
            System.out.println(entry.getKey().toString() + ": " + entry.getValue());
        }
    }

    public void addPlayer(String playerName) {
        scores.put(playerName, 0);
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

    public void player1Answer(int round, int question, boolean correct) {
        // 2 for correct answer, 1 for incorrect answer, 0 for has not answered
        if (correct) {
            player1ScoreBoard[round][question] = 2;

        } else {
            player1ScoreBoard[round][question] = 1;
        }
    }

    public void player2Answer(int round, int question, boolean correct) {
        if (correct) {
            player2ScoreBoard[round][question] = 2;
        } else {
            player2ScoreBoard[round][question] = 1;
        }
    }

    public Integer[][] getPlayer1ScoreBoard() {
        return player1ScoreBoard;
    }

    public Integer[][] getPlayer2ScoreBoard() {
        return player2ScoreBoard;
    }

}


