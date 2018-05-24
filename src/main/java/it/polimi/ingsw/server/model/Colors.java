package it.polimi.ingsw.server.model;

import java.util.Random;

public enum Colors {
    YELLOW, RED, BLUE, GREEN, MAGENTA, WHITE, NULL;

    private static final Colors[] values = Colors.values();
    private static final Random random = new Random();

    public static Colors random() {
        return values[random.nextInt(values.length)];
    }

    public static Colors parseColor(String string) {
        if (string.equals("YELLOW"))
            return Colors.YELLOW;
        else if (string.equals("RED"))
            return Colors.RED;
        else if (string.equals("BLUE"))
            return Colors.BLUE;
        else if (string.equals("GREEN"))
            return Colors.GREEN;
        else if (string.equals("MAGENTA"))
            return Colors.MAGENTA;
        else if (string.equals("WHITE"))
            return Colors.WHITE;

        return Colors.NULL;
    }

}
