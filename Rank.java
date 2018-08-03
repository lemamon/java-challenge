import java.io.File;
import java.io.FileNotFoundException;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Scanner;
import java.util.TreeMap;

public class Rank {

    private static HashMap<Integer, HashMap<String, Integer>> questions;
    private static HashMap<String, Integer> validAnswers;
    private static HashMap<String, Integer> invalidAnswers;

    public static void main(String[] args)   throws FileNotFoundException {

        questions = new HashMap<>();
        validAnswers = new HashMap<>();
        invalidAnswers = new HashMap<>();

 	for (int i = 0; i < args.length; i++) {
	    readFile(args[i]);
        }

        favAnswers();
        printValidAnswers();
        printInvalidAnswers();
    }

    private static void inputFiles() {

    }

    private static void readFile(String path) throws FileNotFoundException {
        Scanner scanner =  new Scanner(new File(path));
        String companyName = scanner.nextLine();

        System.out.println(companyName);
        HashMap<Integer, Question> map = new HashMap<>();

        while (scanner.hasNextLine()){

            String line = scanner.nextLine();

            Scanner lineScanner = new Scanner(line);

            lineScanner.useDelimiter(" ");

            int question = lineScanner.nextInt();
            int choice = lineScanner.nextInt();

            if(choice >= 0 && choice <= 4){
                addValidAnswers(companyName);
                map.put(question, addAnswer(choice, question, map.get(question), companyName));
            }else {
                addInvalidAnswers(companyName);
            }

            lineScanner.close();

        }

        map.forEach((integer, question) -> {
            System.out.println(question.getAnswersPercents());
        });

        System.out.println();
    }


    private static Question addAnswer(int answer, int id, Question question, String name) {

        if(question == null) {
            question = new Question();
            question.setId(id);
        }

        if(answer < 2) {
            question.addFav();
        } else if(answer < 3) {
            question.addNeutral();
        } else {
            question.addUnfav();
        }

        if(questions.containsKey(id)) {
            questions.get(id).put(name, question.getFavPercent());
        } else {
            questions.put(id, new HashMap<>());
            questions.get(id).put(name, question.getFavPercent());
        }

        return question;
    }

    private static void favAnswers () {
        System.out.println("Fav answers by questions:");

        questions.forEach((question, map) -> {
            System.out.print(question + ": ");

            TreeMap<String, Integer> sortMap = sortMapByValue(map);
            sortMap.forEach((s, fav) -> {
                System.out.print(s + " " + fav + "% fav, ");
            });

            System.out.println();
        });
        System.out.println();
    }

    private static void addValidAnswers(String name) {
        if(validAnswers.containsKey(name)){
            int answers = validAnswers.get(name);
            validAnswers.put(name,answers + 1);
        } else {
            validAnswers.put(name, 1);
        }
    }

    private static void addInvalidAnswers(String name) {
        if(invalidAnswers.containsKey(name)){
            int answers = invalidAnswers.get(name);
            invalidAnswers.put(name,answers + 1);
        } else {
            invalidAnswers.put(name, 1);
        }
    }

    private static void printValidAnswers() {
        System.out.println("Valid answers: \n");

        validAnswers.forEach((name, amount) -> {
            System.out.println(name + ": " + amount);
        });

        System.out.println();
    }

    private static void printInvalidAnswers() {
        System.out.println("Invalid answers: \n");

        invalidAnswers.forEach((name, amount) -> {
            System.out.println(name + ": " + amount);
        });

        System.out.println();
    }

    private static TreeMap<String, Integer> sortMapByValue(HashMap<String, Integer> map){
        Comparator<String> comparator = new ValueComparator(map);
        TreeMap<String, Integer> result = new TreeMap<String, Integer>(comparator);
        result.putAll(map);
        return result;
    }


}


class Question {
    private int id;
    private int fav = 0;
    private int neutral = 0;
    private int unfav = 0;

    public void addFav () {
        fav++;
    }

    public int getFavPercent() {
        return  ((fav * 100)/getAnswersAmount() );
    }

    public void addNeutral () {
        neutral++;
    }

    public int getNeutralPercent() {
        return  ((neutral * 100)/getAnswersAmount());
    }

    public void addUnfav () {
        unfav++;
    }

    public int getUnfavPercent() {
        return  ((unfav * 100)/getAnswersAmount());
    }

    public String getAnswersPercents() {
        return (id + ": " + getFavPercent() + "% fav, " + getNeutralPercent() + "% neutral, " + getUnfavPercent()+ "% unfav");
    }

    public int getAnswersAmount() {
        return (fav + neutral + unfav);
    }

    public void setId(int id) {
        this.id = id;
    }
}



class ValueComparator implements Comparator<String>{

    HashMap<String, Integer> map = new HashMap<String, Integer>();

    public ValueComparator(HashMap<String, Integer> map){
        this.map.putAll(map);
    }

    @Override
    public int compare(String s1, String s2) {
        if(map.get(s1) >= map.get(s2)){
            return -1;
        }else{
            return 1;
        }
    }
}
