package pl.plauszta.game;

import java.util.List;
import java.util.Random;

public class Game {

    private static Game INSTANCE = new Game();

    private GameMode mode;
    private Hangman hangman;
    private final WordDatabase wordDatabase = WordDatabase.getInstance();

    private Game() {
        if (INSTANCE != null) {
            throw new IllegalStateException("Game already constructed");
        }
    }

    public static Game getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new Game();
        }
        return INSTANCE;
    }

    public GameMode getMode() {
        return mode;
    }

    public Hangman getHangman() {
        return hangman;
    }

    public WordDatabase getWordDatabase() {
        return wordDatabase;
    }

    public void setMode(GameMode mode) {
        this.mode = mode;
    }

    public void setHangman(Hangman hangman) {
        this.hangman = hangman;
    }

    public static void resetGame() {
        INSTANCE = null;
    }

    public void changeWordForHangman() {
        setRandomWordForHangman();
    }

    public void changeWordForHangman(String word) {
        if ("".equals(word)) {
            changeWordForHangman();
        } else {
            hangman = new Hangman(word.toLowerCase());
        }
    }

    private void setRandomWordForHangman() {
        Random random = new Random();
        final List<String> words = Game.getInstance().getWordDatabase().getWords();
        String word = words.get(random.nextInt(words.size())).toLowerCase();
        hangman = new Hangman(word);
    }
}
