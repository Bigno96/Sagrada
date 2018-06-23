package it.polimi.ingsw.server.model;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Random;
import java.util.function.Consumer;
import java.util.function.Supplier;

public enum Colors implements Serializable {
    YELLOW, RED, BLUE, GREEN, MAGENTA, WHITE;

    private static final Colors[] values = Colors.values();
    private static final Random random = new Random();

    public static Colors random() {
        return values[random.nextInt(values.length)];
    }

    public static Colors parseColor(String string) {
        HashMap<String, Supplier<Colors>> mapColor = new HashMap<>();

        Supplier<Colors> yellow = () -> Colors.YELLOW;
        Supplier<Colors> red = () -> Colors.RED;
        Supplier<Colors> blue = () -> Colors.BLUE;
        Supplier<Colors> green = () -> Colors.GREEN;
        Supplier<Colors> magenta = () -> Colors.MAGENTA;
        Supplier<Colors> white = () -> Colors.WHITE;

        mapColor.put("YELLOW", yellow);
        mapColor.put("RED", red);
        mapColor.put("BLUE", blue);
        mapColor.put("GREEN", green);
        mapColor.put("MAGENTA", magenta);

        if (mapColor.containsKey(string))
            return mapColor.get(string).get();

        return white.get();
    }

}
