package it.polimi.ingsw.client.view.gui;

import javafx.scene.layout.VBox;
import javafx.stage.*;
import javafx.scene.*;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.geometry.*;

public class AlertBox implements Runnable{

    Stage window;

    public AlertBox(){
        
        window = new Stage();

    }

    public static void display(String message){

        Stage window = null;
       // window.initModality(Modality.APPLICATION_MODAL);

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

    public void close(){

        window.hide();

    }

    public void print(String s){

        window.show();

    }

    @Override
    public void run() {
        /*
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
        window.show();*/
    }

}
