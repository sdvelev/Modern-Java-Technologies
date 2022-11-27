package bg.sofia.uni.fmi.mjt.markdown;

public enum MarkdownSymbol {

    HEADING('#'),
    STAR('*'),
    BACK_QUOTE('`');

    private final char symbol;

    MarkdownSymbol(char symbol) {
        this.symbol = symbol;
    }

    char getSymbol() {
        return this.symbol;
    }

}
