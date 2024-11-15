import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;

//Kategori;Fråga;Alternativ 1;Alternativ 2;Alternativ 3;Alternativ 4;Korrekt svar

public class QuizDatabase {

    private Map<String, List<String>> categories;

    public QuizDatabase() throws IOException {

        BufferedReader in = new BufferedReader(new FileReader("src/Q&A.csv"));

        String line = null;
        categories = new HashMap<>();

        while ((line = in.readLine()) != null){
            line = line.trim();
            String [] lineArray = line.split(";");

            if (lineArray.length == 7){

                if (categories.containsKey(lineArray[0])){
                    categories.get(lineArray[0]).add(lineArray[1]);

                } else {
                    List<String> list = new ArrayList<>();
                    list.add(lineArray[1]);
                    categories.put(lineArray[0], list);
                }
            }
        }
    }

    public List <String> getCategories(String category){
        return categories.get(category);
    }

    public Set<String> getAllCategory(){
        return categories.keySet();
    }
}
