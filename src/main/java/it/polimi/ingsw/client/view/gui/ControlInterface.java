package it.polimi.ingsw.client.view.gui;

import it.polimi.ingsw.server.model.windowcard.WindowCard;

import java.util.List;

public interface ControlInterface {

    void print(String message);

    void setGuiSystem(GuiSystem guiSystem);

    void setList(List<WindowCard> cards);

}