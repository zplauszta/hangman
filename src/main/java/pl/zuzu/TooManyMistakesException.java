package pl.zuzu;

public class TooManyMistakesException extends Throwable {

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
