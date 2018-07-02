package it.polimi.ingsw.client.view.gui;

import it.polimi.ingsw.server.model.windowcard.WindowCard;
import javafx.fxml.FXML;
import javafx.scene.control.TextField;

import java.util.List;

public class WaitingController implements ControlInterface{

    @FXML
    public TextField textField;

    private GuiSystem guiSystem;

    public void setGuiSystem(GuiSystem guiSystem) {
        this.guiSystem = guiSystem;
        this.textField.setText("La partita sta per iniziare");

    }

    @Override
    public void setList(List<WindowCard> cards) {

    }

    public void print(String s) {

        this.textField.appendText("\n"+s);

    }

}
