package it.polimi.ingsw.client.view.gui;

import javafx.scene.input.MouseEvent;

public class AskBooleanController {

    AskBooleanWindow askBooleanWindow;

    public void minus(MouseEvent mouseEvent) {

        askBooleanWindow.getBoardController().resultBoolaen = 1;
        askBooleanWindow.closeWindow();

    }

    public void plus(MouseEvent mouseEvent) {

        askBooleanWindow.getBoardController().resultBoolaen = 2;
        askBooleanWindow.closeWindow();

    }

    public void setAskBooleanWindow(AskBooleanWindow askBooleanWindow) {


        this.askBooleanWindow = askBooleanWindow;

    }

}
