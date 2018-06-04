package it.polimi.ingsw.client;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

import java.io.IOException;

public class ClientMainR extends Application {

    private BorderPane rootLayout;
    private Stage primaryStage;

    @Override
    public void start(Stage primaryStage) {

        this.primaryStage = primaryStage;
        this.primaryStage.setTitle("How do you wanna play?");
       initRootLayout();

    }


    public void initRootLayout() {
        Platform.runLater(() -> {
            Parent root = null;
            try {
                root = FXMLLoader.load(getClass().getClassLoader().getResource("fxml/MainPage.fxml"));
                //control = new ClientMainC();
                //FXMLLoader loader = new FXMLLoader(getClass().getResource(
                //        "PersonEditor.fxml"));
                //Parent root = (Parent) loader.load();
            } catch (IOException e) {
                e.printStackTrace();
            }

            //ClientMainC controller = loader.getController();
            //controller.setStage(this.stage);

            //ClientMainC ctrl = loader.getController();
            //ctrl.init(table.getSelectionModel().getSelectedItem());

            primaryStage.setScene(new Scene(root, 700, 400));
            primaryStage.show();
        });
    }


    public Stage getPrimaryStage() {
        return primaryStage;
    }

    public static void main(String[] args) {
        launch(args);
    }

}
