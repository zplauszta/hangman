package pl.plauszta.game;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public class Hangman {
    private static final String LETTER_PATTERN = "\\p{L}";
    private final String word;
    private final List<Character> usedCharacters;
    private Status status;

    public Hangman(String word) {
        this.word = word;
        status = Status.ZERO_MISTAKE;
        Set<Character> charsOfWord = wordAsCharSet();
        usedCharacters = new ArrayList<>();
        for (Character character : charsOfWord) {
            if (!character.toString().matches(LETTER_PATTERN)) {
                usedCharacters.add(character);
            }
        }
    }

    public void addMistake() {
        status = Status.values()[status.ordinal() + 1];
    }

    public EnteredLetterStatus checkLetter(char c) {
        if (usedCharacters.contains(c)) {
            return EnteredLetterStatus.ALREADY_ENTERED;
        }
        usedCharacters.add(c);
        Set<Character> charsOfWord = wordAsCharSet();

        if (!charsOfWord.contains(c)) {
            addMistake();
            return EnteredLetterStatus.MISSED;
        }
        if (isWon()) {
            status = Status.GUESSED;
        }
        return EnteredLetterStatus.GUESSED;
    }

    public String getGuessedLetters() {
        StringBuilder stringBuilder = new StringBuilder();

        for (char c : word.toCharArray()) {
            final boolean isGuessedLetter = usedCharacters.contains(c);
            final boolean isLetter = (c + "").matches(LETTER_PATTERN);
            stringBuilder.append(isGuessedLetter || !isLetter ? c : "_");
        }
        return stringBuilder.toString();
    }

    private boolean isWon() {
        Set<Character> charsOfWord = wordAsCharSet();
        return usedCharacters.containsAll(charsOfWord);
    }

    private Set<Character> wordAsCharSet() {
        return word.chars()
                .mapToObj(e -> (char) e)
                .collect(Collectors.toSet());
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
