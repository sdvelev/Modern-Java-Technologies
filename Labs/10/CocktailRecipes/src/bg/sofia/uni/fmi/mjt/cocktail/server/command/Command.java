package bg.sofia.uni.fmi.mjt.cocktail.server.command;

import java.util.List;

public class Command {

    private String command;
    private List<String> arguments;
    public Command(String command, List<String> arguments) {
        this.command = command;
        this.arguments = arguments;
    }

    public String command() {
        return command;
    }

    public List<String> arguments() {
        return arguments;
    }
}
