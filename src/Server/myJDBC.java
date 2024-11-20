package Server;

import java.io.FileInputStream;
import java.io.IOException;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

public class myJDBC {
    private Connection connection;
    private Statement statement;
    private String host;
    private String userName;
    private String password;

    public myJDBC() {

        readPropertiesFile();

        try {
            connection = DriverManager.getConnection(
                    host,
                    userName,
                    password
            );
            statement = connection.createStatement();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void readPropertiesFile() {

        Properties properties = new Properties();

        try (FileInputStream fileInputStream = new FileInputStream("src/Server/setting.properties")) {
            properties.load(fileInputStream);

            host = properties.getProperty("database.host");
            userName = properties.getProperty("database.user");
            password = properties.getProperty("database.password");

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public List<Question> getQuestionsFromDB() {
        List<Question> questions = new ArrayList<>();
        try {
            ResultSet resultSet = statement.executeQuery("SELECT * FROM QuizDB");
            while (resultSet.next()) {
                String questionText = resultSet.getString("QUESTION");
                String[] options = new String[]{
                        resultSet.getString("ALT_1"),
                        resultSet.getString("ALT_2"),
                        resultSet.getString("ALT_3"),
                        resultSet.getString("ALT_4")
                };
                String correctAnswerText = resultSet.getString("ANSWER");

                // Find the index of the correct answer
                int correctAnswerIndex = -1;
                for (int i = 0; i < options.length; i++) {
                    if (options[i].equalsIgnoreCase(correctAnswerText)) {
                        correctAnswerIndex = i;
                        break;
                    }
                }

                if (correctAnswerIndex == -1) {
                    System.err.println("Correct answer text not found in options for question: " + questionText);
                } else {
                    questions.add(new Question(questionText, options, correctAnswerIndex));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return questions;
    }
}
