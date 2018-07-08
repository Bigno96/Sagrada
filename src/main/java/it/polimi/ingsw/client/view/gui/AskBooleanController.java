package it.polimi.ingsw.client.view.gui;

import javafx.scene.input.MouseEvent;

public class AskBooleanController {

    private AskBooleanWindow askBooleanWindow;

    public void minus(MouseEvent mouseEvent) {

        askBooleanWindow.getBoardController().setResultBoolean(1);
        askBooleanWindow.closeWindow();
        notifyAll();

    }

    public void plus(MouseEvent mouseEvent) {

        askBooleanWindow.getBoardController().setResultBoolean(2);
        askBooleanWindow.closeWindow();
        notifyAll();

    }

    void setAskBooleanWindow(AskBooleanWindow askBooleanWindow) {

        this.askBooleanWindow = askBooleanWindow;

    }

}
