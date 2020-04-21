package pl.zuzu.game;

import pl.zuzu.WordDatabase;

public class Game {

    private GameMode mode;
    private Hangman hangman;
    private WordDatabase wordDatabase = new WordDatabase();

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
}
