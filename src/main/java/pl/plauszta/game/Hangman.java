package pl.plauszta.game;

import pl.plauszta.TooManyMistakesException;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public class Hangman {
    private final String word;
    private Status status;
    private final List<Character> usedCharacters;
    private static final String PATTERN = "\\p{L}";

    public Hangman(String word) {
        this.word = word;
        status = Status.ZERO_MISTAKE;
        Set<Character> charsOfWord = word.chars()
                .mapToObj(e -> (char) e)
                .collect(Collectors.toSet());
        usedCharacters = new ArrayList<>();
        for (Character character : charsOfWord) {
            if (!character.toString().matches(PATTERN)) {
                usedCharacters.add(character);
            }
        }
    }

    public void addMistake() throws TooManyMistakesException {
        if (status.equals(Status.SIXTH_MISTAKE)) {
            throw new TooManyMistakesException();
        }
        status = Status.values()[status.ordinal() + 1];
    }

    public StageStatus checkLetter(char c) throws TooManyMistakesException {
        if (usedCharacters.contains(c)) {
            return StageStatus.ALREADY_ENTERED;
        }

        usedCharacters.add(c);

        Set<Character> charsOfWord = word.chars()
                .mapToObj(e -> (char) e)
                .collect(Collectors.toSet());

        if (!charsOfWord.contains(c)) {
            addMistake();
            return StageStatus.MISSED;
        }

        if (isWon()) {
            status = Status.GUESSED;
        }
        return StageStatus.GUESSED;
    }

    public String getGuessedLetters() {
        StringBuilder stringBuilder = new StringBuilder();

        for (char c : word.toCharArray()) {
            stringBuilder.append((usedCharacters.contains(c) || !(c + "").matches(PATTERN)) ? c : "_");
        }
        return stringBuilder.toString();
    }

    private boolean isWon() {
        Set<Character> charsOfWord = word.chars()
                .mapToObj(e -> (char) e)
                .collect(Collectors.toSet());
        return usedCharacters.containsAll(charsOfWord);
    }

    public boolean isEnd() {
        return status.equals(Status.GUESSED) || status.equals(Status.SIXTH_MISTAKE);
    }

    public String getWord() {
        return word;
    }

    public Status getStatus() {
        return status;
    }

    public List<Character> getUsedCharacters() {
        return usedCharacters;
    }
}
