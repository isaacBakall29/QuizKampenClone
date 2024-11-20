import java.sql.*;

public class myJDBC {
    public static void main(String[] args) {
        try {
            Connection connection = DriverManager.getConnection(
                    "jdbc:mysql://sql8.freesqldatabase.com:3306/sql8745937",
                    "sql8745937",
                    "ubuMkvFhGq"
            );
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery("SELECT * FROM QuizDB");

            while(resultSet.next()){
                System.out.println(resultSet.getString("ID"));
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
