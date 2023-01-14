package bg.sofia.uni.fmi.mjt.cocktail.server.command;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.List;

public class CommandExtractorTest {

    @Test
    void testNewCommandCreateCommand() {

        String clientInput = "create manhattan whisky=100ml ice=1cube";
        Command commandToReturn = CommandExtractor.newCommand(clientInput);

        List<String> expectedArguments = List.of("manhattan", "whisky", "100ml", "ice", "1cube");

        Assertions.assertTrue(commandToReturn.command().equalsIgnoreCase("create"),
            "Command is expected to be create but it is not");
        Assertions.assertIterableEquals(expectedArguments, commandToReturn.arguments(),
            "Expected create arguments are not the same as the actual");
    }

    @Test
    void testNewCommandGetAllCommand() {

        String clientInput = "get all";
        Command commandToReturn = CommandExtractor.newCommand(clientInput);

        Assertions.assertTrue(commandToReturn.command().equalsIgnoreCase("get all"),
            "Get all command is expected but not return");

        Assertions.assertTrue(commandToReturn.arguments().isEmpty(),
            "Get all command is expected to have no arguments but it has");
    }

    @Test
    void testNewCommandGetByNameCommand() {

        String clientInput = "get by-name manhattan";
        Command commandToReturn = CommandExtractor.newCommand(clientInput);

        List<String> expectedArguments = List.of("manhattan");

        Assertions.assertTrue(commandToReturn.command().equalsIgnoreCase("get by-name"),
            "Get all command is expected but not return");


        Assertions.assertIterableEquals(expectedArguments, commandToReturn.arguments(),
            "Actual arguments of get by-name command are not the same as expected");
    }

    @Test
    void testNewCommandGetByIngredientCommand() {

        String clientInput = "get by-ingredient whisky";
        Command commandToReturn = CommandExtractor.newCommand(clientInput);

        List<String> expectedArguments = List.of("whisky");

        Assertions.assertTrue(commandToReturn.command().equalsIgnoreCase("get by-ingredient"),
            "Get all command is expected but not return");


        Assertions.assertIterableEquals(expectedArguments, commandToReturn.arguments(),
            "Actual arguments of get by-ingredient command are not the same as expected");
    }

    @Test
    void testNewCommandDisconnectCommand() {

        String clientInput = "disconnect";
        Command commandToReturn = CommandExtractor.newCommand(clientInput);

        Assertions.assertTrue(commandToReturn.command().equalsIgnoreCase("disconnect"),
            "Disconnect command is expected but not return");

        Assertions.assertTrue(commandToReturn.arguments().isEmpty(),
            "Disconnect command is expected to have no arguments but it has");
    }

    @Test
    void testNewCommandUnknownCommand() {

        String clientInput = "modify";
        Command commandToReturn = CommandExtractor.newCommand(clientInput);

        Assertions.assertTrue(commandToReturn.command().equalsIgnoreCase("unknown"),
            "Unknown command is expected but not return");

        Assertions.assertTrue(commandToReturn.arguments().isEmpty(),
            "Unknown command is expected to have no arguments but it has");
    }
}
