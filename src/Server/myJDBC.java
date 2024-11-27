package Server;

import java.io.FileInputStream;
import java.io.IOException;
import java.sql.*;
import java.util.ArrayList;
import java.util.Arrays;
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




// Category: Geography
        Question question1 = new Question("Hur många landskap finns det i Sverige?", new String[]{"25", "21", "15", "30", "25"}, 2, "Geography", null);
        Question question2 = new Question("Vad heter världens längsta flod?", new String[]{"Amazonas", "Nilen", "Mississippi", "Yangtze", "Nilen"}, 2, "Geography", null);
        Question question3 = new Question("Vilket land har flest invånare?", new String[]{"Indien", "Kina", "USA", "Brasilien", "Kina"}, 2, "Geography", null);

// Category: History
        Question question4 = new Question("När började andra världskriget?", new String[]{"1914", "1939", "1945", "1929", "1939"}, 2, "History", null);
        Question question5 = new Question("Vem var Sveriges kung under stormaktstiden?", new String[]{"Karl XII", "Gustav II Adolf", "Oscar II", "Karl IX", "Gustav II Adolf"}, 2, "History", null);
        Question question6 = new Question("Vilket år blev Berlinmuren riven?", new String[]{"1989", "1991", "1979", "1985", "1989"}, 2, "History", null);

// Category: Science
        Question question7 = new Question("Vad är kemiska formeln för vatten?", new String[]{"H2O", "CO2", "O2", "H2SO4", "H2O"}, 2, "Science", null);
        Question question8 = new Question("Vilken planet är närmast solen?", new String[]{"Venus", "Mars", "Merkurius", "Jorden", "Merkurius"}, 2, "Science", null);
        Question question9 = new Question("Vad är den starkaste formen av bindning mellan atomer?", new String[]{"Vätebindning", "Jonbindning", "Kovalent bindning", "Metallbindning", "Jonbindning"}, 2, "Science", null);

// Category: Literature
        Question question10 = new Question("Vem skrev 'Romeo och Julia'?", new String[]{"William Shakespeare", "Leo Tolstoj", "Charles Dickens", "Jane Austen", "William Shakespeare"}, 2, "Literature", null);
        Question question11 = new Question("Vad heter Sveriges mest kända Nobelpristagare i litteratur?", new String[]{"Selma Lagerlöf", "Harry Martinson", "Tomas Tranströmer", "Pär Lagerkvist", "Selma Lagerlöf"}, 2, "Literature", null);
        Question question12 = new Question("Vem är författaren till '1984'?", new String[]{"George Orwell", "Aldous Huxley", "Jules Verne", "Ray Bradbury", "George Orwell"}, 2, "Literature", null);

// Category: Sports
        Question question13 = new Question("Hur många spelare har ett fotbollslag på planen?", new String[]{"10", "11", "12", "13", "11"}, 2, "Sports", null);
        Question question14 = new Question("Vilken svensk vann Wimbledon 5 gånger i rad?", new String[]{"Björn Borg", "Stefan Edberg", "Mats Wilander", "Robin Söderling", "Björn Borg"}, 2, "Sports", null);
        Question question15 = new Question("Vad heter sporten som kombinerar skidåkning och skytte?", new String[]{"Biathlon", "Skidskytte", "Nordisk kombination", "Freestyle", "Skidskytte"}, 2, "Sports", null);

// Category: Music
        Question question16 = new Question("Vilket band skrev låten 'Bohemian Rhapsody'?", new String[]{"The Beatles", "Queen", "Led Zeppelin", "The Rolling Stones", "Queen"}, 2, "Music", null);
        Question question17 = new Question("Vem var känd som 'The King of Pop'?", new String[]{"Elvis Presley", "Michael Jackson", "Prince", "Madonna", "Michael Jackson"}, 2, "Music", null);
        Question question18 = new Question("Vilket instrument är mest förknippat med Ludwig van Beethoven?", new String[]{"Violin", "Piano", "Flöjt", "Cello", "Piano"}, 2, "Music", null);

// Category: Movies
        Question question19 = new Question("Vilken film vann Oscar för bästa film 1994?", new String[]{"Pulp Fiction", "Forrest Gump", "Shawshank Redemption", "Titanic", "Forrest Gump"}, 2, "Movies", null);
        Question question20 = new Question("Vem spelade huvudrollen i 'Pirates of the Caribbean'?", new String[]{"Johnny Depp", "Orlando Bloom", "Keira Knightley", "Geoffrey Rush", "Johnny Depp"}, 2, "Movies", null);
        Question question21 = new Question("Vad heter den första filmen i Star Wars-serien?", new String[]{"A New Hope", "The Phantom Menace", "Return of the Jedi", "The Empire Strikes Back", "A New Hope"}, 2, "Movies", null);

// Category: Technology
        Question question22 = new Question("Vad är förkortningen för artificiell intelligens?", new String[]{"AI", "AR", "VR", "ML", "AI"}, 2, "Technology", null);
        Question question23 = new Question("Vilket företag skapade iPhone?", new String[]{"Samsung", "Google", "Apple", "Sony", "Apple"}, 2, "Technology", null);
        Question question24 = new Question("Vad är binärtalet för 10 i decimalsystemet?", new String[]{"1010", "10", "1100", "1000", "1010"}, 2, "Technology", null);

// Category: Animals
        Question question25 = new Question("Vilket djur är känd som 'Kung av djungeln'?", new String[]{"Tiger", "Lejon", "Elefant", "Gepard", "Lejon"}, 2, "Animals", null);
        Question question26 = new Question("Vad heter jordens största däggdjur?", new String[]{"Blåval", "Elefant", "Haj", "Orka", "Blåval"}, 2, "Animals", null);
        Question question27 = new Question("Vilken fågel är känd för att inte kunna flyga?", new String[]{"Penguin", "Flamingo", "Örn", "Albatross", "Penguin"}, 2, "Animals", null);

// Category: Food
        Question question28 = new Question("Vad heter huvudbeståndsdelen i sushi?", new String[]{"Ris", "Fisk", "Alger", "Soja", "Ris"}, 2, "Food", null);
        Question question29 = new Question("Vilken frukt är känd för att växa i klasar?", new String[]{"Äpple", "Banan", "Apelsin", "Mango", "Banan"}, 2, "Food", null);
        Question question30 = new Question("Vad kallas den italienska maträtten med deg, ost, och tomatsås?", new String[]{"Pasta", "Lasagne", "Pizza", "Risotto", "Pizza"}, 2, "Food", null);

        questions.add(question1);
        questions.add(question2);
        questions.add(question3);
        questions.add(question4);
        questions.add(question5);
        questions.add(question6);
        questions.add(question7);
        questions.add(question8);
        questions.add(question9);
        questions.add(question10);
        questions.add(question11);
        questions.add(question12);
        questions.add(question13);
        questions.add(question14);
        questions.add(question15);
        questions.add(question16);
        questions.add(question17);
        questions.add(question18);
        questions.add(question19);
        questions.add(question20);
        questions.add(question21);
        questions.add(question22);
        questions.add(question23);
        questions.add(question24);
        questions.add(question25);
        questions.add(question26);
        questions.add(question27);
        questions.add(question28);
        questions.add(question29);
        questions.add(question30);


        return questions;

    }

    public List<String> getCategoriesFromDB(){
        List<String> categories = Arrays.asList(
                "Geography",
                "History",
                "Science",
                "Literature",
                "Sports",
                "Music",
                "Movies",
                "Technology",
                "Animals",
                "Food"
        );

        return categories;

    }

    public List<Question> getQuestionsFromDB(boolean fake) {
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
                String category = resultSet.getString("CATEGORY");
                byte[] imageBytes = resultSet.getBytes("IMAGE");


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
                    questions.add(new Question(questionText, options, correctAnswerIndex, category, imageBytes));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return questions;
    }

    public List<String> getCategoriesFromDB(boolean fake) {
        List<String> categories = new ArrayList<>();
        try {
            ResultSet resultSet = statement.executeQuery("SELECT distinct (CATEGORY) FROM QuizDB");
            while (resultSet.next()) {
                categories.add(resultSet.getString("CATEGORY"));
            }
            return categories;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
