package bg.sofia.uni.fmi.mjt.newsfeed.exceptions;

public class NewsFeedException extends RuntimeException {
    public NewsFeedException(String message) {
        super(message);
    }

    public NewsFeedException(String message, Throwable cause) {
        super(message, cause);
    }
}
