package it.polimi.ingsw.client;

import it.polimi.ingsw.client.network.ServerSpeaker;
import it.polimi.ingsw.client.view.gui.GuiAskConnection;
import it.polimi.ingsw.client.view.gui.GuiSystem;
import it.polimi.ingsw.client.view.gui.IncorrectIP;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.util.HashMap;
import java.util.Scanner;

import static java.lang.System.out;

public class ChooseGuiCli{

    private GuiSystem guiSystem;

    private boolean chooseCli;
    private Stage loginWindow;
    private TextField userName = new TextField();
    private TextField ip = new TextField();

    private ServerSpeaker serverSpeaker;
    private Scanner inKeyboard;
    private HashMap<String, ServerSpeaker> connParam;

    public void display (Stage window){

        loginWindow = window;

        loginWindow.setTitle("Choose connection");
        Button button = new Button("Continue");

        //ChoiceBoxes
        ChoiceBox<String> choiceBox = new ChoiceBox<>();

        //getItems
        choiceBox.getItems().add("GUI");
        choiceBox.getItems().add("CLI");

        //set Default value
        choiceBox.setValue("CLI");

        button.setOnAction(e -> getChoice(choiceBox));

        //Layout
        VBox layout = new VBox(10);
        layout.setPadding(new Insets(20, 20, 20 , 20));
        layout.getChildren().addAll(choiceBox, button, userName, ip);

        Label label = new Label();
        label.setText("Set your data");
        layout.setAlignment(Pos.CENTER);

        Scene scene = new Scene(layout, 300, 400);
        window.setScene(scene);
        window.show();

    }

    //Set Connection
    private void getChoice(ChoiceBox<String> choiceBox){
            if (choiceBox.getValue().equals("RMI")) {
                chooseCli = false;
            } else {
                chooseCli = true;
            }

        closeWindow();
    }

    private void closeWindow(){
        loginWindow.close();
    }

    public boolean isChooseCli() {
        return chooseCli;
    }

    public void setChooseCli(boolean chooseCli) {
        this.chooseCli = chooseCli;
    }
}
