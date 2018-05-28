package it.polimi.ingsw.client.view.gui;

import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.*;

import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;

public class GuiAskConnection{

    private GuiSystem guiSystem;

    Stage LoginWindow;

    public GuiAskConnection(GuiSystem guiSystem){
        this.guiSystem = guiSystem;
    }

    public void display(Stage window){

        LoginWindow = window;

        LoginWindow.initModality(Modality.APPLICATION_MODAL);
        LoginWindow.setTitle("Choose connection");
        Button button = new Button("Continue");

        //ChoiceBoxes
        ChoiceBox<String> choiceBox = new ChoiceBox<>();

        //getItems
        choiceBox.getItems().add("RMI");
        choiceBox.getItems().add("socket");

        //set Default value
        choiceBox.setValue("RMI");

        button.setOnAction(e -> getChoice(choiceBox));

        //Layout
        VBox layout = new VBox(10);
        layout.setPadding(new Insets(20, 20, 20 , 20));
        layout.getChildren().addAll(choiceBox, button);

        Scene scene = new Scene(layout, 300, 100);
        window.setScene(scene);
        window.show();

    }

    //Set Connection
    private void getChoice(ChoiceBox<String> choiceBox){
        if(choiceBox.getValue().equals("RMI")){
            guiSystem.setConnection("RMI");
        } else {
            guiSystem.setConnection("Socket");
        }
        closeWindow();
    }

    private void closeWindow(){
        LoginWindow.close();
    }

}