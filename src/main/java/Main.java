import java.util.Scanner;

public class Main {

    public static Scanner sc = new Scanner(System.in);

    public static void main(String[] args) throws ToManyMistakesException {

        do {
            String word = getWordToGuess();
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
        if(hangman.getStatus().equals(Status.GUESSED)){
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
        } else if(stateOfGuess == -1){
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