package it.polimi.ingsw.client;

import it.polimi.ingsw.client.network.ServerSpeaker;
import it.polimi.ingsw.client.view.ViewInterface;
import it.polimi.ingsw.client.view.cli.CliSystem;
import it.polimi.ingsw.client.view.gui.ClientGUIController;
import it.polimi.ingsw.client.view.gui.GuiSystem;
import it.polimi.ingsw.parser.messageparser.ViewMessageParser;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

import static java.lang.System.*;

public class ClientMain extends Application {

    private Stage primaryStage;

    private ViewMessageParser dictionary;

    private static final String TITLE_CLIENT_PAGE = "Quale modalità scegli?";
    private static final String CLI_CHOSEN = "Stai usando la CLI";

    @Override
    public void start(Stage primaryStage) {

        this.primaryStage = primaryStage;
        primaryStage.resizableProperty().setValue(Boolean.FALSE);
        this.primaryStage.setTitle(dictionary.getMessage(TITLE_CLIENT_PAGE));
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

    public void openCLI() {
        ViewInterface graphic = new CliSystem();

        Platform.runLater(() -> {
            Parent root = null;
            FXMLLoader loader = new FXMLLoader(getClass().getClassLoader().getResource("fxml/CLISystem.fxml"));
            try {
                root = loader.load();
            } catch (IOException e) {
                e.printStackTrace();
            }
            assert root != null;
            primaryStage.setScene(new Scene(root));

            primaryStage.show();
        });

        out.println(dictionary.getMessage(CLI_CHOSEN));
            graphic.startGraphic();
    }

    public void openGUI() {
        GuiSystem guiSystem = new GuiSystem(primaryStage);
        guiSystem.startGraphic();
    }
}