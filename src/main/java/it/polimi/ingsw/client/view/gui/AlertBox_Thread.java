package it.polimi.ingsw.client.view.gui;

import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;

public class AlertBox_Thread implements Runnable {

    Stage window;

    public void run(String message){

        window.initModality(Modality.APPLICATION_MODAL);

        window.setTitle("Messaggio dal server");
        window.setMinWidth(250);

        Label label = new Label();
        label.setText(message);
        //Button closeButton = new Button("Ok");
        //closeButton.setOnAction(e -> window.close());

        VBox layout = new VBox(10);
        layout.getChildren().addAll(label);
        //layout.getChildren().addAll(closeButton);
        layout.setAlignment(Pos.CENTER);

        Scene scene = new Scene(layout);
        window.setScene(scene);
        window.show();

    }

    @Override
    public void run() {

    }
}
