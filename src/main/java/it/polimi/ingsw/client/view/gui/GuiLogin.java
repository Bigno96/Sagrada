package it.polimi.ingsw.client.view.gui;

import it.polimi.ingsw.client.ClientMain;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;

public class GuiLogin{

    ClientMain clientMain;

    public static void loginStart() {
        Stage window = new Stage();
        window.initModality(Modality.APPLICATION_MODAL);
        window.setTitle("Login");

        Label label = new Label();
        label.setText("Welcome");

        //Form
        TextField userName = new TextField();

        Button button = new Button("Continue");
        //button.setOnAction(e-> guiSystem.setUserName( userName.getText()));

        VBox layout = new VBox(10);
        layout.getChildren().addAll(label, button);
        layout.setAlignment(Pos.CENTER);

        Scene scene = new Scene(layout);
        window.setScene(scene);
        window.showAndWait();

    }

    public void setClientMain(ClientMain clientMain) {
        this.clientMain = clientMain;
    }

}