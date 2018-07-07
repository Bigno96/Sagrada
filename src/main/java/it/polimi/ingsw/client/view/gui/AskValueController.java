package it.polimi.ingsw.client.view.gui;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.input.MouseEvent;

public class AskValueController {

    @FXML
    public Button button;
    @FXML
    public TextArea textArea;
    private AskValueWindow askValueWindow;


    public void choose(MouseEvent mouseEvent) {

        if(textArea.getText() == null){

            Platform.runLater(() -> {

                AlertBox alertBox = new AlertBox();
                alertBox.display("Errore nell'inserimento del valore", "Inserisci un valore da 1 a 6");

            });

        }else if( (Integer.parseInt(textArea.getText())) < 6 && (Integer.parseInt(textArea.getText())) > 1){

            askValueWindow.getBoardController().resultValue = (Integer.parseInt(textArea.getText()));
            askValueWindow.closeWindow();

        }

    }


    void setAskValueWindow(AskValueWindow askValueWindow) {

        this.askValueWindow = askValueWindow;

    }
}
