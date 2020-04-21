public enum Status {
    ZERO_MISTAKE,
    FIRST_MISTAKE,
    SECOND_MISTAKE,
    THIRD_MISTAKE,
    FOURTH_MISTAKE,
    FIFTH_MISTAKE,
    SIXTH_MISTAKE,
    GUESSED;

    public int numberOfMistakes() {
        return ordinal() + 1;
    }
}