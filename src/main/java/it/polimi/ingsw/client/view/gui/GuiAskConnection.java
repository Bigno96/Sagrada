package it.polimi.ingsw.client.view.gui;

import javafx.application.Platform;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.*;

import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;

import it.polimi.ingsw.client.network.ServerSpeaker;
import it.polimi.ingsw.client.network.rmi.RmiServerSpeaker;
import it.polimi.ingsw.client.network.socket.SocketServerSpeaker;
import it.polimi.ingsw.exception.SamePlayerException;

import java.util.HashMap;
import java.util.Scanner;

import static java.lang.System.out;

public class GuiAskConnection{

    private GuiSystem guiSystem;

    private boolean connect;
    private Stage loginWindow;
    private TextField userName = new TextField();
    private TextField ip = new TextField();

    private ServerSpeaker serverSpeaker;
    private Scanner inKeyboard;
    private HashMap<String, ServerSpeaker> connParam;

    public GuiAskConnection(GuiSystem guiSystem){
        this.guiSystem = guiSystem;
    }

    public void display(Stage window){

        loginWindow = window;

        loginWindow.initModality(Modality.APPLICATION_MODAL);
        loginWindow.setTitle("Choose connection");
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
        do {
            if (choiceBox.getValue().equals("RMI")) {
                guiSystem.setConnection("RMI");
            } else {
                guiSystem.setConnection("Socket");
            }

            guiSystem.setUserName(userName.getText());
            guiSystem.setIP(ip.getText());
            if (!validIP(ip.getText())) {               //If IP is incorrect open IncorrectIPWindow and ConnectionWinodow
                Platform.runLater(() -> {
                    GuiAskConnection connectionWindows = new GuiAskConnection(this.guiSystem);
                    Stage window = new Stage();
                    try {
                        connectionWindows.display(window);
                    } catch (Exception e) {
                        out.println(e.getMessage());
                    }

                });
                Platform.runLater(() -> {
                    IncorrectIP incorrectIPWindow = new IncorrectIP();
                    Stage window = new Stage();
                    try {
                        incorrectIPWindow.display(window);
                    } catch (Exception e) {
                        out.println(e.getMessage());
                    }

                });
            }
            //If IP is correct try to connect

        }while (connect);
            closeWindow();
    }

    private boolean validIP(String ip) {
        if (ip.isEmpty())
            return false;

        String[] parts = ip.split("\\.");

        if (parts.length != 4 && parts.length != 6)
            return false;

        for (String s : parts) {
            int i = Integer.parseInt(s);
            if (i < 0 || i > 255)
                return false;
        }

        return !ip.endsWith(".");
    }

    private void closeWindow(){
        loginWindow.close();
    }

}