package bg.sofia.uni.fmi.mjt.newsfeed.exceptions;

public class ServerError extends Error {

    public ServerError(String message) {
        super(message);
    }

    public ServerError(String message, Throwable cause) {
        super(message, cause);
    }
}
