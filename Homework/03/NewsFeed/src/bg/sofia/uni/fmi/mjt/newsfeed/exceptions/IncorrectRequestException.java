package bg.sofia.uni.fmi.mjt.newsfeed.exceptions;

public class IncorrectRequestException extends Exception {
    public IncorrectRequestException(String message) {
        super(message);
    }

    public IncorrectRequestException(String message, Throwable cause) {
        super(message, cause);
    }
}
