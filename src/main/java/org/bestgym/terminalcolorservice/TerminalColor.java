package org.bestgym.terminalcolorservice;

// För att få ett lite snyggare user experience :)
public class TerminalColor {
    public static void message(ColorPicker color, String message) {
        switch (color) {
            case RED -> System.out.println("\u001B[31m" + message + "\u001B[0m");
            case GREEN -> System.out.println("\u001B[32m" + message + "\u001B[0m");
            case YELLOW -> System.out.println("\u001B[33m" + message + "\u001B[0m");
            case BLUE -> System.out.println("\u001B[34m" + message + "\u001B[0m");
        }
    }
}
