package model;

import java.util.Random;

public enum Colors {
    YELLOW, RED, BLUE, GREEN, VIOLET, NULL;

    private static final Colors[] values = Colors.values();
    private static final Random random = new Random();

    public static Colors random() {
        return values[random.nextInt(values.length)];
    }
}
