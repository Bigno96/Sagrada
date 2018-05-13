package model.toolcard;

import model.Colors;

public class ToolCard {

    private String name;
    private int id;
    private Colors diceColor;

    public ToolCard(int id, String name, Colors diceColor) {
        this.name = name;
        this.id = id;
        this.diceColor = diceColor;
    }

    public String getName() {
        return name;
    }

    public int getId() {
        return id;
    }

    public Colors getDiceColor() {
        return diceColor;
    }
}
