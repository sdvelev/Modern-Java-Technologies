package bg.sofia.uni.fmi.mjt.cocktail.server.command;

import bg.sofia.uni.fmi.mjt.cocktail.server.Cocktail;
import bg.sofia.uni.fmi.mjt.cocktail.server.Ingredient;
import bg.sofia.uni.fmi.mjt.cocktail.server.storage.CocktailStorage;
import bg.sofia.uni.fmi.mjt.cocktail.server.storage.exceptions.CocktailAlreadyExistsException;
import bg.sofia.uni.fmi.mjt.cocktail.server.storage.exceptions.CocktailNotFoundException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import static org.mockito.Mockito.*;

public class CommandExecutorTest {



    private CocktailStorage cocktailStorageMock = Mockito.mock(CocktailStorage.class);

    private CommandExecutor commandExecutor = new CommandExecutor(cocktailStorageMock);

    @Test
    void testExecuteCommandCreateCommandSuccessfully() {

        Command commandToAdd = new Command("create", List.of("manhattan", "whisky", "100ml", "ice", "1cube"));

        Cocktail cocktailToAdd = new Cocktail("manhattan", Set.of(new Ingredient("whisky", "100ml"),
            new Ingredient("ice", "1cube")));

        String expectedResponse = "Created cocktail with name manhattan";
        String response = this.commandExecutor.executeCommand(commandToAdd);

        try {

            verify(cocktailStorageMock, times(1)).createCocktail(cocktailToAdd);
        } catch(CocktailAlreadyExistsException e) {

            Assertions.fail("Unexpected CocktailAlreadyExistException is thrown");
        }

        Assertions.assertEquals(expectedResponse, response, "Successful created response is expected " +
            "but not send");
    }

    @Test
    void testExecuteCommandGetAllCommand() {

        Command commandToAdd = new Command("get all", new ArrayList<>());

        Cocktail cocktailToAdd = new Cocktail("manhattan", Set.of(new Ingredient("whisky", "100ml"),
            new Ingredient("ice", "1cube")));

        when(this.cocktailStorageMock.getCocktails()).thenReturn(List.of(cocktailToAdd));

        String expectedResponseFirst = "[Cocktail{name='manhattan', ingredients=[Ingredient{name='whisky', amount='100ml'}, " +
            "Ingredient{name='ice', amount='1cube'}]}]";
        String expectedResponseSecond = "[Cocktail{name='manhattan', ingredients=[Ingredient{name='ice', " +
            "amount='1cube'}, Ingredient{name='whisky', amount='100ml'}]}]";
        String response = this.commandExecutor.executeCommand(commandToAdd);

        Assertions.assertTrue(response.equalsIgnoreCase(expectedResponseFirst) ||
        response.equalsIgnoreCase(expectedResponseSecond), "Successful get all response is expected " +
            "but not send correctly");
    }

    @Test
    void testExecuteCommandGetByNameCommand()  throws CocktailNotFoundException{

        Command commandToAdd = new Command("get by-name", List.of("manhattan"));

        Cocktail cocktailToAdd = new Cocktail("manhattan", Set.of(new Ingredient("whisky", "100ml"),
            new Ingredient("ice", "1cube")));

        when(this.cocktailStorageMock.getCocktail("manhattan")).thenReturn(cocktailToAdd);

        String expectedResponseFirst = "Cocktail{name='manhattan', ingredients=[Ingredient{name='whisky', amount='100ml'}, " +
            "Ingredient{name='ice', amount='1cube'}]}";
        String expectedResponseSecond = "Cocktail{name='manhattan', ingredients=[Ingredient{name='ice', amount='1cube'}," +
            " Ingredient{name='whisky', amount='100ml'}]}";
        String response = this.commandExecutor.executeCommand(commandToAdd);

        Assertions.assertTrue(response.equalsIgnoreCase(expectedResponseFirst) ||
            response.equalsIgnoreCase(expectedResponseSecond), "Successful get by-name response is expected " +
            "but not send correctly");
    }

    @Test
    void testExecuteCommandGetByNameCommandThrowException()  throws CocktailNotFoundException {

        Command commandToAdd = new Command("get by-name", List.of("manhattan"));

        when(this.cocktailStorageMock.getCocktail("manhattan")).
            thenThrow(new CocktailNotFoundException("There is not a cocktail with such a name"));

        String expectedResponse = "There is not a cocktail with such a name";

        String response = this.commandExecutor.executeCommand(commandToAdd);

        Assertions.assertEquals(expectedResponse, response, "There is not such a cocktail response is expected" +
            "but not returned");
    }

    @Test
    void testExecuteCommandGetByIngredientCommand() {

        Command commandToAdd = new Command("get by-ingredient", List.of("whisky"));

        Cocktail cocktailToAdd = new Cocktail("manhattan", Set.of(new Ingredient("whisky", "100ml"),
            new Ingredient("ice", "1cube")));

        when(this.cocktailStorageMock.getCocktailsWithIngredient("whisky")).
            thenReturn(List.of(cocktailToAdd));

        String expectedResponseFirst = "[Cocktail{name='manhattan', ingredients=[Ingredient{name='whisky', amount='100ml'}, " +
            "Ingredient{name='ice', amount='1cube'}]}]";
        String expectedResponseSecond = "[Cocktail{name='manhattan', ingredients=[Ingredient{name='ice', " +
            "amount='1cube'}, Ingredient{name='whisky', amount='100ml'}]}]";

        String response = this.commandExecutor.executeCommand(commandToAdd);

        Assertions.assertTrue(response.equalsIgnoreCase(expectedResponseFirst) ||
            response.equalsIgnoreCase(expectedResponseSecond), "Successful get by-ingredient response is expected " +
            "but not send correctly");
    }

    @Test
    void testExecuteCommandDisconnectCommand() {

        Command commandToAdd = new Command("disconnect", List.of("whisky"));

        String expectedResponse = "Disconnected from the server";

        String response = this.commandExecutor.executeCommand(commandToAdd);

        Assertions.assertEquals(expectedResponse, response,"Successful disconnect response is expected " +
            "but not send correctly");
    }

    @Test
    void testExecuteCommandUnknownCommand() {

        Command commandToAdd = new Command("modify", List.of("whisky"));

        String expectedResponse = "Unknown command";

        String response = this.commandExecutor.executeCommand(commandToAdd);

        Assertions.assertEquals(expectedResponse, response,"Unknown command response is expected " +
            "but not send correctly");
    }
}
