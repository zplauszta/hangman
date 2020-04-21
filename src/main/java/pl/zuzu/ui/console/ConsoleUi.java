package pl.zuzu.ui.console;

import pl.zuzu.*;
import pl.zuzu.game.Game;
import pl.zuzu.game.GameMode;
import pl.zuzu.game.Hangman;
import pl.zuzu.game.Status;

import java.util.List;
import java.util.Random;
import java.util.Scanner;

public class ConsoleUi {

    static Scanner sc = new Scanner(System.in);

    public static void gameLoop() throws TooManyMistakesException {
        Game game = new Game();
        Random random = new Random();
        game.getWordDatabase().init();
        game.setMode(getGameMode());

        do {
            final List<String> words = game.getWordDatabase().getWords();
            String word = game.getMode().equals(GameMode.TWO_PLAYERS) ? getWordToGuess() : words.get(random.nextInt(words.size()));
            Hangman hangman = new Hangman(word);
            game.setHangman(hangman);
            clearScreen();

            while (hangman.isEnd()) {
                printStage(hangman);
            }

            endGameScreen(hangman);
            System.out.println("If you want finish this game enter 'x'");

        } while (!"x".equals(sc.nextLine()));
    }

    private static GameMode getGameMode() {
        System.out.println("HANGMAN");
        System.out.println("Do you want play alone or with someone? (\"1\" or \"2\")");
        String option = sc.nextLine();
        while (!option.equals("1") && !option.equals("2")) {
            System.out.println("Do you want play alone or with someone? (\"1\" or \"2\")");
            option = sc.nextLine();
        }
        return option.equals("1") ? GameMode.ONE_PLAYER : GameMode.TWO_PLAYERS;
    }

    private static String getWordToGuess() {
        System.out.println("HANGMAN");
        System.out.println();
        System.out.println("Please pass the word: ");
        return sc.nextLine();
    }

    private static void clearScreen() {
        for (int i = 0; i < 50; i++) {
            System.out.println();
        }
    }

    private static void printStage(Hangman hangman) throws TooManyMistakesException {
        System.out.println(hangman.buildHangman());
        System.out.println(hangman.getGuessedLetters());
        System.out.println("Type a letter: ");
        char letter = sc.nextLine().charAt(0);
        clearScreen();
        typeStatement(hangman, letter);
    }

    private static void typeStatement(Hangman hangman, char letter) throws TooManyMistakesException {
        int stateOfGuess = hangman.checkLetter(letter);
        if (stateOfGuess == 0) {
            System.out.println("Already used!");
        } else if (stateOfGuess == -1) {
            System.out.println("Missed.");
        }
    }

    private static void endGameScreen(Hangman hangman) {
        if (hangman.getStatus().equals(Status.GUESSED)) {
            System.out.println("Congratulation! You have just guessed a word: " + hangman.getWord());
        } else {
            System.out.println(hangman.buildHangman());
            System.out.println("Buuu! You lose. The word was: " + hangman.getWord());
        }
    }
}
