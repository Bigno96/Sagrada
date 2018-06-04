package it.polimi.ingsw.client;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

//ClientMain open GUI and ask CLI or GUI
//Close GUI and play on CLI

import static java.lang.System.out;

public class ClientMainR extends Application {

    //ClientMainC control;

    @Override
    public void start(Stage primaryStage) throws Exception {

        Platform.runLater(() -> {
            Parent root = null;
            try {
                root = FXMLLoader.load(getClass().getClassLoader().getResource("fxml/MainPage.fxml"));
                //control = new ClientMainC();
            } catch (IOException e) {
                e.printStackTrace();
            }
            primaryStage.setTitle("How do you wanna play?");
            primaryStage.setScene(new Scene(root, 700, 400));
            primaryStage.show();
        });

      //  Thread.currentThread().wait();
/*
        if (control.GUI() == false) {
             //change Stage
        } else {
            primaryStage.close();
        }*/
    }

    public static void main(String[] args) {
        launch(args);
    }

}
