package pl.plauszta;

public class TooManyMistakesException extends Exception {

    public TooManyMistakesException() {
    }

    public TooManyMistakesException(String s) {
        super(s);
    }

    public TooManyMistakesException(String message, Throwable cause) {
        super(message, cause);
    }

    public TooManyMistakesException(Throwable cause) {
        super(cause);
    }
}
