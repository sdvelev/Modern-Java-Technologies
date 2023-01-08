package bg.sofia.uni.fmi.mjt.cocktail.server.storage.exception;

public class CocktailAlreadyExistsException extends Exception {

    public CocktailAlreadyExistsException(String message) {

        super(message);
    }

    public CocktailAlreadyExistsException(String message, Throwable cause) {

        super(message, cause);
    }
}
