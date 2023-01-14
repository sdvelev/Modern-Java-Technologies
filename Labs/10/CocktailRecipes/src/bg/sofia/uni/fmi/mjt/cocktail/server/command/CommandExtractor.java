package bg.sofia.uni.fmi.mjt.cocktail.server.command;

import java.util.ArrayList;
import java.util.List;

public class CommandExtractor {

    private final static String GET_COMMAND_STRING = "get";
    private final static String ALL_COMMAND_STRING = "all";
    private final static String BY_NAME_COMMAND_STRING = "by-name";
    private final static String BY_INGREDIENT_COMMAND_STRING = "by-ingredient";
    private final static String UNKNOWN_COMMAND_STRING = "unknown";
    private final static String INTERVAL_REGEX = " ";
    private final static String BEFORE_EQUALS = "=.+";
    private final static String AFTER_EQUALS = ".+=";
    private final static int GET_BY_NAME_INGREDIENT_ARGUMENTS = 3;

    private static Command createCommand(List<String> arguments, String[] lineArray) {

        arguments.add(lineArray[1]);

        for (int i = 2; i < lineArray.length; i++) {

            arguments.add(lineArray[i].replaceAll(BEFORE_EQUALS, ""));
            arguments.add(lineArray[i].replaceAll(AFTER_EQUALS, ""));
        }

        return new Command(lineArray[0], arguments);
    }

    private static Command getCommand(List<String> arguments, String[] lineArray) {

        if (lineArray[1].equalsIgnoreCase(ALL_COMMAND_STRING) && lineArray.length == 2) {

            return new Command(lineArray[0] + " " + lineArray[1], new ArrayList<>());
        } else if ((lineArray[1].equalsIgnoreCase(BY_NAME_COMMAND_STRING) ||
            lineArray[1].equalsIgnoreCase(BY_INGREDIENT_COMMAND_STRING)) &&
            lineArray.length == GET_BY_NAME_INGREDIENT_ARGUMENTS) {

            arguments.add(lineArray[2]);
            return new Command(lineArray[0] + " " + lineArray[1], arguments);
        }

        return new Command(UNKNOWN_COMMAND_STRING, new ArrayList<>());
    }

    public static Command newCommand(String clientInput) {

        List<String> arguments = new ArrayList<>();

        String[] lineArray = clientInput.split(INTERVAL_REGEX);

        if (lineArray[0].equalsIgnoreCase(CommandName.CREATE_COMMAND.getCommandName())) {

            return createCommand(arguments, lineArray);
        }
        else if (lineArray[0].equals(CommandName.DISCONNECT_COMMAND.getCommandName())) {

            return new Command(lineArray[0], new ArrayList<>());
        }
        else if (lineArray[0].equalsIgnoreCase(GET_COMMAND_STRING)) {

            return getCommand(arguments, lineArray);
        }

        return new Command(UNKNOWN_COMMAND_STRING, new ArrayList<>());
    }
}
