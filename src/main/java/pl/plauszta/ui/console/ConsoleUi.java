package pl.plauszta.ui.console;

import pl.plauszta.*;
import pl.plauszta.game.*;

import java.util.Scanner;

public class ConsoleUi {

    static Scanner sc = new Scanner(System.in);

    public void gameLoop() throws TooManyMistakesException {
        final Game game = Game.getInstance();
        game.getWordDatabase().init();
        game.setMode(getGameMode());

        do {
            if (GameMode.TWO_PLAYERS.equals(game.getMode())) {
                game.changeWordForHangman(getWordToGuess());
            } else {
                game.changeWordForHangman();
            }
            clearScreen();

            final Hangman hangman = game.getHangman();
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
        System.out.println(buildHangman(hangman));
        System.out.println(hangman.getGuessedLetters());
        System.out.println("Type a letter: ");
        char letter = sc.nextLine().charAt(0);
        clearScreen();
        typeStatement(hangman, letter);
    }

    private static void typeStatement(Hangman hangman, char letter) throws TooManyMistakesException {
        StageStatus stateOfGuess = hangman.checkLetter(letter);
        if (StageStatus.ALREADY_ENTERED.equals(stateOfGuess)) {
            System.out.println("Already used!");
        } else if (stateOfGuess.equals(StageStatus.MISSED)) {
            System.out.println("Missed.");
        }
    }

    private static void endGameScreen(Hangman hangman) {
        if (hangman.getStatus().equals(Status.GUESSED)) {
            System.out.println("Congratulation! You have just guessed a word: " + hangman.getWord());
        } else {
            System.out.println(buildHangman(hangman));
            System.out.println("Buuu! You lose. The word was: " + hangman.getWord());
        }
    }

    private static String buildHangman(Hangman hangman) {
        Status status = hangman.getStatus();
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(" ------\n");
        stringBuilder.append(" | /   |\n");
        stringBuilder.append(" |/");

        stringBuilder.append(status.ordinal() > 0 ? "    o\n" : "\n");

        stringBuilder.append(" |");

        stringBuilder.append(status.ordinal() > 1 ? "     |\n" : "\n");

        stringBuilder.append(" |");

        if (status.ordinal() > 2) {
            stringBuilder.append("    /|");
        }

        stringBuilder.append(status.ordinal() > 3 ? "\\\n" : "\n");

        stringBuilder.append(" |");

        if (status.ordinal() > 4) {
            stringBuilder.append("    /");
        }

        stringBuilder.append(status.ordinal() > 5 ? " \\\n" : "\n");

        stringBuilder.append("---");

        return stringBuilder.toString();
    }
}
