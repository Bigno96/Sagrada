package it.polimi.ingsw.client;

import it.polimi.ingsw.client.view.ViewInterface;
import it.polimi.ingsw.client.view.cli.CliSystem;
import it.polimi.ingsw.client.view.gui.ClientGUIController;
import it.polimi.ingsw.client.view.gui.ClosingWindow;
import it.polimi.ingsw.client.view.gui.GuiSystem;
import it.polimi.ingsw.exception.IDNotFoundException;
import it.polimi.ingsw.exception.PositionException;
import it.polimi.ingsw.exception.ValueException;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.FileNotFoundException;
import java.io.IOException;

import static java.lang.System.*;

public class ClientMain extends Application {

    GuiSystem guiSystem;
    private Stage primaryStage;
    private boolean CLI;
    private boolean GUI;

    @Override
    public void start(Stage primaryStage) {

        this.primaryStage = primaryStage;
        this.primaryStage.setTitle("How do you wanna play?");
        initRootLayout();

    }

    private void initRootLayout() {

        Platform.runLater(() -> {
            Parent root = null;

            FXMLLoader loader  = new FXMLLoader(getClass().getClassLoader().getResource("fxml/MainPage.fxml"));
            try {
                root = loader.load();
            } catch (IOException e) {
                e.printStackTrace();
            }

            assert root != null;
            primaryStage.setScene(new Scene(root));

            ClientGUIController ctrl = loader.getController();
            ctrl.setClientMain(this);

            primaryStage.show();
        });

    }

    public static void main(String[] args) {
        launch(args);
    }

    public void openCLI(){
        ViewInterface graphic = new CliSystem();

        ClosingWindow closingWindow = new ClosingWindow(primaryStage);
        closingWindow.start();

        out.println("CLI graphic chosen");
        try {
            graphic.startGraphic();
        } catch (FileNotFoundException | IDNotFoundException | PositionException | ValueException e) {
            e.printStackTrace();
        }

    }

    public void openGUI() {
        guiSystem = new GuiSystem(primaryStage);
        guiSystem.start();
    }
}