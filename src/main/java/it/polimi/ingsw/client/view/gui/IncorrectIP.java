package it.polimi.ingsw.client.view.gui;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.*;

import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;

public class IncorrectIP {

    Stage incorrectIPWindow;

    public void display(Stage window) {

        incorrectIPWindow = window;

        incorrectIPWindow.initModality(Modality.APPLICATION_MODAL);
        incorrectIPWindow.setTitle("Error! Incorrect IP");

        Button button = new Button("Continue");
        button.setOnAction(e -> incorrectIPWindow.close());

        //Layout
        VBox layout = new VBox(10);
        layout.setPadding(new Insets(20, 20, 20, 20));
        layout.getChildren().addAll(button);

        Label label = new Label();
        label.setText("Set your data");
        layout.setAlignment(Pos.CENTER);

        Scene scene = new Scene(layout, 300, 100);
        window.setScene(scene);
        window.show();

    }
}
