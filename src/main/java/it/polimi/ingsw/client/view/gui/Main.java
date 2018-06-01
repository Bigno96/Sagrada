package it.polimi.ingsw.client.view.gui;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class Main extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception {
        //Parent loader = (Parent) new FXMLLoader(getClass().getClassLoader().getResource("NClientMain.fxml"));
        Parent root = FXMLLoader.load(getClass().getClassLoader().getResource("fxml/LoginPage.fxml"));
        //Parent root = FXMLLoader.load(getClass().getResource("sample/packageIntermezzo/sample.fxml"));
        primaryStage.setTitle("Hello World");
        primaryStage.setScene(new Scene(root, 300, 275));
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}