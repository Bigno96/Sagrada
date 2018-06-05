package it.polimi.ingsw.client;

import it.polimi.ingsw.client.view.ViewInterface;
import it.polimi.ingsw.client.view.cli.CliSystem;
import it.polimi.ingsw.exception.IDNotFoundException;
import it.polimi.ingsw.exception.PositionException;
import it.polimi.ingsw.exception.ValueException;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URL;

public class ClientMainR extends Application {

    private BorderPane rootLayout;
    private Stage primaryStage;
    private ClientMainC controller;
    private ViewInterface graphic;

    @Override
    public void start(Stage primaryStage) {

        this.primaryStage = primaryStage;
        this.primaryStage.setTitle("How do you wanna play?");
        initRootLayout();

    }


    public void initRootLayout() {


        Platform.runLater(() -> {
            Parent root = null;

            FXMLLoader loader = new FXMLLoader();

            loader = new FXMLLoader(getClass().getClassLoader().getResource("fxml/MainPage.fxml"));
            try {
                root = loader.load();
            } catch (IOException e) {
                e.printStackTrace();
            }

            primaryStage.setScene(new Scene(root));

            ClientMainC ctrl = loader.getController();
            ctrl.setClientMainR(this);

            primaryStage.show();
        });
    }


    public Stage getPrimaryStage() {
        return primaryStage;
    }

    public static void main(String[] args) {
        launch(args);
    }

    public void openCLI(){
        graphic = new CliSystem();

        primaryStage.hide();

        System.out.println("CLI graphic chosen");
        try {
            graphic.startGraphic();
        } catch (FileNotFoundException | IDNotFoundException | PositionException | ValueException e) {
            e.printStackTrace();
        }
    }

    public void loginInit() {
        Platform.runLater(() -> {
            Parent root = null;

            FXMLLoader loader = new FXMLLoader();

            loader = new FXMLLoader(getClass().getClassLoader().getResource("fxml/LoginPage.fxml"));
            try {
                root = loader.load();
            } catch (IOException e) {
                e.printStackTrace();
            }

            primaryStage.setScene(new Scene(root));

            ClientMainC ctrl = loader.getController();
            ctrl.setClientMainR(this);

            primaryStage.show();
        });
    }
}