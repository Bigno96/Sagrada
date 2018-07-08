package it.polimi.ingsw.client.view.gui;

import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.IOException;

import static java.lang.System.out;

class AskBooleanWindow {

    private static final String ASK_BOOLEAN_PATH = "fxml/AskBooleanWindow.fxml";

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
            FXMLLoader loader = new FXMLLoader(getClass().getClassLoader().getResource(ASK_BOOLEAN_PATH));
            try {
                root = loader.load();
            } catch (IOException e) {
                out.println(e.getMessage());
            }

            assert root != null;
            window.setScene(new Scene(root));
            window.setOnCloseRequest(e -> closeProgram());

            AskBooleanController askController = loader.getController();
            askController.setAskBooleanWindow(this);
            window.setOnCloseRequest(e -> closeProgram());

            window.show();
        });

    }

    private void closeProgram(){

        boardController.setResultBoolean(0);
        window.close();

    }

    void closeWindow(){

        window.close();

    }

    BoardController getBoardController() {
        return boardController;
    }
}
