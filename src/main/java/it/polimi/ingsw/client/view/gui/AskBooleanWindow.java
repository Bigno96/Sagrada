package it.polimi.ingsw.client.view.gui;

import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.IOException;

public class AskBooleanWindow {

    private int result;
    private Stage window;
    private BoardController boardController;

    public void display(BoardController boardController) {

        window = new Stage();
        window.initModality(Modality.APPLICATION_MODAL);
        window.setTitle("Scegli");
        window.setMinWidth(200);

        this.boardController = boardController;

        Platform.runLater(() -> {
            Parent root = null;
            FXMLLoader loader = new FXMLLoader(getClass().getClassLoader().getResource("fxml/AskBooleanWindow.fxml"));
            try {
                root = loader.load();
            } catch (IOException e) {
                e.printStackTrace();
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

        boardController.resultBoolaen = 0;
        window.close();

    }

    void closeWindow(){

        window.close();

    }

    BoardController getBoardController() {
        return boardController;
    }
}
