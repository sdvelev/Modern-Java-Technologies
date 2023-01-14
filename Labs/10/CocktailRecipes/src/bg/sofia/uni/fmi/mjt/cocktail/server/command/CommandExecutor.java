package bg.sofia.uni.fmi.mjt.cocktail.server.command;

import bg.sofia.uni.fmi.mjt.cocktail.server.Cocktail;
import bg.sofia.uni.fmi.mjt.cocktail.server.Ingredient;
import bg.sofia.uni.fmi.mjt.cocktail.server.storage.CocktailStorage;
import bg.sofia.uni.fmi.mjt.cocktail.server.storage.exceptions.CocktailAlreadyExistsException;
import bg.sofia.uni.fmi.mjt.cocktail.server.storage.exceptions.CocktailNotFoundException;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class CommandExecutor {

    private CocktailStorage cocktailStorage;

    private static final String CREATE_COMMAND_STRING = "create";
    private static final String GET_ALL_COMMAND_STRING = "get all" ;
    private static final String GET_BY_NAME_COMMAND_STRING = "get by-name";
    private static final String GET_BY_INGREDIENT_COMMAND_STRING = "get by-ingredient";
    private static final String DISCONNECT_COMMAND_STRING = "disconnect";

    public CommandExecutor(CocktailStorage cocktailStorage) {

        this.cocktailStorage = cocktailStorage;
    }

    public String executeCommand(Command cmd) {

        return switch(cmd.command()) {

            case CREATE_COMMAND_STRING -> this.processCreateCommand(cmd.arguments());
            case GET_ALL_COMMAND_STRING -> this.processGetAllCommand();
            case GET_BY_NAME_COMMAND_STRING -> this.processGetByNameCommand(cmd.arguments());
            case GET_BY_INGREDIENT_COMMAND_STRING -> this.processGetByIngredientCommand(cmd.arguments());
            case DISCONNECT_COMMAND_STRING -> this.processDisconnectCommand();
            default -> "Unknown command";
        };
    }

    private String processCreateCommand(List<String> arguments) {

        String cocktailName = arguments.get(0);

        if (arguments.size() % 2 == 0) {
            return "Arguments format is incorrect";
        }

        Set<Ingredient> ingredientsToAdd = new HashSet<>();

        for (int i = 1; i < arguments.size(); i += 2) {

            ingredientsToAdd.add(new Ingredient(arguments.get(i), arguments.get(i + 1)));
        }

        try {

            this.cocktailStorage.createCocktail(new Cocktail(cocktailName, ingredientsToAdd));
        } catch (CocktailAlreadyExistsException e) {

            return "There is already a cocktail with the same name";
        }

        return "Created cocktail with name " + cocktailName;
    }

    private String processGetAllCommand() {

        return this.cocktailStorage.getCocktails().toString();
    }

    private String processGetByNameCommand(List<String> arguments) {

        try {

            return this.cocktailStorage.getCocktail(arguments.get(0)).toString();
        } catch (CocktailNotFoundException c) {

            return "There is not a cocktail with such a name";
        }
    }

    private String processGetByIngredientCommand(List<String> arguments) {

        return this.cocktailStorage.getCocktailsWithIngredient(arguments.get(0)).toString();
    }

    private String processDisconnectCommand() {

        return "Disconnected from the server";
    }
}
