import java.sql.*;

public class myJDBC {
    public static void main(String[] args) {
        try {
            Connection connection = DriverManager.getConnection(
                    "jdbc:mysql://127.0.0.1:3306/quiz",
                    "root",
                    "greensnow24!"
            );
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery("SELECT * FROM questions");

            while(resultSet.next()){
                System.out.println(resultSet.getString("CATEGORY"));
                System.out.println(resultSet.getString("QUESTION"));
                System.out.println(resultSet.getString("ALT_1"));
                System.out.println(resultSet.getString("ALT_2"));
                System.out.println(resultSet.getString("ALT_3"));
                System.out.println(resultSet.getString("ALT_4"));
                System.out.println(resultSet.getString("ANSWER"));
                System.out.println(" ");
            }

        }catch (SQLException e) {
            e.printStackTrace();
        }

    }
}
