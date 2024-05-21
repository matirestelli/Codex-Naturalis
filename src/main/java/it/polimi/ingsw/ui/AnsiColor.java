package it.polimi.ingsw.ui;

public enum AnsiColor {
    RESET("\u001B[0m"),
    BOLD("\u001b[1m"),
    BLUE_BACKGROUND("\u001B[44m"),
    GOLD_BLUE_BACKGROUND("\u001B[48;5;18m"),
    RED_BACKGROUND("\u001B[41m"),
    GOLD_RED_BACKGROUND("\u001B[48;5;214m"),
    YELLOW("\u001B[33m"),
    WHITE_BACKGROUND("\u001B[47m"),
    PURPLE_BACKGROUND("\u001B[45m"),
    GREEN_BACKGROUND("\u001B[48;5;34m"),
    YELLOW_BACKGROUND("\u001B[48;5;179m"),
    GOLD_PURPLE_BACKGROUND("\u001B[48;5;129m"),
    GOLD_GREEN_BACKGROUND("\u001B[48;5;28m"),
    GOLD("\u001B[33m");

    private final String code;

    AnsiColor(String code) {
        this.code = code;
    }

    @Override
    public String toString() {
        return code;
    }
}
