package it.polimi.ingsw.client.view.gui;

import javafx.fxml.FXML;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.text.Text;

import java.awt.*;
import java.util.List;

public class WaitingController extends ControlInterface{

    @FXML
    public TextArea textArea;
    @FXML
    public TextField textField;

    private GuiSystem guiSystem;

    public void setGuiSystem(GuiSystem guiSystem) {
        this.guiSystem = guiSystem;
    }

    public void print(String s) {

        System.out.println("waiting"+s);
        textField.setText(s);

    }
}
