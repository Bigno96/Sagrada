package it.polimi.ingsw.client.view.gui;

import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class ClosingWindow {

    private Stage primaryStage;

    public ClosingWindow(Stage primaryStage){
        this.primaryStage = primaryStage;
    }

    public void start() {
        Platform.runLater(() -> {
            Parent root = null;
            try {
                root = FXMLLoader.load(getClass().getClassLoader().getResource("fxml/ExitWindow.fxml"));
                //control = new ClientMainC();
            } catch (IOException e) {
                e.printStackTrace();
            }
            primaryStage.setTitle("...");
            primaryStage.setScene(new Scene(root, 0 , 0));
            primaryStage.show();
        });

    }
}
