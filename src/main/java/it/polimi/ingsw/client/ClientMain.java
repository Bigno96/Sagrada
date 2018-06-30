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

/**
 * ClientMain
 *
 * At the opening you can choose to continue to play into GUI or close it and play into CLI
 */
public class ClientMain extends Application {

    private Stage primaryStage;

    private ViewMessageParser dictionary;
/*
    private static final String TITLE_CLIENT_PAGE = "Quale modalità scegli?";
    private static final String CLI_CHOSEN = "Stai usando la CLI";
*/

    /**
     * @param primaryStage initialize the base stage
     */
    @Override
    public void start(Stage primaryStage) {

        this.primaryStage = primaryStage;
        primaryStage.resizableProperty().setValue(Boolean.FALSE);                    //disable resizable
        this.primaryStage.setTitle("How do you wanna play?");
        //this.primaryStage.setTitle(dictionary.getMessage(TITLE_CLIENT_PAGE));
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

    /**
     * Close GUI and continue into CLI
     */
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

        out.println("CLI graphic chosen");
        //out.println(dictionary.getMessage(CLI_CHOSEN));
            graphic.startGraphic();
    }

    public void openGUI() {
        GuiSystem guiSystem = new GuiSystem(primaryStage);
        guiSystem.startGraphic();
    }


    public static void main(String[] args) {
        launch(args);
    }
}