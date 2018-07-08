package it.polimi.ingsw.client.view.gui;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.input.MouseEvent;

public class AskValueController {

    private static final String ERROR = "Errore nell'inserimento del valore";
    private static final String INSERT_VALUE = "Inserisci un valore da 1 a 6";

    @FXML
    public Button button;
    @FXML
    public TextArea textArea;
    private AskValueWindow askValueWindow;


    public void choose(MouseEvent mouseEvent) {

        if(textArea.getText() == null){

            Platform.runLater(() -> AlertBox.display(ERROR, INSERT_VALUE));

        }else if( (Integer.parseInt(textArea.getText())) < 6 && (Integer.parseInt(textArea.getText())) > 1){

            askValueWindow.getBoardController().setResultValue(Integer.parseInt(textArea.getText()));
            askValueWindow.closeWindow();

        }

    }


    void setAskValueWindow(AskValueWindow askValueWindow) {

        this.askValueWindow = askValueWindow;

    }
}
