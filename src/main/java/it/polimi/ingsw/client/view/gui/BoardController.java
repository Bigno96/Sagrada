package it.polimi.ingsw.client.view.gui;

import it.polimi.ingsw.server.model.windowcard.WindowCard;

import javafx.fxml.FXML;
import javafx.scene.control.TextArea;


import it.polimi.ingsw.server.model.windowcard.WindowCard;
import javafx.event.ActionEvent;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

import java.util.List;


public class BoardController implements ControlInterface{

    GuiSystem guiSystem;

    @FXML
    public ImageView myWind;
    @FXML
    public ImageView dxWind;
    @FXML
    public ImageView sxWind;
    @FXML
    public ImageView secondWind;
    @FXML
    public TextArea textArea;


    BoardController(GuiSystem guiSystem){
        this.guiSystem = guiSystem;

    }

    public void print(String s) {

        this.textArea.appendText("\n"+s);

    }

    @Override
    public void setGuiSystem(GuiSystem guiSystem) {

        this.textArea.setText("La partita è iniziata!\nBuona fortuna!");

    }

    @Override
    public void setList(List<WindowCard> cards) {

    }

}
