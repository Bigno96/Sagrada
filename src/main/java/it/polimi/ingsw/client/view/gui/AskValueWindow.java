package it.polimi.ingsw.client.view.gui;

import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.IOException;

import static java.lang.System.out;

class AskValueWindow {

    private static final String ASK_VALUE_FXML = "fxml/AskValue.fxml";

    private Stage window;
    private BoardController boardController;

    void display(BoardController boardController) {

        window = new Stage();
        window.initModality(Modality.APPLICATION_MODAL);
        window.setTitle("Scegli");
        window.setMinWidth(200);

        this.boardController = boardController;

        Platform.runLater(() -> {
            Parent root = null;
            FXMLLoader loader = new FXMLLoader(getClass().getClassLoader().getResource(ASK_VALUE_FXML));
            try {
                root = loader.load();
            } catch (IOException e) {
                out.println(e.getMessage());
            }

            assert root != null;
            window.setScene(new Scene(root));
            window.setOnCloseRequest(e -> closeProgram());

            AskValueController askController = loader.getController();
            askController.setAskValueWindow(this);
            window.setOnCloseRequest(e -> closeProgram());

            window.show();
        });

    }

    private void closeProgram(){

        boardController.setResultValue(0);
        window.close();

    }

    void closeWindow(){

        window.close();

    }

    BoardController getBoardController() {
        return boardController;
    }
}