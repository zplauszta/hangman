import java.util.*;
import java.net.*;
import java.io.*;

public class Main {

    public static Scanner sc = new Scanner(System.in);
    public static List<String> dataBase;

    public static void main(String[] args) throws ToManyMistakesException {
        final String path = "https://raw.githubusercontent.com/dwyl/english-words/master/words.txt";
        Random random = new Random();
        downloadDatabase(path);

        System.out.println("HANGMAN");
        System.out.println("Do you want play alone or with someone? (\"1\" or \"2\")");
        String option = sc.nextLine();
        while(!option.equals("1") && !option.equals("2")){
            System.out.println("Do you want play alone or with someone? (\"1\" or \"2\")");
            option = sc.nextLine();
        }

        do {
            String word;
            if(option.equals("2")){
                word = getWordToGuess();
            } else{
                word = dataBase.get(random.nextInt(dataBase.size()));
            }
            Hangman hangman = new Hangman(word);
            clearScreen();

            while (!hangman.isEnd()) {
                printStage(hangman);
            }
            endGameScreen(hangman);

            System.out.println("If you want finish this game enter 'x'");
        } while (!"x".equals(sc.nextLine()));
        System.out.println("Goodbye");
        sc.close();
    }

    private static void downloadDatabase(String path) {
        StringBuilder content = new StringBuilder();
        try {
            URL url = new URL(path);

            URLConnection urlConnection = url.openConnection();

            BufferedReader bufferedReader = new BufferedReader(
                    new InputStreamReader(urlConnection.getInputStream()));

            String line;

            while ((line = bufferedReader.readLine()) != null) {
                content.append(line).append("\n");
            }

            bufferedReader.close();
        } catch (Exception e) {
            e.printStackTrace();
        }

        dataBase = new ArrayList<>(Arrays.asList(content.toString().split("\n")));
        System.out.println(dataBase);
    }

    private static void printStage(Hangman hangman) throws ToManyMistakesException {
        System.out.println(hangman.buildHangman());
        System.out.println(hangman.getGuessedLetters());
        System.out.println("Type a letter: ");
        char letter = sc.nextLine().charAt(0);
        clearScreen();
        typeStatement(hangman, letter);
    }

    private static void clearScreen() {
        for (int i = 0; i < 50; i++) {
            System.out.println();
        }
    }

    private static void endGameScreen(Hangman hangman) {
        if (hangman.getStatus().equals(Status.GUESSED)) {
            System.out.println("Congratultion! You have just guessed a word: " + hangman.getWord());
        } else {
            System.out.println(hangman.buildHangman());
            System.out.println("Buuu! You lose. The word was: " + hangman.getWord());
        }
    }

    private static void typeStatement(Hangman hangman, char letter) throws ToManyMistakesException {
        int stateOfGuess = hangman.checkLetter(letter);
        if (stateOfGuess == 0) {
            System.out.println("Already used!");
        } else if (stateOfGuess == -1) {
            System.out.println("Missed.");
        }
    }

    private static String getWordToGuess() {
        System.out.println("HANGMAN");
        System.out.println();
        System.out.println("Please pass the word: ");
        return sc.nextLine();
    }
}