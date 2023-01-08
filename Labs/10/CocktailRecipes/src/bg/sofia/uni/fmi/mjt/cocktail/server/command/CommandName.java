package bg.sofia.uni.fmi.mjt.cocktail.server.command;

public enum CommandName {

    CREATE_COMMAND("create"),
    GET_ALL_COMMAND("get all"),
    GET_BY_NAME_COMMAND("get by-name"),
    GET_BY_INGREDIENTS_COMMAND("get by-ingredients"),
    DISCONNECT_COMMAND("disconnect");

    private final String commandName;

    CommandName(String commandName) {
        this.commandName = commandName;
    }

    public String getCommandName() {
        return this.commandName;
    }
}
